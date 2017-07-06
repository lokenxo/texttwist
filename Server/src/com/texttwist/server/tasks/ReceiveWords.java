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
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by loke on 27/06/2017.
 */
public class ReceiveWords implements Callable<Boolean>{

    protected ExecutorService threadPool = Executors.newCachedThreadPool();

    public DatagramChannel DatagramChannel;
    public final Match match;
    byte[] receiveData = new byte[1024];
    ByteBuffer buffer;


    public ReceiveWords(Match match, DatagramChannel DatagramChannel, ByteBuffer buffer) {
        this.match = match;
        this.buffer = buffer;
        this.DatagramChannel = DatagramChannel;
    }

    @Override
    public Boolean call() throws Exception {
        System.out.print("READY TO Receive words !!!!");

        Future<Boolean> matchTimeout = threadPool.submit(new MatchTimeout());

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        Message msg;
        while(true) {
            DatagramChannel.receive(buffer);
            buffer.flip();
            System.out.println(buffer.limit());
            int limits = buffer.limit();
            byte bytes[] = new byte[limits];
            buffer.get(bytes, 0, limits);
            String rcv = new String(bytes);


            System.out.println("RECEIVED: " + rcv);
            buffer.rewind();
            msg = Message.toMessage(rcv);
            if(msg.message.equals("WORDS")){
                break;
            }
        }
        Future<Integer> computeScore = threadPool.submit(new ComputeScore(msg.sender, match, msg.data));

        //Se tutti hanno inviato le parole, blocca il timer e restituisci true
        computeScore.get();
        System.out.println(match.matchCreator);
        System.out.println(match.allPlayersSendedHisScore());


        if(match.allPlayersSendedHisScore()){
            System.out.println("TIMEOUT BLOCCATO, OK");
           // match.setUndefinedScorePlayersToZero();

            matchTimeout.cancel(true);
            DatagramChannel.close();
            return true;
        }
        return false;

    }

}
