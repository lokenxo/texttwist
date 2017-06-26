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
    public DefaultListModel<String> letters;
    public DefaultListModel<Pair<String,Integer>> playersScore = new DefaultListModel<Pair<String, Integer>>();

    public Match(String matchCreator, DefaultListModel<String> players){
        for (int i =0; i < players.size(); i++){
            this.playersStatus.addElement(new Pair<>(players.get(i), 0));
            this.playersSocket.addElement(new Pair<>(players.get(i), null));
        }

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

    public void setLetters(DefaultListModel<String> letters){
        this.letters = letters;
    }
}
