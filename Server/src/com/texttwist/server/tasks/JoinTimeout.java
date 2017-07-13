package com.texttwist.server.tasks;
import com.texttwist.server.models.Match;
import java.util.concurrent.*;

/**
 * Created by loke on 23/06/2017.
 */
public class JoinTimeout implements Callable<Boolean> {

    public final Match match;

    public JoinTimeout(Match match) {
        this.match = match;

    }

    @Override
    public Boolean call() throws Exception {
        try {
            Thread.currentThread().sleep(1*60*1000);
            System.out.println("TIMEOUTTTT");

            if(match.joinTimeout) {
                return false;
            }
            else {
                match.joinTimeout=true;
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
