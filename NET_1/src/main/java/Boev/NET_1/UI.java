package Boev.NET_1;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Enumeration;

public class UI {
    public static class Constants{
        public static final int portMulticasting = 7778;
        public static final int pauseSec = 2000;
        public static InetAddress myAddress;
        public static long PID = ProcessHandle.current().pid();
        public static byte[] messageLive = new byte[13];
        public static byte[] messageDie = new byte[13];

        public static final int numberOfIterations = 5;

        public volatile static boolean live = true;
//        static {
//            try {

//                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//                while (interfaces.hasMoreElements()) {
//                    NetworkInterface networkInterface = interfaces.nextElement();
//
//
//                    if (networkInterface.isLoopback() || !networkInterface.isUp()) {
//                        continue;
//                    }
//
//                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
//                    while (addresses.hasMoreElements()) {
//                        InetAddress addr = addresses.nextElement();
//
//
//                        if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
//                            myAddress = addr;
////                        System.out.println("Локальный IP-адрес: " + addr.getHostAddress());
//                        }
//                    }
//
//                    ByteBuffer buffer = ByteBuffer.wrap(messageLive);
//                    buffer.position(0);
//                    buffer.put(UI.Constants.myAddress.getAddress(), 0, 4);
//                    buffer.putLong(PID);
//                    buffer.put((byte)1);
//
//                    System.arraycopy(messageLive, 0, messageDie, 0, 12);
//                    messageDie[12] = (byte)0;
//
//                }
//            } catch (SocketException e) {
//                e.printStackTrace();
//                throw new RuntimeException("Не удалось получить локальный IP-адрес", e);
//            }
//        }
    }

}
