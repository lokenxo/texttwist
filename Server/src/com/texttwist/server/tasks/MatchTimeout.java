package com.texttwist.server.tasks;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.texttwist.server.models.Match;
import constants.Config;
import models.Message;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.Callable;

import static com.texttwist.server.components.GameServer.activeMatches;

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
            Thread.currentThread().sleep(1*60*1000); //TODO 5*60*1000
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

