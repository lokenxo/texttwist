package com.texttwist.server.models;

import com.texttwist.server.services.MessageService;
import com.texttwist.server.tasks.TimeoutMatch;
import javafx.util.Pair;

import javax.swing.*;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Author:      Lorenzo Iovino on 23/06/2017.
 * Description: Match Model. Methods for manage the matches and model of single match.
 *              Single point of concurrent access.
 */
public class Match {

    /****GLOBAL AREA OF ALL MATCHES****/
    //Players status: A list of pair where elements are <playerName, status>.
    //               status is 0 if user is not currently in a match, and 1 otherwise
    public final List<Pair<String,Integer>> playersStatus =  Collections.synchronizedList(new ArrayList<>());

    //Players socket: A list of pair where elements are <playerName, socketChannel>.
    //               socketChannel is the TCP socket associated with client for messages exchange
    public final List<Pair<String,SocketChannel>> playersSocket =  Collections.synchronizedList(new ArrayList<>());

    //Players score: A list of pair where elements are <playerName, score>.
    public final List<Pair<String,Integer>> playersScore =  Collections.synchronizedList(new ArrayList<>());

    //Players score: A list of active matches.
    public static List<Match> activeMatches =  Collections.synchronizedList(new ArrayList<>());


    /****SINGLE INSTANCE OF MATCH****/
    //If match is started
    private boolean started = false;

    //Name of the creator of the match
    public final String matchCreator;

    //MulticastID associated with this match
    public Integer multicastId;

    //True if happen timeout, false otherwise
    public boolean matchTimeout = false;
    public boolean joinTimeout = false;

    //Letters of the match
    public DefaultListModel<String> letters;

    private ExecutorService matchTimeoutThread = Executors.newSingleThreadExecutor();

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
        for (Match match : matches) {
            if (match.matchCreator.equals(matchName)) {
                return match;
            }
        }
        return null;
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

    public static Match findMatchByPlayerName(String player){
        for (Match activeMatch : activeMatches) {
            for (int j = 0; j < activeMatch.playersStatus.size(); j++) {
                if (activeMatch.playersStatus.get(j).getKey().equals(player)) {
                    return activeMatch;
                }
            }
        }
        return null;
    }

    public void startGame(){
        this.started = true;
        matchTimeoutThread.submit(new TimeoutMatch(this));
    }

    public void setScore(String player, Integer score){
        Match m = findMatchByPlayerName(player);
        if(m!=null) {
            for (int i = 0; i < m.playersScore.size(); i++) {
                if (m.playersScore.get(i).getKey().equals(player)) {
                    m.playersScore.set(i, new Pair<>(player, score));
                }
            }
        }
    }

    public Boolean allPlayersSendedHisScore(){
        for (Pair<String, Integer> player : playersScore) {
            if (player.getValue() == -1) {
                return false;
            }
        }
        return true;
    }

    public void setUndefinedScorePlayersToZero(){
        for (int i = 0; i < playersScore.size(); i++) {
            if (playersScore.get(i).getValue() == -1) {
                playersScore.set(i, new Pair<>(playersScore.get(i).getKey(), 0));
            }
        }
    }

    public DefaultListModel<String> getMatchPlayersScoreAsStringList(){
        DefaultListModel<String> l = new DefaultListModel<>();
        for (Pair<String, Integer> player : playersScore) {
            l.addElement(player.getKey() + ":" + player.getValue());
        }
        return l;
    }

    private int generateMulticastId(){
         return MessageService.multicastId++;
    }

    public void setLetters(DefaultListModel<String> letters){
        this.letters = letters;
    }
}
