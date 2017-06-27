package com.texttwist.server.models;

import javafx.util.Pair;

import javax.swing.*;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.*;

import static com.texttwist.server.components.GameServer.activeMatches;

/**
 * Created by loke on 23/06/2017.
 */
public class Match {
    public DefaultListModel<Pair<String,Integer>> playersStatus = new DefaultListModel<Pair<String, Integer>>();
    public DefaultListModel<Pair<String,SocketChannel>> playersSocket = new DefaultListModel<Pair<String, SocketChannel>>();
    private boolean started = false;
    public String matchCreator;
    public Integer multicastId;
    public DefaultListModel<String> letters;
    public DefaultListModel<Pair<String,Integer>> playersScore = new DefaultListModel<Pair<String, Integer>>();

    public Match(String matchCreator, DefaultListModel<String> players){
        for (int i =0; i < players.size(); i++){
            this.playersStatus.addElement(new Pair<>(players.get(i), 0));
            this.playersScore.addElement(new Pair<>(players.get(i), -1));
            this.playersSocket.addElement(new Pair<>(players.get(i), null));
        }
        this.multicastId = this.generateMulticastId();
        this.matchCreator = matchCreator;
    }

    public static Match findMatch(String matchName){
        for(int i = 0; i<activeMatches.size(); i++) {
            if (activeMatches.get(i).matchCreator.equals(matchName)) {
                return activeMatches.get(i);
            }
        }
        return null;
    }


    public boolean isStarted(){
        return started;
    }

    public void startGame(){
        this.started=true;
    }

    public void setScore(String player, Integer score){
        for(int i = 0; i<playersScore.size(); i++) {
            if (playersScore.get(i).getKey().equals(player)) {
                playersScore.set(i, new Pair<String, Integer>(player, score));
            }
        }
    }

    public Boolean allPlayersSendedHisScore(){
        for(int i = 0; i<playersScore.size(); i++) {
            if (playersScore.get(i).getValue() == -1) {
                return false;
            }
        }
        return true;
    }

    public void setUndefinedScorePlayersToZero(){
        for(int i = 0; i<playersScore.size(); i++) {
            if (playersScore.get(i).getValue() == -1) {
                playersScore.set(i, new Pair<String, Integer>(playersScore.get(i).getKey(), 0));
            }
        }
    }

    public DefaultListModel<String> getMatchPlayersScoreAsStringList(){
        DefaultListModel<String> l = new DefaultListModel<>();
        for(int i = 0; i<playersScore.size(); i++) {
            l.addElement(playersScore.get(i).getKey()+":"+playersScore.get(i).getValue());
        }
        return l;
    }


    private int generateMulticastId(){
        if(activeMatches.size() != 0) {
            return activeMatches.lastElement().multicastId+1;
        } else {
            return 4000;
        }
    }
    public void setLetters(DefaultListModel<String> letters){
        this.letters = letters;
    }
}
