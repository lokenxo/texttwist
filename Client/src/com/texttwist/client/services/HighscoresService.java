package com.texttwist.client.services;

import com.texttwist.client.App;
import com.texttwist.client.tasks.FetchHighscore;
import com.texttwist.client.tasks.WaitForScore;
import constants.Config;
import javafx.util.Pair;
import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * Created by loke on 28/06/2017.
 */
public class HighscoresService {

    ByteBuffer buffer = ByteBuffer.allocate(1024);
    public DefaultListModel<String> ranks = new DefaultListModel<>();

    SocketChannel clientSocket = null;

    public HighscoresService(){
        InetSocketAddress socketAddress = new InetSocketAddress(Config.GameServerURI, Config.GameServerPort);
        try {
            clientSocket = SocketChannel.open(socketAddress);
            clientSocket.configureBlocking(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

