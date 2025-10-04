package org.example;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Hear implements Runnable, Closeable{

    private Handler handler;
    private ServerSocket ServerSocket;

    public Hear(Handler handler) throws IOException{
        this.handler = handler;
        ServerSocket =  new ServerSocket(UI.Constants.PORT);
    }


    @Override
    public void run(){
        System.out.println("Жду подключения");
        while(UI.Constants.live) {
            try{

                Socket socket = ServerSocket.accept();
                handler.GiveSocketQueue().put(socket);


            } catch (IOException e) {
                System.out.println("Проблема с accept()");
            } catch (InterruptedException e) {
                System.out.println("НЕ положил сокет");
            }
        }


    }

    @Override
    public void close() throws IOException {
        if(ServerSocket != null) ServerSocket.close();
    }
}
