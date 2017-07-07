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
        System.out.println("Match started, countdown for join!");

    }

    @Override
    public Boolean call() throws Exception {
        try {
            Thread.currentThread().sleep(1*5*1000);

            System.out.println("TIMEOUT - MANDA MESSAGGIO ERRORE A TUTTI GLI UTENTI DEL MATCH");
            if(match.joinTimeout) {
                return false;
            }
            else {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
