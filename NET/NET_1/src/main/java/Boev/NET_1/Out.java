package Boev.NET_1;

import java.util.Iterator;
import java.util.Map;

public class Out implements Runnable{

    private Map<MyPair, Integer> myMap;


    public Out(Handler handler){
        myMap = handler.getMyMap();
    }

    @Override
    public void run() {

        while (UI.Constants.live){

            synchronized (myMap){
                System.out.println("Обнаружено копий: " + myMap.size());

                Iterator <Map.Entry<MyPair, Integer>> iterator = myMap.entrySet().iterator();

                while(iterator.hasNext()){
                    Map.Entry<MyPair, Integer> entry = iterator.next();
                    int nowValue = entry.getValue();

                    System.out.println("Копия существует: " + entry.getKey().toString());
                    if(nowValue == 0){
                        iterator.remove();
                    }
                    else{

                        entry.setValue(nowValue - 1);
                    }
                }
            }

            try{
                Thread.sleep(UI.Constants.pauseSec);
            }catch ( InterruptedException ignored) {

            }

        }

    }

}
