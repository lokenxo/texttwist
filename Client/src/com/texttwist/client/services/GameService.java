package com.texttwist.client.services;

import com.texttwist.client.App;
import com.texttwist.client.pages.GamePage;
import com.texttwist.client.pages.MenuPage;
import com.texttwist.client.pages.Page;
import com.texttwist.client.tasks.InvitePlayers;
import com.texttwist.client.tasks.JoinMatch;
import com.texttwist.client.ui.TTDialog;
import javafx.util.Pair;
import models.Message;
import javax.swing.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.concurrent.*;

/**
 * Author:      Lorenzo Iovino on 18/06/2017.
 * Description: GameService.
 *              Provide the interface for the game.
 */
public class GameService {

    public Integer multicastId = 0 ;
    public DefaultListModel<String> pendingList = new DefaultListModel<>();
    public DefaultListModel<String> words = new DefaultListModel<>();
    public DefaultListModel<String> letters = new DefaultListModel<>();
    public DefaultListModel<Pair<String,Integer>> globalRanks = new DefaultListModel<>();
    public DefaultListModel<Pair<String,Integer>> ranks = new DefaultListModel<>();
    public Boolean gameIsStarted = false;
    public Boolean isWaiting = false;

    public void addToPendingList(String username) throws IOException {
        pendingList.addElement(username);
    }

    public GameService(){
        App.openClientTCPSocket();
    }

    public DefaultListModel<String> getLetters(){
        return App.gameService.letters;
    }

    public DefaultListModel<String> getWords() {
        return App.gameService.words;
    }

    public void setLetters(DefaultListModel<String> letters){
        this.letters = letters;
    }

    public void start(){
        gameIsStarted = true;
    }

    public void stop(){
        gameIsStarted = false;
    }

    public void setMulticastId(Integer multicastId){
        this.multicastId = multicastId;
    }
}
