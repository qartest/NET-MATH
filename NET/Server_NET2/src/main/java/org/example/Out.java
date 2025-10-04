package org.example;

import java.util.Iterator;
import java.util.Map;

public class Out implements Runnable{

    private Map<ClientReader, MyPair> myMap;
    public Out(Handler my){
        myMap = my.GiveMyClients();
    }


    @Override
    public void run() {

        while (UI.Constants.live){
            try {

                Thread.sleep(3000);

                synchronized (myMap){
                    Iterator<Map.Entry<ClientReader, MyPair>> iterator = myMap.entrySet().iterator();
                    System.out.println("Всего клиентов: " + myMap.size());
                    while(iterator.hasNext()){
                        Map.Entry<ClientReader, MyPair> entry = iterator.next();


                        ClientReader client = entry.getKey();
                        long timeBegin = client.timeBegin;
                        long time = System.currentTimeMillis();
                        long data = client.nowFileSIze;


                        if(client.live){
                            entry.setValue(new MyPair(data, time));
                            System.out.println(client.toString() + " Средняя скорость: " + (double)(data / (time - timeBegin) * 1000) + "b/sec Мгновенная скорость: " + (double)((data - entry.getValue().GetSize()) / (time - entry.getValue().getTime()) * 1000) + " b/sec");
                        }
                        else{
                            long timeEnd = client.timeEnd;
//                            System.out.println("Data: " + data);
//                            System.out.println("TimeBegin: " + timeBegin);
//
//                            System.out.println("data in map" + entry.getValue().GetSize());
//                            System.out.println("time in map" + entry.getValue().getTime());
//                            System.out.println();

                            System.out.println(client.toString() + " Средняя скорость: " + (double)(data / (timeEnd - timeBegin) * 1000) + "b/sec Мгновенная скорость: " + (double)((data - entry.getValue().GetSize()) / (timeEnd - entry.getValue().getTime()) * 1000) + " b/sec");
                            iterator.remove();
                            System.out.println("Я удалил файл");
                        }
                    }
                }

            } catch (InterruptedException e) {
                System.out.println("Что-то не так со сном");
            }

        }

    }
}
