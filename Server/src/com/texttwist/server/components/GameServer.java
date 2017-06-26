
package com.texttwist.server.components;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.texttwist.server.models.Dictionary;
import com.texttwist.server.models.Match;
import com.texttwist.server.tasks.SendInvitations;
import com.texttwist.server.tasks.WaitForPlayers;
import jdk.nashorn.internal.parser.JSONParser;
import models.Message;
import org.json.simple.JsonObject;
import utilities.Logger;

import javax.swing.*;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

public class GameServer implements Runnable{

    protected int serverPort;
    protected ServerSocketChannel serverSocketChannel = null;
    protected ThreadProxy proxy;
    protected Selector selector = null;
    protected ExecutorService threadPool = Executors.newCachedThreadPool();
    private String dictionaryPath = "./Server/resources/dictionary";
    public static Dictionary dict;


    public static DefaultListModel<Match> activeMatches = new DefaultListModel<Match>();

    public GameServer(int port){
        this.serverPort = port;
    }

    public void run(){

        dict = new Dictionary(dictionaryPath);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(serverPort));
            serverSocketChannel.register(selector, OP_ACCEPT);
            Logger.write("Game Service is running at "+this.serverPort+" port...");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SocketChannel client = null;
                SelectionKey key = iter.next();
                iter.remove();

                try {
                    switch (key.readyOps()) {
                        case OP_ACCEPT:
                            client = ((ServerSocketChannel) key.channel()).accept();
                            client.configureBlocking(false);
                            client.register(selector, OP_READ);
                            break;

                        case OP_READ:
                            client = (SocketChannel) key.channel();
                            buffer.clear();
                            if (client.read(buffer) != -1) {
                                buffer.flip();
                                String line = new String(buffer.array(), buffer.position(), buffer.remaining());

                                if (line.startsWith("MESSAGE")) {
                                    Message msg = Message.toMessage(line);
                                    proxy = new ThreadProxy(msg, client);
                                    Future<Boolean> identifyMessage = threadPool.submit(proxy);
                                    key.cancel();
                                }


                                if (line.startsWith("CLOSE")) {
                                    client.close();
                                } else if (line.startsWith("QUIT")) {
                                    for (SelectionKey k : selector.keys()) {
                                        k.cancel();
                                        k.channel().close();
                                    }
                                    selector.close();
                                    return;
                                }
                            } else {
                                key.cancel();
                            }
                            break;

                        default:
                            System.out.println("unhandled " + key.readyOps());
                            break;
                    }
                } catch (IOException e) {
                    try {
                        client.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                        e.printStackTrace();
                    }
                }
        }
    }
}