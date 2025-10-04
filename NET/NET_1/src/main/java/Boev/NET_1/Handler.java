package Boev.NET_1;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Handler implements Runnable{

    public BlockingQueue<byte[]> myQueue = new LinkedBlockingQueue<>();

    private Map<MyPair, Integer>  myMap = Collections.synchronizedMap(new HashMap<>());


    public Map<MyPair, Integer> getMyMap(){
        return myMap;
    }

    @Override
    public void run() {
        byte[] bufferAddr = new byte[4];
        while(UI.Constants.live){
            try{

                byte[] message = myQueue.take();
                if(!UI.Constants.live){
                    continue;
                }
                ByteBuffer help = ByteBuffer.wrap(message);

                System.arraycopy(message, 0,bufferAddr, 0, 4);
                InetAddress addr = InetAddress.getByAddress(bufferAddr);
                long PID = help.getLong(4);

                boolean live;

                if(message[12] == (byte)1){
                    live = true;
                } else {
                    live = false;
                }

                MyPair myMessage = new MyPair(addr, PID);
                synchronized (myMap){
                    if(live){
                        myMap.put(myMessage, UI.Constants.numberOfIterations);
                    }
                    else{
                        myMap.put(myMessage, 0);
                    }
                }

            } catch (InterruptedException e) {

            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


class MyPair{
    private final InetAddress addr;
    private final long PID;

    MyPair(InetAddress address, long PID){
        addr = address;
        this.PID = PID;
    }

    public InetAddress getAddr(){
        return addr;
    }

    public long getPID(){
        return PID;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        MyPair that = (MyPair) o;
        return Objects.equals(addr, that.addr) && Objects.equals(PID, that.PID);
    }

    @Override
    public int hashCode(){
        return Objects.hash(addr, PID);
    }

    @Override
    public String toString(){
        return "Address = " + addr + " PID = " + PID;
    }
}
