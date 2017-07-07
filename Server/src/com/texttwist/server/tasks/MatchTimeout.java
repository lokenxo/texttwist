package com.texttwist.server.tasks;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.texttwist.server.models.Match;

import java.util.concurrent.Callable;

/**
 * Created by loke on 27/06/2017.
 */
public class MatchTimeout implements Callable<Boolean> {


    public MatchTimeout() {

    }

    @Override
    public Boolean call() throws Exception {
        try {
            Thread.currentThread().sleep(3*60*1000); //TODO 5*60*1000
            return false;
        } catch (InterruptedException e) {
            return true;
        }
    }

}

