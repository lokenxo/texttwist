package com.texttwist.server.tasks;

import com.texttwist.server.models.Match;
import constants.Config;
import models.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 13/07/2017.
 * Description: Task: Send Final Scores
 */
public class SendFinalScores implements Callable<Void> {

    private Match match;

    public SendFinalScores(Match match){
        this.match = match;
    }

    @Override
    public Void call() throws Exception {

        Message msg = new Message("FINALSCORE", "SERVER", "", match.getMatchPlayersScoreAsStringList());
        MulticastSocket multicastSocket = null;
        try {
            multicastSocket = new MulticastSocket(match.multicastId);
            InetAddress ia = InetAddress.getByName(Config.ScoreMulticastServiceURI);
            DatagramPacket hi = new DatagramPacket(msg.toString().getBytes(), msg.toString().length(), ia, match.multicastId);
            multicastSocket.send(hi);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Match.activeMatches.remove(Match.findMatchIndex(Match.activeMatches, match.matchCreator));
        return null;
    }
}
