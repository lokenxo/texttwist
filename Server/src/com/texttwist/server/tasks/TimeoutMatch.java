package com.texttwist.server.tasks;

import com.texttwist.server.models.Match;
import constants.Config;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 27/06/2017.
 * Description: Task: Match Timeout
 */
public class TimeoutMatch implements Callable<Boolean> {

    private Match match;
    public TimeoutMatch(Match match) {
        this.match = match;

    }

    @Override
    public Boolean call() throws Exception {
        try {
            Thread.currentThread().sleep(Config.sendWordsTimeout);

            if(match.matchTimeout) {
                match.setUndefinedScorePlayersToZero();
                new SendScores(match).call();
                return true;
            }
            return false;

        } catch (InterruptedException e) {
            return false;
        }
    }

}

