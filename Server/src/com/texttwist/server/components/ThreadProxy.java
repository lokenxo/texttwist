package com.texttwist.server.components;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Created by loke on 18/06/2017.
 */
public class ThreadProxy implements Runnable {
    protected ExecutorService threadPool = Executors.newCachedThreadPool();
    private String request;
    ThreadProxy(String request){
        this.request = request;
    }

    private Callable<Boolean> checkIfUsersAreOnline = new Callable<Boolean>(){
        String message = "Check If users are online!";
        @Override
        public Boolean call() throws Exception {
            for(int i = 0; i < 1; i++){
                Thread.sleep(2000);
            }
            return true;
        }
    };

    public void run() {
        System.out.println("Selecting right task for new thread");
       /* byte[] buffer = new byte[100];
        try {
            InputStream clientInputStream = clientSocket.getInputStream();
            while (clientInputStream.read(buffer) != -1) {
                request += buffer;
            }
            System.out.println(request);

        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        //Assegna un threadWorker al task in arrivo
        System.out.print(request);
        Future<Boolean> callableFuture = threadPool.submit(checkIfUsersAreOnline);

        try {
            // get() waits for the task to finish and then gets the result
            Boolean returnedValue = callableFuture.get();
            System.out.println(returnedValue);
        } catch (InterruptedException e) {
            // thrown if task was interrupted before completion
            e.printStackTrace();
        } catch (ExecutionException e) {
            // thrown if the task threw an execption while executing
            e.printStackTrace();
        }
    }
}
