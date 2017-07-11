
package com.texttwist.server.components;
import com.texttwist.server.models.Dictionary;
import com.texttwist.server.models.Match;
import com.texttwist.server.tasks.ReceiveWords;
import constants.Config;
import models.Message;
import utilities.Logger;

import java.net.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

public class GameServer implements Runnable{

    private int serverPort;
    private ThreadProxy proxy;
    private ReceiveWords wordsReceiver;

    private DatagramChannel datagramChannel;
    private Selector selector = null;
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private String dictionaryPath = "./Server/resources/dictionary";
    public static Dictionary dict;
    SocketChannel client;
    ByteBuffer bufferWords = ByteBuffer.allocate(1024);
    ByteBuffer bufferMessages = ByteBuffer.allocate(1024);



    public static List<Match> activeMatches =  Collections.synchronizedList(new ArrayList<>());
    public static Integer multicastID = 4000;

    public GameServer(int port){
        this.serverPort = port;
    }

    public void run(){

        dict = new Dictionary(dictionaryPath);
        try {
            selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(serverPort));
            serverSocketChannel.register(selector, OP_ACCEPT);
            InetSocketAddress address = new InetSocketAddress(Config.WordsReceiverServerURI,Config.WordsReceiverServerPort);
            datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(true);
            datagramChannel.connect(address);
            Logger.write("Game Service is running at "+this.serverPort+" port...");

            wordsReceiver = new ReceiveWords(datagramChannel, bufferWords, bufferMessages, client);
            threadPool.submit(wordsReceiver);

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            System.out.println("WAITING FOR MSG");
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                bufferMessages = ByteBuffer.allocate(1024);
                bufferMessages.clear();
                client = null;
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
                            if (client.read(bufferMessages) != -1) {
                                bufferMessages.flip();
                                String line = new String(bufferMessages.array(), bufferMessages.position(), bufferMessages.remaining());
                                System.out.println(line);
                                if (line.startsWith("MESSAGE")) {
                                    SessionsManager.getInstance().printAll();
                                    Message msg = Message.toMessage(line);
                                    proxy = new ThreadProxy(msg, client, bufferMessages);
                                    threadPool.submit(proxy);
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
                            break;
                    }
                } catch (IOException e) {
                    try {
                        client.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}