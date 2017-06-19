package com.texttwist.server.components;

import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Created by loke on 18/06/2017.
 */
public class ThreadProxy implements Runnable {
    protected ExecutorService threadPool = Executors.newCachedThreadPool();
    private Message request;
    ThreadProxy(Message request){
        this.request = request;
    }


    private Boolean isValidToken(String token){
        return SessionsManager.getInstance().isValidToken(token);
    }

    public void run() {
        System.out.println("Selecting right task for new thread");

        if(isValidToken(request.token)){
            switch(request.message){
                case "START_GAME":
                    Future<Boolean> newTask = threadPool.submit(new CheckOnlineUsers(request.data));
                    Boolean returnedValue = null;
                    try {
                        returnedValue = newTask.get();
                        System.out.println(returnedValue);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    System.out.println(returnedValue);

                default:
                    break;
            }

        } else {
            System.out.print("TOKEN NON VALIDO");
            //RISPONDI ERRORE TOKEN NON VALIDO
        }
    }
}
