package com.texttwist.server.tasks;

import com.texttwist.server.Server;
import com.texttwist.server.components.GameServer;
import com.texttwist.server.models.Match;
import constants.Config;
import models.Message;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by loke on 27/06/2017.
 */
public class ReceiveWords implements Callable<Boolean>{

    protected ExecutorService threadPool = Executors.newCachedThreadPool();

    public Boolean receiveWords = true;
    public DatagramSocket datagramSocket;
    public Match match;
    public ReceiveWords(Match match, DatagramSocket datagramSocket) {
        this.match = match;
        this.datagramSocket = datagramSocket;
    }

    @Override
    public Boolean call() throws Exception {
        System.out.print("READY TO Receive words !!!!");


        byte[] receiveData = new byte[1024];

        Future<Boolean> matchTimeout = threadPool.submit(new MatchTimeout(receiveWords));

        while(receiveWords)
        {
            receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            datagramSocket.receive(receivePacket);
            String rcv = new String( receivePacket.getData());
            System.out.println("RECEIVED: " + rcv);
            Message msg = Message.toMessage(rcv);
            Future<Integer> computeScore = threadPool.submit(new ComputeScore(msg.sender, match, msg.data));

            //Se tutti hanno inviato le parole, blocca il timer e restituisci true
            computeScore.get();
            System.out.println("All player sended?");
            System.out.println(match.allPlayersSendedHisScore());
            if(match.allPlayersSendedHisScore()){
                System.out.println("TIMEOUT BLOCCATO, OK");
                matchTimeout.cancel(true);
               // datagramSocket.close();
                return true;
            }

        }
        return false;
    }

}
