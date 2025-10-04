package org.example;

import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Handler implements Runnable{

    private BlockingQueue<Socket> SocketQueue = new LinkedBlockingQueue<>();
    private Map<ClientReader, MyPair> myClients = Collections.synchronizedMap(new HashMap<>());

    public Handler(){

    }

    public BlockingQueue<Socket> GiveSocketQueue(){
        return SocketQueue;
    }

    public Map<ClientReader, MyPair> GiveMyClients(){
        return myClients;
    }

    @Override
    public void run() {
        int t = 0;
        while(UI.Constants.live){
            try{
                Socket socket = SocketQueue.take();
                ClientReader clientReader = new ClientReader(socket);
                new Thread(clientReader).start();
                synchronized (myClients){
                    myClients.put(clientReader, new MyPair(0, clientReader.timeBegin));
                }

            }catch (InterruptedException e) {
                System.out.println("Не взял");
            }catch (IOException e){
                System.out.println("Клиент ридер не создался");
            }
        }

    }
}
