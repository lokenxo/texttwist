package com.texttwist.server.models;

import com.texttwist.server.components.GameServer;
import com.texttwist.server.tasks.MatchTimeout;
import constants.Config;
import javafx.util.Pair;

import javax.swing.*;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.texttwist.server.components.GameServer.activeMatches;

/**
 * Created by loke on 23/06/2017.
 */
public class Match {
    public final List<Pair<String,Integer>> playersStatus =  Collections.synchronizedList(new ArrayList<>()); //Usare Liste!!!!!!!
    public final List<Pair<String,SocketChannel>> playersSocket =  Collections.synchronizedList(new ArrayList<>());
    private boolean started = false;
    public final String matchCreator;
    public Integer multicastId;
    public Future<Boolean> timeout;
    public Future<Boolean> matchTimeout;
    public boolean joinTimeout =true;
    public DefaultListModel<String> letters;
    protected ExecutorService threadPool = Executors.newSingleThreadExecutor();

    public final List<Pair<String,Integer>> playersScore =  Collections.synchronizedList(new ArrayList<>());

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
            for (int i = 0; i < matches.size(); i++) {
                if (matches.get(i).matchCreator.equals(matchName)) {
                    return i;
                }
            }
            return -1;
    }

    public boolean isStarted(){
        return started;
    }

    public static Match findMatchByPlayer(String player){
        for (int i = 0; i < activeMatches.size(); i++) {
            for (int j = 0; j < activeMatches.get(i).playersStatus.size(); j++) {
                if (activeMatches.get(i).playersStatus.get(j).getKey().equals(player)) {
                    return activeMatches.get(i);
                }
            }
           /* if (matches.get(i).matchCreator.equals(matchName)) {
                return i;
            }*/
        }
        return null;
    }

    public void startGame(){
        this.started=true;
        this.matchTimeout = threadPool.submit(new MatchTimeout());

    }

    public void setScore(String player, Integer score){
        Match m = findMatchByPlayer(player);
        synchronized (m) {
            m.printAll();

            for (int i = 0; i < m.playersScore.size(); i++) {
                if (m.playersScore.get(i).getKey().equals(player)) {
                    m.playersScore.set(i, new Pair<String, Integer>(player, score));
                }
            }
        }
    }

    public Boolean allPlayersSendedHisScore(){
        System.out.println(matchCreator);
        printAll();
            for (int i = 0; i < playersScore.size(); i++) {
                if (playersScore.get(i).getValue() == -1) {
                    return false;
                }
            }
            return true;
    }

    public void setUndefinedScorePlayersToZero(){
            for (int i = 0; i < playersScore.size(); i++) {
                if (playersScore.get(i).getValue() == -1) {
                    playersScore.set(i, new Pair<String, Integer>(playersScore.get(i).getKey(), 0));
                }
            }
    }

    public DefaultListModel<String> getMatchPlayersScoreAsStringList(){
            DefaultListModel<String> l = new DefaultListModel<>();
            for (int i = 0; i < playersScore.size(); i++) {
                l.addElement(playersScore.get(i).getKey() + ":" + playersScore.get(i).getValue());
            }
            return l;

    }


    private int generateMulticastId(){
         return GameServer.multicastID++;
    }
    public void setLetters(DefaultListModel<String> letters){
        this.letters = letters;
    }
}
