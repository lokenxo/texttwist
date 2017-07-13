package com.texttwist.client.services;

import com.texttwist.client.App;
import com.texttwist.client.pages.GamePage;
import com.texttwist.client.pages.MenuPage;
import com.texttwist.client.pages.Page;
import com.texttwist.client.tasks.InvitePlayers;
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
    private Boolean gameIsStarted = false;

    private void addToPendingList(String username) throws IOException {
        pendingList.addElement(username);
    }

    public GameService(){
        App.openClientTCPSocket();
    }

    public void newMatch(String userName) {
        //Add to pending invites list
        try {
            this.addToPendingList(userName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!App.gameService.gameIsStarted) {
            //Show invite popup
            new TTDialog("success", "New invite from: " + userName + "!",
                new Callable() {
                    @Override
                    public Object call() throws Exception {
                        App.gameService.joinMatch(userName);
                        return null;
                    }
                },
                new Callable() {
                    @Override
                    public Object call() throws Exception {
                        return new MenuPage(Page.window);
                    }
                });
        }
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

    public void joinMatch(String matchName) {

        //Clear pending invitation list and join selected match
        if(!gameIsStarted) {
            this.pendingList.clear();
            try {
                DefaultListModel<String> matchNames = new DefaultListModel<>();
                matchNames.addElement(matchName);
                Message message = new Message("JOIN_GAME", App.session.account.userName, App.session.token, matchNames);

                byte[] byteMessage = message.toString().getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(byteMessage);
                App.clientTCP.write(buffer);
                new GamePage(Page.window);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Start game and wait for other players
    public void beginMatch(DefaultListModel<String> userNames) {
        new InvitePlayers(userNames).execute();
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
