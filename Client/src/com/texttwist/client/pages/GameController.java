package com.texttwist.client.pages;

import com.texttwist.client.App;
import com.texttwist.client.tasks.StartGame;
import com.texttwist.client.tasks.WaitForPlayers;
import com.texttwist.client.tasks.WaitForScore;
import com.texttwist.client.ui.TTDialog;
import com.texttwist.client.ui.TTLetter;
import constants.Config;
import models.Message;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by loke on 25/06/2017.
 */
public class GameController {


    public DefaultListModel<String> letters = new DefaultListModel<String>();
    public DefaultListModel<String> words = new DefaultListModel<String>();

    public GameController(){
    }

    public DefaultListModel<String> waitForPlayers() {
        return App.matchService.waitForPlayers();
    }


    public void waitForScore(){
        SwingWorker worker = new WaitForScore();
        worker.execute();

    }

    public boolean addWordToWordsList(String word) {
        words.addElement(word);
        return true;
    }

    public Callable<Object> sendWords(DefaultListModel<String> words){
        System.out.println("SENDDDD" + words);

        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();

            InetAddress IPAddress = InetAddress.getByName(Config.WordsReceiverServerURI);
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
            Message msg = new Message("WORDS",App.sessionService.account.userName,"",words);
            String sentence = msg.toString();
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Config.WordsReceiverServerPort);
            clientSocket.send(sendPacket);
            clientSocket.close();
            waitForScore();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Thread startGame(Game game){
        Thread t = new Thread(new StartGame(game));
        t.start();
        return t;
    }


}
