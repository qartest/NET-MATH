package Boev.NET_1;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class BigStart implements Runnable{
    private Input input;
    private InetAddress address;
    private Handler handler;
    private Agent agent;

    private ArrayList<Thread> myThreads= new ArrayList<>();

    public BigStart() throws BadAddr, IOException{
        try {
            input = new Input();
            address = input.giveInet();
            input.GiveLocalInet();
            agent = new Agent(address);
            handler = new Handler();

            myThreads.add(new Thread(new Hear(address, handler)));
            myThreads.add(new Thread(handler));
            myThreads.add(new Thread(new Out(handler)));
            myThreads.add(new Thread(agent));
            myThreads.add(new Thread(input));

        } finally {

        }
    }

    @Override
    public void run() {
        for(Thread t : myThreads){
            t.start();
        }
    }
}
