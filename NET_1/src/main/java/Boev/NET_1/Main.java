package Boev.NET_1;

import java.io.IOException;
import java.net.*;


public class Main {
    public static void main(String[] args) {

        try{
            BigStart bigStart = new BigStart();
            bigStart.run();

        } catch (BadAddr e){
            System.out.println("Проблема с вводимым адресом");
        } catch (IOException e){
            System.out.println("Не адрес");
        }
    }
}