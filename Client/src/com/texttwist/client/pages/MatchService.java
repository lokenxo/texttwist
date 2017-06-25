package com.texttwist.client.pages;

import com.texttwist.client.App;
import com.texttwist.client.tasks.InvitePlayers;
import com.texttwist.client.tasks.WaitForPlayers;
import com.texttwist.client.ui.TTDialog;
import constants.Config;
import models.Message;

import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

/**
 * Created by loke on 18/06/2017.
 */
public class MatchService {

    public DefaultListModel<String> pendingList = new DefaultListModel<String>();
    ByteBuffer buffer = ByteBuffer.allocate(1024);

    public DefaultListModel<String> words = new DefaultListModel<String>();

    SocketChannel clientSocket = null;

    public MatchService(){
        InetSocketAddress socketAddress = new InetSocketAddress(Config.GameServerURI, Config.GameServerPort);
        try {
            clientSocket = SocketChannel.open(socketAddress);
            clientSocket.configureBlocking(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void newMatch(String userName) {
        //Aggiungi alla lista di inviti
        try {
            this.addToPendingList(userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Visualizza popup
        new TTDialog("success", "New invitation from: " + userName + "!",
            new Callable() {
                @Override
                public Object call() throws Exception {
                    App.matchService.joinMatch(userName);
                    return null;
                }
            },
            new Callable() {
                @Override
                public Object call() throws Exception {
                    return new Menu(Page.window);
                }
            });
    }


    public DefaultListModel<String> waitForPlayers(){
        DefaultListModel<String> words = new DefaultListModel<>();
        SwingWorker worker = new WaitForPlayers(clientSocket, words);
        worker.execute();
        return null;
    }


    public void setWords(DefaultListModel<String> words){
        this.words = words;
    }

    public void joinMatch(String matchName) {
        //Svuota la lista dei match pendenti e joina il match selezionato
        this.pendingList.clear();
        try {
            //Invia tcp req a server per dirgli che sto joinando
            DefaultListModel<String> matchNames = new DefaultListModel<String>();
            matchNames.addElement(matchName);
            Message message = new Message("JOIN_GAME", App.sessionService.account.userName, App.sessionService.account.token, matchNames);

            byte[] byteMessage = new String(message.toString()).getBytes();
            buffer = ByteBuffer.wrap(byteMessage);
            clientSocket.write(buffer);

            new Game(Page.window);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Object play(DefaultListModel<String> userNames) throws IOException {
        SwingWorker worker = new InvitePlayers(userNames,clientSocket);
        worker.execute();
        return null;
    }

    public void addToPendingList(String username) throws IOException {
        pendingList.addElement(username);
    }
}
