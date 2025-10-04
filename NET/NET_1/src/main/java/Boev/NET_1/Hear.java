package Boev.NET_1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class Hear implements Runnable, AutoCloseable{
    private MulticastSocket socket;
    private InetAddress addr;



    private Handler myHandler;

    public Hear(InetAddress address, Handler handler) throws IOException {
        socket = new MulticastSocket(UI.Constants.portMulticasting);
        addr = address;
        socket.joinGroup(addr);
        myHandler = handler;
    }

    @Override
    public void run() {

        byte[] buffer = new byte[13];
        while(UI.Constants.live){
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(packet);
                myHandler.myQueue.put(buffer);


            } catch (IOException e) {
                System.out.println("Плохо принимать пакеты");

            } catch (InterruptedException e) {
                System.out.println("Не отправил буфер");
            }

        }
    }

    @Override
    public void close() throws Exception {
        if(!socket.isClosed()){
            socket.close();
        }
    }
}
