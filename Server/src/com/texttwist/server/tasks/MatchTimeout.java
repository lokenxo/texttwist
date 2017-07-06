package com.texttwist.server.tasks;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.texttwist.server.models.Match;

import java.util.concurrent.Callable;

/**
 * Created by loke on 27/06/2017.
 */
public class MatchTimeout implements Callable<Boolean> {


    public MatchTimeout() {
        System.out.println("GamePage started, countdown for end words!");

    }

    @Override
    public Boolean call() throws Exception {
        try {
            Thread.currentThread().sleep(3*60*1000); //TODO 5*60*1000
            System.out.println("timer scaduto");
            System.out.println("TIMEOUT - SETTA A 0 il punteggio degli utenti che non hanno inviato le parole");
            return false;
        } catch (InterruptedException e) {
            System.out.println("TIMER BLOCATO PRIMA");
            return true;
        }
    }

}

