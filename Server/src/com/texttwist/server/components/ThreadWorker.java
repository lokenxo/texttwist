package com.texttwist.server.components;

import utilities.Logger;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import static java.lang.Thread.sleep;


public class ThreadWorker implements Runnable{

    protected Socket clientSocket = null;
    protected String task = null;

    public ThreadWorker(Socket clientSocket, String task) {
        this.clientSocket = clientSocket;
        this.task = task;
    }

    public void run() {
        Logger.write(Thread.currentThread().getName()+": Start task -> " + task);
        try {
            sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       /* InputStream input  = clientSocket.getInputStream();
        OutputStream output = clientSocket.getOutputStream();
        long time = System.currentTimeMillis();
        output.write(("HTTP/1.1 200 OK\n\nThreadWorker: " +
                this.serverText + " - " +
                time +
                "").getBytes());
        output.close();
        input.close();*/
        Logger.write(Thread.currentThread().getName()+": End of task -> " + task);
    }
}