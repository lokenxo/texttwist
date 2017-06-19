package com.texttwist.server.components;

import com.texttwist.server.tasks.CheckOnlineUsers;
import com.texttwist.server.tasks.SendInvitations;
import models.Message;

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

        System.out.println(request.token);
        System.out.println(request.sender);
        System.out.println(request.message);
        System.out.println(request.data);



        if(isValidToken(request.token)){
            switch(request.message){
                case "START_GAME":
                    Future<Boolean> onlineUsers = threadPool.submit(new CheckOnlineUsers(request.data));
                    Boolean res = null;
                    try {
                        res = onlineUsers.get();
                        SessionsManager.getInstance().printSessions();
                        if(res){
                            Future<Boolean> sendInvitations = threadPool.submit(new SendInvitations(request.sender, request.data));
                            try {
                                res = sendInvitations.get();
                                System.out.println(res);
                                if (res) {
                                    System.out.println("SJSJSJSJSJ");

                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                default:
                    break;
            }

        } else {
            System.out.print("TOKEN NON VALIDO");
            //RISPONDI ERRORE TOKEN NON VALIDO
        }
    }
}
