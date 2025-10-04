package Boev.NET_2;

import java.io.IOException;

public class Close extends IOException {
    public Close(String message) {
        super(message);
    }
    public Close(){
        super();
    }
}
