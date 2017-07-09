package com.texttwist.server.tasks;
import com.texttwist.client.App;
import com.texttwist.server.components.AccountsManager;
import com.texttwist.server.models.Dictionary;
import com.texttwist.server.models.Match;
import constants.Config;
import models.Message;
import models.User;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.Callable;

import static com.texttwist.server.components.GameServer.activeMatches;

/**
 * Created by loke on 27/06/2017.
 */
public class ComputeScore implements Callable<Integer> {

    public DefaultListModel<String> words;
    public final String sender;
    public Match match;

    public ComputeScore(String sender, DefaultListModel<String> words, Match match){
        this.words = words;
        this.match = match;
        this.sender = sender;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("COMPUTE SCORE STARTED");
            System.out.println(match);
        System.out.println("COMPUTE SCORE STAsssssRTED");

        synchronized (match) {
            System.out.print("CALCOLO LO SCORE PER " + match.matchCreator);
            Integer score = 0;

            for (int i = 0; i < words.size(); i++) {
                if (isValid(words.get(i), match.letters)) {
                    score += words.get(i).length();
                }
            }

            System.out.println("SOODDISDIS");
            match.setScore(sender, score);

            User u = AccountsManager.getInstance().findUser(sender);
            u.addScore(score);

            if(match.allPlayersSendedHisScore()) {

                match.matchTimeout.cancel(true);
                //channel.close();
                //Start receive words: tempo masimo 5 minuti per completare l'invio delle lettere.

                match.setUndefinedScorePlayersToZero();

                System.out.println("SEND BROADCAST");
                while (true) {
                    System.out.println("SENDING");
                    Message msg = new Message("FINALSCORE", "SERVER", "", match.getMatchPlayersScoreAsStringList());

                    MulticastSocket multicastSocket = new MulticastSocket(match.multicastId);
                    InetAddress ia = InetAddress.getByName(Config.ScoreMulticastServerURI);
                    DatagramPacket hi = new DatagramPacket(msg.toString().getBytes(), msg.toString().length(), ia, match.multicastId);
                    multicastSocket.send(hi);
                    activeMatches.remove(Match.findMatchIndex(activeMatches, match.matchCreator));
                    //multicastSocket.disconnect();
                    //multicastSocket.close();
                }
            }
            return score;

        }

    }

    private Boolean isValid(String word, DefaultListModel<String> letters) {
        for (int i =0 ; i< word.length(); i++){
            String c = Character.toString(word.charAt(i));
            Boolean isCharacterPresent = false;
            for(int j =0 ; j< letters.size(); j++){
                if(c.equals(letters.get(j))){
                    isCharacterPresent = true;
                }
            }
            if(word.equals("")){
                return true;
            }

            if(!isCharacterPresent){
                return false;
            }
        }
        return Dictionary.isContainedInDictionary(word);
    }

}
