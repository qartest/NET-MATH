package org.example;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ClientReader implements Runnable, AutoCloseable{
     private Socket socket;
     private DataInputStream input;
     private FileOutputStream out;
     public volatile long nowFileSIze = 0;
     private byte[] buffer = new byte[4096];

     public boolean live = true;
     public long timeBegin = System.currentTimeMillis();
     public long timeEnd;

     public ClientReader(Socket socket) throws IOException {
         this.socket = socket;
         this.input = new DataInputStream(socket.getInputStream());
     }

    @Override
    public void run() {
         byte[] sizeBuffer = new byte[4];
         byte[] longBuffer = new byte[8];

        try {
            timeBegin = System.currentTimeMillis();
            input.readFully(sizeBuffer);
            int nameSize = ByteBuffer.wrap(sizeBuffer).getInt();

            input.readFully(buffer);
            String name = new String(buffer, 0, nameSize, StandardCharsets.UTF_8);
            out = new FileOutputStream((UI.Constants.saveDir.resolve(name).toString()));

            input.readFully(longBuffer);
            long fileSizeCl = ByteBuffer.wrap(longBuffer).getLong();


            int bytesRead;
            while((bytesRead = input.read(buffer)) != -1){
                out.write(buffer, 0, bytesRead);
                nowFileSIze += bytesRead;
                if(nowFileSIze > fileSizeCl){
                    break;
                }
            }

            byte[] answer = new byte[1];
            if(nowFileSIze == fileSizeCl){
                answer[0] = 1;
            }else {
                answer[0] = 0;
            }
            socket.getOutputStream().write(answer);
            live = false;
            timeEnd = System.currentTimeMillis();
            
        }catch (IOException e) {
            System.out.println("Что-то пошло не поплану");
            live = false;
        }
    }

    @Override
    public void close() throws Exception {
        if(socket != null) socket.close();
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        ClientReader that = (ClientReader) o;
        return Objects.equals(socket, that.socket);
    }

    @Override
    public int hashCode(){
        return Objects.hash(socket);
    }

    @Override
    public String toString(){
        return socket.toString();
    }
}

class MyPair{
    private long lastFileSize;
    private long lastTime;

    MyPair(long lastFileSize, long lastTime){
        this.lastFileSize = lastFileSize;
        this.lastTime = lastTime;
    }

    public long GetSize(){
        return lastFileSize;
    }

    public long getTime(){
        return lastTime;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        MyPair that = (MyPair) o;
        return Objects.equals(lastFileSize, that.lastFileSize) && Objects.equals(lastTime, that.lastTime);
    }

    @Override
    public int hashCode(){
        return Objects.hash(lastFileSize, lastTime);
    }

    @Override
    public String toString(){
        return "Address = " + lastFileSize + " PID = " + lastTime;
    }
}
