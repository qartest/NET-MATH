package Boev.NET_2;

import java.io.*;
import java.net.UnknownHostException;


public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.Start(args);
        }
        catch (Close e){
            System.out.println("Сервер закрыл соединение");
        }
        catch (FileNotFoundException e) {
            System.out.println("Филе не найдено");
        } catch (UnknownHostException e) {
            System.out.println("IP не найден...");
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("мало данных");
        } catch (IOException e) {
            System.out.println("Проблема с отрпавкой");
        }
    }

}