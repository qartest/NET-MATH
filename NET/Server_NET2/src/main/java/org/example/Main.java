package org.example;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        try{
            Input input = new Input();
            input.InputData(args);
            input.CreateSaveDir();


            Handler handler = new Handler();
            Hear hear = new Hear(handler);
            Out out = new Out(handler);

            new Thread(hear).start();
            new Thread(handler).start();
            new Thread(out).start();

        } catch (SocketException e) {
            System.out.println("Проблема с сокетами");
        } catch (UnknownHostException | BadAddr e) {
            System.out.println("Проблема с адресом");
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Мало данных");
        } catch (IOException e){
            System.out.println("Проблемы");
        }
    }
}