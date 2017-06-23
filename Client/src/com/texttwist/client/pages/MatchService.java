package com.texttwist.client.pages;

import com.texttwist.client.App;
import com.texttwist.client.ui.TTDialog;
import constants.Config;
import models.Message;
import models.Response;
import org.json.simple.JsonObject;

import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * Created by loke on 18/06/2017.
 */
public class MatchService {

    public DefaultListModel<String> pendingList = new DefaultListModel<String>();
    ByteBuffer buffer = ByteBuffer.allocate(1024);
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

        Message message = new Message("START_GAME", App.sessionService.account.userName, App.sessionService.account.token, userNames);

        byte[] byteMessage = new String(message.toString()).getBytes();
        buffer = ByteBuffer.wrap(byteMessage);
        clientSocket.write(buffer);

        while (true) {
            if (clientSocket.read(buffer) != -1) {
                buffer.clear();

                String line = new String(buffer.array(), buffer.position(), buffer.remaining());
                if (line.startsWith("MESSAGE")) {

                    Message msg = Message.toMessage(line);
                    if (msg.message.equals("USER_NOT_ONLINE")) {
                        new TTDialog("alert", "Users not online!",
                                new Callable() {
                                    @Override
                                    public Object call() throws Exception {
                                        return null;
                                    }
                                }, null);
                        return null;
                    }

                    if (msg.message.equals("INVITES_ALL_SENDED")) {
                       // clientSocket.close();
                        new TTDialog("success", "Invite all sended!",
                                new Callable() {
                                    @Override
                                    public Object call() throws Exception {
                                        //In attesa dei giocatori
                                        return new Game(Page.window);
                                    }
                                }, null);
                        return null;

                    }

                    if (msg.message.equals("TIMEOUT")) {
                        clientSocket.close();
                        new TTDialog("alert", "TIMEOUT!",
                                new Callable() {
                                    @Override
                                    public Object call() throws Exception {
                                        return new Menu(Page.window);

                                    }
                                }, null);
                        return null;
                    }
                }

            }
        }
    }

    public void addToPendingList(String username) throws IOException {
        pendingList.addElement(username);
    }
}
