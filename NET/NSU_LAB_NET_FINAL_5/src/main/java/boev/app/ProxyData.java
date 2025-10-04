package boev.app;

import java.net.InetAddress;
import java.nio.channels.SocketChannel;

public class ProxyData {
    public static final int STAGE_INITIAL = 0;
    public static final int STAGE_CONNECTING = 1;
    public static final int STAGE_CONNECTING_TO_SERVER = 2;
    public static final int STAGE_RELAYING = 3;


    public byte domainNameLength;
    public String domainName;
    public byte[] domainNameBytes;
    public InetAddress ipAddress;

    public short port;

    public typeConnection typeConnection;

    public int stage = STAGE_INITIAL;
    public SocketChannel clientChannel;
    public SocketChannel targetChannel;
}
