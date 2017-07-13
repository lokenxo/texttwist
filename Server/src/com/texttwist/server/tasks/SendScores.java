package com.texttwist.server.tasks;

import com.texttwist.server.models.Match;
import constants.Config;
import models.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.Callable;

import static com.texttwist.server.services.MessageService.activeMatches;

/**
 * Created by loke on 13/07/2017.
 */
public class SendScores implements Callable<Void> {

    private Match match;

    public SendScores(Match match){
        this.match = match;
    }

    @Override
    public Void call() throws Exception {

        while (true) {
            System.out.println("SENDING");
            Message msg = new Message("FINALSCORE", "SERVER", "", match.getMatchPlayersScoreAsStringList());

            MulticastSocket multicastSocket = null;
            try {
                multicastSocket = new MulticastSocket(match.multicastId);
                InetAddress ia = InetAddress.getByName(Config.ScoreMulticastServerURI);
                DatagramPacket hi = new DatagramPacket(msg.toString().getBytes(), msg.toString().length(), ia, match.multicastId);
                multicastSocket.send(hi);
            } catch (IOException e) {
                e.printStackTrace();
            }
            activeMatches.remove(Match.findMatchIndex(activeMatches, match.matchCreator));
            return null;
        }
    }
}
