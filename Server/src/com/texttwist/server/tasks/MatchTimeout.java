package com.texttwist.server.tasks;

import com.texttwist.client.App;
import com.texttwist.server.models.Match;
import models.Message;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

/**
 * Created by loke on 23/06/2017.
 */
public class MatchTimeout implements Callable<Boolean> {

    public Match match;

    public MatchTimeout(Match match) {
        this.match = match;
        System.out.println("Math started, countdown for join!");

    }

    @Override
    public Boolean call() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            Thread.currentThread().sleep(7*60*1000);

            System.out.println("TIMEOUT - MANDA MESSAGGIO ERRORE A TUTTI GLI UTENTI DEL MATCH");
          return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
