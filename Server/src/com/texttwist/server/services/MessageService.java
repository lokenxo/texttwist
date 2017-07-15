package com.texttwist.server.services;

import com.texttwist.server.Server;
import com.texttwist.server.tasks.MessageDispatcher;
import com.texttwist.server.models.Dictionary;
import constants.Config;
import models.Message;
import java.net.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;


/**
 * Author:      Lorenzo Iovino on 17/06/2017.
 * Description: Message Service
 */
public class MessageService implements Runnable{

    private Selector selector = null;
    private ExecutorService dispatcherPool = Executors.newCachedThreadPool();

    public MessageService()
    {
        try {
            selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(Config.MessageServicePort));
            serverSocketChannel.register(selector, OP_ACCEPT);
            Server.logger.write("Message Service is running at "+Config.MessageServicePort +" port...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                ByteBuffer bufferMessages = ByteBuffer.allocate(1024);
                bufferMessages.clear();
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
                            if (client.read(bufferMessages) != -1) {
                                bufferMessages.flip();
                                String line = new String(bufferMessages.array(), bufferMessages.position(), bufferMessages.remaining());
                                if (line.startsWith("MESSAGE")) {
                                    Message msg = Message.toMessage(line);
                                    MessageDispatcher proxy = new MessageDispatcher(msg, client, bufferMessages);
                                    dispatcherPool.submit(proxy);
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