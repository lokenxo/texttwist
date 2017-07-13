package com.texttwist.server.tasks;

import com.texttwist.server.models.Match;
import constants.Config;

import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 27/06/2017.
 * Description: Jedis Service
 */
public class MatchTimeout implements Callable<Boolean> {

    private Match match;
    public MatchTimeout(Match match) {
        this.match = match;

    }

    @Override
    public Boolean call() throws Exception {
        try {
            Thread.currentThread().sleep(Config.sendWordsTimeout);
            match.setUndefinedScorePlayersToZero();

            if(match.matchTimeout) {
                System.out.println("SEND BROADCAST BECAUSE TIMEOUT");
                new SendScores(match).call();
                return true;
            }
            return false;

        } catch (InterruptedException e) {
            return false;
        }
    }

}

