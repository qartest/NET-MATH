package Boev.NET_1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Agent implements AutoCloseable, Runnable {
    private DatagramSocket socket;
    private InetAddress addr;

    public Agent(InetAddress inet){
        addr = inet;
        try{
            socket = new DatagramSocket();
        } catch (SocketException e){
            System.out.println("ЧТО-ТО ПОШЛО НЕ ПОПЛАНУ");
            e.getMessage();
        }
    }

    public void sendLive() throws IOException {

        DatagramPacket packet = new DatagramPacket(UI.Constants.messageLive, UI.Constants.messageLive.length, addr, UI.Constants.portMulticasting);
        socket.send(packet);
    }

    public void sendDie() throws IOException{
        DatagramPacket packet = new DatagramPacket(UI.Constants.messageDie,UI.Constants.messageDie.length, addr, UI.Constants.portMulticasting);
        socket.send(packet);
    }

    @Override
    public void close() throws Exception {
        if(socket.isClosed()){
            socket.close();
        }
    }

    @Override
    public void run() {
        while (UI.Constants.live){
            try{
                sendLive();
                Thread.sleep(1000);
            }catch (IOException ignored){

            } catch (InterruptedException ignored) {

            }
        }
        try{
            sendDie();
        } catch (IOException e) {
            System.out.println("Не послал");
        }

    }
}
