package com.texttwist.server.tasks;

import com.texttwist.server.components.GameServer;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.concurrent.Callable;

/**
 * Created by loke on 25/06/2017.
 */
public class GenerateWords implements Callable<DefaultListModel<String>> {


    public GenerateWords(){

    }

    @Override
    public DefaultListModel<String> call() throws Exception {
        DefaultListModel<String> l = new DefaultListModel<String>();

        String word = GameServer.dict.getRandomWord(6, 7);
        System.out.println(word);
        for (int i = 0;i < word.length(); i++){
            l.addElement(String.valueOf(word.charAt(i)));
        }

        return l;
    }

}
