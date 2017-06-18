
package com.texttwist.server.components;
import com.sun.org.apache.xpath.internal.operations.Bool;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.JsonObject;
import utilities.Logger;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.*;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

public class GameServer implements Runnable{

    protected int serverPort;
    protected ServerSocketChannel serverSocketChannel = null;
    protected ThreadProxy proxy;
    protected Selector selector = null;
    protected JsonObject msg = null;

    public GameServer(int port){
        this.serverPort = port;
    }

    public void run(){

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
                SocketChannel client;
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

                                proxy = new ThreadProxy(JSONParser.quote(line));
                                Thread t = new Thread(proxy);
                                t.start();

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
                    e.printStackTrace();
                }
            }
     /*   try {
            this.serverSocket = new ServerSocket(this.serverPort);
            Logger.write("Game Service is running at "+this.serverPort+" port...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            proxy = new ThreadProxy(clientSocket);
            Thread t = new Thread(proxy);
            t.start();

*/

            // threadPool.shutdown(); // shutdown the pool.
            //this.threadPool.execute(new ThreadWorker(clientSocket, "TASK DI PROVA"));
        }
    }
}