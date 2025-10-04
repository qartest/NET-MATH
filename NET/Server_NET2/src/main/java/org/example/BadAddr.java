package org.example;

import java.io.IOException;

public class BadAddr extends IOException {
    public BadAddr(String message) {
        super(message);
    }
    public BadAddr(){
        super();
    }
}
