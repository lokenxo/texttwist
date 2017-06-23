package com.texttwist.server.models;

import javafx.util.Pair;

import javax.swing.*;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Created by loke on 23/06/2017.
 */
public class Match {
    public DefaultListModel<Pair<String,Integer>> playersStatus = new DefaultListModel<Pair<String, Integer>>();
    public DefaultListModel<Pair<String,SocketChannel>> playersSocket = new DefaultListModel<Pair<String, SocketChannel>>();

    public String matchCreator;
    public DefaultListModel<Pair<String,Integer>> playersScore = new DefaultListModel<Pair<String, Integer>>();

    public Match(String matchCreator, DefaultListModel<String> players){
        for (int i =0; i < players.size(); i++){
            this.playersStatus.addElement(new Pair<>(players.get(i), 0));
            this.playersSocket.addElement(new Pair<>(players.get(i), null));
        }

        this.matchCreator = matchCreator;
    }

}
