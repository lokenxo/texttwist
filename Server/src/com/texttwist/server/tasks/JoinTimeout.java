package com.texttwist.server.tasks;
import com.texttwist.server.models.Match;
import constants.Config;

import java.util.concurrent.*;

/**
 * Author:      Lorenzo Iovino on 23/06/2017.
 * Description: Task: Join Timeout
 */
public class JoinTimeout implements Callable<Boolean> {

    public final Match match;

    public JoinTimeout(Match match) {
        this.match = match;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            match.joinTimeout = true;
            Thread.currentThread().sleep(Config.joinMatchTimeout);

            if(match.joinTimeout) {
                match.joinTimeout = false;
                return true;
            }
            else {
                match.joinTimeout = false;
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
