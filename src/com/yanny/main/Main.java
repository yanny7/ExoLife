package com.yanny.main;

import com.yanny.interfaces.ServerInterface;
import com.yanny.server.Server;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ServerInterface server = new Server();
        server.start();

        Thread.sleep(10000);

        server.stop();
    }
}
