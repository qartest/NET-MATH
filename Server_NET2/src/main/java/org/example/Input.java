package org.example;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;

public class Input implements Runnable{

    public void InputData(String[] args) throws BadAddr, SocketException, UnknownHostException, ArrayIndexOutOfBoundsException {


//        InetAddress address = InetAddress.getByName(args[0]);
        UI.Constants.PORT = Integer.parseInt(args[0]);

//        boolean haveAddr = false;
//
//        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//        while (interfaces.hasMoreElements() && !haveAddr) {
//            NetworkInterface networkInterface = interfaces.nextElement();
//            if (networkInterface.isLoopback() || !networkInterface.isUp() || !networkInterface.supportsMulticast()) {
//                continue;
//            }
//
//            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
//            while (addresses.hasMoreElements()) {
//                InetAddress addr = addresses.nextElement();
//
//                if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress() && addr.equals(address)) {
//                    UI.Constants.FinalInet = addr;
//                    haveAddr = true;
//                }
//            }
//        }
//        if(!haveAddr){
//            throw new BadAddr();
//        }

    }

    public void CreateSaveDir() throws IOException{
        Files.createDirectories(UI.Constants.saveDir);

    }
    @Override
    public void run() {

    }
}
