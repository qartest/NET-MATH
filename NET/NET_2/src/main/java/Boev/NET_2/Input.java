//package Boev.NET_2;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.nio.ByteBuffer;
//import java.util.Enumeration;
//
//public class Input implements Runnable{
//
//    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//
//    public InetAddress giveInet() throws IOException {
//        String input;
//        InetAddress address;
//
//        while((input = reader.readLine()).isEmpty()){
//            System.out.println("Введи что-нибудь, плохой человек");
//        }
//
//        address = InetAddress.getByName(input);
//
//        if(address.isMulticastAddress()){
//            if(address instanceof java.net.Inet4Address){
//
//                byte[] byteAddress = address.getAddress();
//
//                if(!((byteAddress[0] & 0xFF) == 224 && (byteAddress[1] & 0xFF) == 0)){
//                    throw new BadAddr();
//                }
//
//            }else if(address instanceof java.net.Inet6Address){
//                byte[] byteAddress = address.getAddress();
//
//                if (!((byteAddress[0] & 0xFF) == 0xFF && (byteAddress[1] & 0x0F) == 0x02)) {
//                    throw new BadAddr();
//                }
//            }
//            else{
//                throw new BadAddr();
//            }
//        }
//        return address;
//    }
//
//    public void GiveLocalInet() throws IOException{
//        String input;
//        InetAddress address;
//
//        while((input = reader.readLine()).isEmpty()){
//            System.out.println("Введи что-нибудь, плохой человек");
//        }
//
//        address = InetAddress.getByName(input);
//
//        try {
//            boolean haveAddr = false;
//            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//            while (interfaces.hasMoreElements() && !haveAddr) {
//                NetworkInterface networkInterface = interfaces.nextElement();
//
//
//                if (networkInterface.isLoopback() || !networkInterface.isUp() || !networkInterface.supportsMulticast()) {
//                    continue;
//                }
//
//                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
//                while (addresses.hasMoreElements()) {
//                    InetAddress addr = addresses.nextElement();
//
//
//                    if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress() && addr.equals(address)) {
//                        UI.Constants.myAddress = addr;
//                        haveAddr = true;
////                        System.out.println("Локальный IP-адрес: " + addr.getHostAddress());
//                    }
//                }
//            }
//            if(!haveAddr){
//                throw new BadAddr();
//            }
//            ByteBuffer buffer = ByteBuffer.wrap(UI.Constants.messageLive);
//            buffer.position(0);
//            buffer.put(UI.Constants.myAddress.getAddress(), 0, 4);
//            buffer.putLong(UI.Constants.PID);
//            buffer.put((byte)1);
//
//            System.arraycopy(UI.Constants.messageLive, 0, UI.Constants.messageDie, 0, 12);
//            UI.Constants.messageDie[12] = (byte)0;
//
//        } catch (SocketException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Не удалось получить локальный IP-адрес", e);
//        }
//
//    }
//
//    @Override
//    public void run() {
//        String input;
//        while (UI.Constants.live){
//            try{
//                input = reader.readLine();
//                if(input.equals("exit")){
//                    UI.Constants.live = false;
//                }
//                else{
//                    System.out.println("Замолчи");
//                }
//            }catch (IOException ignored) {
//
//            }
//
//        }
//    }
//}
