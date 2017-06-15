package com.texttwist.server;

import com.texttwist.server.components.TTServer;
import utilities.TTLogger;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Server start");
        TTLogger logger = new TTLogger(new File("./log"));
        TTServer ttServer = new TTServer();
        ttServer.start();

    }
}
