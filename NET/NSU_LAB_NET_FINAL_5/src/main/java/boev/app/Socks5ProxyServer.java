package boev.app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

import lombok.Data;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;


public class Socks5ProxyServer {

    private static final int SOCKS_VERSION = 0x05;
    private static final int TCP_CONNECT = 0x01;
    private static final int ADDR_TYPE_IPV4 = 0x01;
    private static final int ADDR_TYPE_DOMAIN = 0x03;
    public static final int BUFFER_SIZE = 8192;

    private int port;
    private Selector selector;

    public Socks5ProxyServer(int port) throws IOException {
        this.port = port;
        this.selector = Selector.open();
    }

    public void start() throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("SOCKS5 Proxy Server started on port " + port);
        Set<SelectionKey> selectedKeys;
        Iterator<SelectionKey> it;


        while (true){
            selector.select(1000);

            selectedKeys = selector.selectedKeys();
            it = selectedKeys.iterator();

            while(it.hasNext()){
                SelectionKey key = it.next();

                if (!key.isValid()) {
                    System.out.println("invalid key: " + key);
                    key.cancel();
                    it.remove();
                    continue;
                }
                if (key.isAcceptable()) {
                    register(serverChannel);
                } else if (key.isReadable() && key.isWritable()) {
                    responseTCPMessage(key);
                } else if (key.isConnectable()) {
                    System.out.println("is connectable");

                    ProxyData state = (ProxyData) key.attachment();
                    SocketChannel targetChannel = state.targetChannel;

                    if (targetChannel.finishConnect()) {
                        System.out.println("Connection to target server completed.");
                        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        handleConnect(state);
                    } else {
                        System.out.println("Still connecting...");
                    }
                }

                if (!key.isValid()) {
                    System.out.println("key not valid: " + key);
                    it.remove();
                    key.cancel();
                    continue;
                }
                it.remove();
            }

        }
    }

    private void responseTCPMessage(SelectionKey selectionKey){
        SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
        ProxyData state = (ProxyData) selectionKey.attachment();

        try{
            if(state.stage == ProxyData.STAGE_INITIAL){
                if (!handleSocksHandshake(clientChannel, state)) selectionKey.cancel();
            } else if(state.stage == ProxyData.STAGE_CONNECTING){
                if (!handleSocksRequest(state)) selectionKey.cancel();
            } else if (state.stage == ProxyData.STAGE_RELAYING) {
                if (!relayData(state)) selectionKey.cancel();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("~~~~~~~~~~~~~~~");
        }
    }

    private boolean relayData(ProxyData state) throws IOException {
        SocketChannel targetChannel = state.targetChannel;
        SocketChannel clientChannel = state.clientChannel;

        if (!targetChannel.isOpen()) {
            clientChannel.close();
            targetChannel.close();
            return false;
        }

        if (!clientChannel.isOpen()) {
            clientChannel.close();
            targetChannel.close();
            return false;
        }

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        buffer.clear();
        int bytesRead;

        try{
            bytesRead = clientChannel.read(buffer);
        } catch (SocketException e){
            targetChannel.close();
            clientChannel.close();
            return false;
        }


        System.out.println("bytesRead in relayData: " + bytesRead);
        if (bytesRead == -1) {
            targetChannel.close();
            clientChannel.close();
            return false;
        }

        buffer.flip();

        while (buffer.hasRemaining()) {
            targetChannel.write(buffer);
        }

        buffer.clear();
        try{
            bytesRead = targetChannel.read(buffer);
        } catch (SocketException e){
            targetChannel.close();
            clientChannel.close();
            return false;
        }

        System.out.println("bytesRead in relayData: " + bytesRead);
        if (bytesRead == -1) {
            targetChannel.close();
            clientChannel.close();
            return false;
        }

        buffer.flip();

        while (buffer.hasRemaining()) {
            clientChannel.write(buffer);
        }
        return true;
    }

    private boolean handleSocksRequest(ProxyData state) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        int bytesRead = state.clientChannel.read(buffer);
        if (bytesRead == -1) {
            state.targetChannel.close();
            state.clientChannel.close();
            return false;
        }

        SocketAddress targetAddress;

        if (bytesRead == 4 && buffer.get(0) == SOCKS_VERSION) {

            byte addressType = buffer.get(3);

            if (buffer.get(1) == TCP_CONNECT) {
                if (addressType == ADDR_TYPE_IPV4) {
                    System.out.println("ipv4 connection");
                    targetAddress = readIPv4(state);
                    state.typeConnection = typeConnection.TCP;
                } else if (addressType == ADDR_TYPE_DOMAIN) {
                    targetAddress = readDomainName(state);
                    System.out.println("domain connection");
                    state.typeConnection = typeConnection.DOMAIN;
                } else {
                    state.clientChannel.close();
                    return false;
                }
                connectToTarget(targetAddress, state);
            } else {
                state.clientChannel.close();
            }


        } else {
            System.out.println("bad connection");
            System.out.println("request: bytesRead: " + bytesRead + " SocksVersion: " + buffer.get(0));
            state.clientChannel.close();
            state.targetChannel.close();
            return false;
        }

        return true;
    }

    private void connectToTarget(SocketAddress socketAddress, ProxyData state) throws IOException {
        SocketChannel targetChannel = SocketChannel.open();
        targetChannel.configureBlocking(false);
        targetChannel.connect(socketAddress);
        System.out.println(socketAddress);

        targetChannel.register(selector, SelectionKey.OP_CONNECT, state);
        state.targetChannel = targetChannel;
        state.stage = ProxyData.STAGE_CONNECTING_TO_SERVER;
    }

    private SocketAddress readIPv4(ProxyData state) throws IOException {
        SocketChannel clientChannel = state.clientChannel;

        ByteBuffer buffer = ByteBuffer.allocate(6);

        buffer.clear();
        clientChannel.read(buffer);
        buffer.flip();

        byte[] ipBytes = new byte[4];
        buffer.get(ipBytes);
        state.ipAddress = InetAddress.getByAddress(ipBytes);
        state.port = buffer.getShort();
        System.out.println(state.port);
        System.out.println( state.ipAddress);

        return new InetSocketAddress(state.ipAddress, state.port);

    }

    private SocketAddress readDomainName(ProxyData state) throws IOException {
        SocketChannel clientChannel = state.clientChannel;

        ByteBuffer lenBuffer = ByteBuffer.allocate(1);
        lenBuffer.clear();
        clientChannel.read(lenBuffer);

        lenBuffer.flip();

        byte nameLen = lenBuffer.get();
        state.domainNameLength = nameLen;

        ByteBuffer buffer = ByteBuffer.allocate((nameLen + 2));
        buffer.clear();
        clientChannel.read(buffer);
        buffer.flip();

        byte[] domainBytes = new byte[nameLen];
        state.domainNameBytes = domainBytes;

        buffer.get(domainBytes);

        state.port = buffer.getShort();
        state.domainName = new String(domainBytes, StandardCharsets.UTF_8);
        state.ipAddress = resolveDNS(state.domainName);

        return new InetSocketAddress(state.ipAddress, state.port);

    }

    private boolean handleSocksHandshake(SocketChannel clientChannel, ProxyData data) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        buffer.clear();
        int bytesRead = clientChannel.read(buffer);

        if(bytesRead >= 2 && buffer.get(0) == SOCKS_VERSION){
            ByteBuffer response = ByteBuffer.allocate(2);
            response.clear();
            response.put((byte) SOCKS_VERSION);
            response.put((byte) 0x00);
            response.flip();
            clientChannel.write(response);
            data.clientChannel = clientChannel;
            data.stage = ProxyData.STAGE_CONNECTING;
        } else{
            clientChannel.close();
            return false;
        }
        return true;
    }

    private InetAddress resolveDNS(String dnsName) throws IOException{
        Resolver resolver = new SimpleResolver();
        Lookup lookup = new Lookup(dnsName, Type.A);
        lookup.setResolver(resolver);

        Record[] records = lookup.run();
        if(records == null || lookup.getResult() != Lookup.SUCCESSFUL){
            throw new IOException("Failed to resolve DNS: " + dnsName);
        }
        return ((ARecord) records[0]).getAddress();

    }
    private void register(ServerSocketChannel serverChannel) throws IOException {
//        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverChannel.accept();

        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, new ProxyData());
        System.out.println("Accepted client from " + socketChannel.getRemoteAddress());
    }

    private void handleConnect(ProxyData state) throws IOException{
        SocketChannel targetChannel = state.targetChannel;

        ByteBuffer response;

        if (state.typeConnection == typeConnection.TCP) {
            response = ByteBuffer.allocate(10);
        } else {
            response = ByteBuffer.allocate(4 + 1 + state.domainNameBytes.length + 2);
        }

        response.clear();
        response.put((byte) SOCKS_VERSION);

        response.put((byte) 0x00);
        response.put((byte) 0x00); // Резерв (0)
        response.put((state.typeConnection == typeConnection.TCP) ? (byte) ADDR_TYPE_IPV4 : (byte) ADDR_TYPE_DOMAIN);

        InetSocketAddress remoteAddress = (InetSocketAddress) targetChannel.getRemoteAddress();

        if (state.typeConnection == typeConnection.TCP) {
            response.put(state.ipAddress.getAddress()); // IP
        } else if (state.typeConnection == typeConnection.DOMAIN) {
            response.put(state.domainNameLength);
            response.put(state.domainNameBytes);
        }

        response.putShort((byte) remoteAddress.getPort()); // Порт

        response.flip();

        System.out.println("before while");

        do {
            state.clientChannel.write(response);
        } while (response.hasRemaining());

        System.out.println("after while");

        state.stage = ProxyData.STAGE_RELAYING;
        System.out.println("Connected successfully, now relaying data.");
    }
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java App <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try {
            new Socks5ProxyServer(port).start();
        } catch (IOException  e) {
            throw new RuntimeException(e);
        }
    }
}