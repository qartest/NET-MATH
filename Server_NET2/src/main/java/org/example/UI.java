package org.example;

import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UI {
    static public class Constants{
        public volatile static boolean live = true;
        public static int PORT = 0;
        public static int MaxQueue = 50;

        public static String nameDir = "uploads";
        public static Path saveDir = Paths.get(System.getProperty("user.dir"), nameDir);


    }
}
