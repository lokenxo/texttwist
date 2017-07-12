package com.texttwist.client.models;

import com.texttwist.client.App;
import com.texttwist.client.pages.GamePage;
import com.texttwist.client.pages.MenuPage;
import com.texttwist.client.pages.Page;
import com.texttwist.client.tasks.InvitePlayers;
import com.texttwist.client.ui.TTDialog;
import constants.Config;
import interfaces.INotificationClient;
import interfaces.INotificationServer;
import javafx.util.Pair;
import models.Message;

import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.*;

/**
 * Created by loke on 18/06/2017.
 */
public class Game {

    public Integer multicastId = 0 ;
    public DefaultListModel<String> pendingList = new DefaultListModel<String>();
    public DefaultListModel<String> words = new DefaultListModel<String>();
    public DefaultListModel<String> letters = new DefaultListModel<String>();
    public DefaultListModel<Pair<String,Integer>> globalRanks = new DefaultListModel<>();
    public DefaultListModel<Pair<String,Integer>> ranks = new DefaultListModel<>();
    public INotificationClient notificationStub;
    public MulticastSocket multicastSocket;
    public SocketChannel clientSocket;
    public INotificationServer notificationServer;
    public Boolean gameIsStarted = false;

    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    public Game(){

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(Config.NotificationServerStubPort);
            notificationServer = (INotificationServer) registry.lookup(Config.NotificationServerName);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        InetSocketAddress socketAddress = new InetSocketAddress(Config.GameServerURI, Config.GameServerPort);
        try {
            clientSocket = SocketChannel.open(socketAddress);
            clientSocket.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void newMatch(String userName) {
        //Add to pending invitation list
        try {
            this.addToPendingList(userName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!App.game.gameIsStarted) {
            //Show invitation popup
            new TTDialog("success", "New invitation from: " + userName + "!",
                new Callable() {
                    @Override
                    public Object call() throws Exception {
                        App.game.joinMatch(userName);
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
        return App.game.letters;
    }

    public DefaultListModel<String> getWords() {
        return App.game.words;
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
                buffer = ByteBuffer.wrap(byteMessage);
                clientSocket.write(buffer);
                new GamePage(Page.window);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Object play(DefaultListModel<String> userNames) throws IOException {
        SwingWorker worker = new InvitePlayers(userNames,clientSocket);
        worker.execute();
        return null;
    }

    public Void start(){
        App.game.gameIsStarted = true;
        return null;
    }

    public Void stop(){
        App.game.gameIsStarted = false;
        return null;
    }

    public void setMulticastId(Integer multicastId){
        this.multicastId = multicastId;
    }

    private void addToPendingList(String username) throws IOException {
        pendingList.addElement(username);
    }
}
