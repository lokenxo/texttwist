package com.texttwist.server.models;

import constants.Config;
import javafx.util.Pair;

import javax.swing.*;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.*;

import static com.texttwist.server.components.GameServer.activeMatches;

/**
 * Created by loke on 23/06/2017.
 */
public class Match {
    public List<Pair<String,Integer>> playersStatus =  Collections.synchronizedList(new ArrayList<>()); //Usare Liste!!!!!!!
    public List<Pair<String,SocketChannel>> playersSocket =  Collections.synchronizedList(new ArrayList<>());
    private boolean started = false;
    public String matchCreator;
    public Integer multicastId;
    public DefaultListModel<String> letters;
    public List<Pair<String,Integer>> playersScore =  Collections.synchronizedList(new ArrayList<>());

    public Match(String matchCreator, DefaultListModel<String> players){
        for (int i =0; i < players.size(); i++){
            this.playersStatus.add(new Pair<>(players.get(i), 0));
            this.playersScore.add(new Pair<>(players.get(i), -1));
            this.playersSocket.add(new Pair<>(players.get(i), null));
        }
        this.multicastId = this.generateMulticastId();
        this.matchCreator = matchCreator;
    }

    public static Match findMatch(List<Match> matches, String matchName){
        synchronized (matches) {
            for (int i = 0; i < matches.size(); i++) {
                if (matches.get(i).matchCreator.equals(matchName)) {
                    return matches.get(i);
                }
            }
            return null;
        }
    }


    public void printAll(){
        for (int i = 0; i < playersScore.size(); i++) {
                System.out.println(playersScore.get(i).getKey() + " : " +playersScore.get(i).getValue());

        }
    }
    public static int findMatchIndex(List<Match> matches, String matchName){
        synchronized (matches) {
            for (int i = 0; i < matches.size(); i++) {
                if (matches.get(i).matchCreator.equals(matchName)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public boolean isStarted(){
        return started;
    }

    public void startGame(){
        this.started=true;
    }

    public void setScore(String player, Integer score){
        System.out.println(player + " prova a settare il suo score a " + score);
        synchronized (playersScore) {
            for (int i = 0; i < playersScore.size(); i++) {
                if (playersScore.get(i).getKey().equals(player)) {
                    playersScore.set(i, new Pair<String, Integer>(player, score));
                    System.out.println("SEtting score of " + playersScore.get(i).getKey() + " to " + score);
                }
            }
        }
    }

    public Boolean allPlayersSendedHisScore(){
        printAll();
        synchronized (playersScore) {
            for (int i = 0; i < playersScore.size(); i++) {
                if (playersScore.get(i).getValue() == -1) {
                    return false;
                }
            }
            return true;
        }
    }

    public void setUndefinedScorePlayersToZero(){
        synchronized (playersScore) {
            for (int i = 0; i < playersScore.size(); i++) {
                if (playersScore.get(i).getValue() == -1) {
                    playersScore.set(i, new Pair<String, Integer>(playersScore.get(i).getKey(), 0));
                }
            }
        }
    }

    public DefaultListModel<String> getMatchPlayersScoreAsStringList(){
        synchronized (playersScore) {
            DefaultListModel<String> l = new DefaultListModel<>();
            for (int i = 0; i < playersScore.size(); i++) {
                l.addElement(playersScore.get(i).getKey() + ":" + playersScore.get(i).getValue());
            }
            return l;
        }
    }


    private int generateMulticastId(){
        synchronized (playersScore) {
            if (activeMatches.size() != 0) {
                return activeMatches.get(activeMatches.size()).multicastId + 1;
            } else {
                return 4000;
            }
        }
    }
    public void setLetters(DefaultListModel<String> letters){
        this.letters = letters;
    }
}
