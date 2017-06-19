package com.texttwist.client.pages;

import com.texttwist.client.App;
import com.texttwist.client.constants.Config;
import interfaces.IAuth;
import models.Message;
import models.Response;
import org.json.simple.JsonObject;

import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by loke on 18/06/2017.
 */
public class MatchService {

    public MatchService(){
    }

    public Response play(DefaultListModel<String> userNames) throws IOException {


        InetSocketAddress socketAddress = new InetSocketAddress(Config.GameServerURI, Config.GameServerPort);
        SocketChannel clientSocket = SocketChannel.open(socketAddress);


        Message message = new Message("START_GAME", App.sessionService.account.token, userNames);

        byte[] byteMessage = new String(message.toString()).getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(byteMessage);

        clientSocket.write(buffer);


        //Risposta dal server
        /*JsonObject data = new JsonObject();
        data.put("obj", out);
        data.put("unavailableUsers", out);*/
        clientSocket.close();

        Response res = new Response("Player unavailable!",400, new JsonObject());
        return res;
    }
}
