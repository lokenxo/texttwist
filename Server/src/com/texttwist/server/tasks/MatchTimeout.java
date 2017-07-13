package com.texttwist.server.tasks;

import com.texttwist.server.models.Match;

import java.util.concurrent.Callable;

/**
 * Created by loke on 27/06/2017.
 */
public class MatchTimeout implements Callable<Boolean> {

    private Match match;
    public MatchTimeout(Match match) {
        this.match = match;

    }

    @Override
    public Boolean call() throws Exception {
        try {
            Thread.currentThread().sleep(5*60*1000); //TODO 5*60*1000
            match.setUndefinedScorePlayersToZero();

            if(match.matchTimeout) {
                System.out.println("SEND BROADCAST BECAUSE TIMEOUT");
                match.sendScores();
                return true;
            }
            return false;

        } catch (InterruptedException e) {
            return false;
        }
    }

}

