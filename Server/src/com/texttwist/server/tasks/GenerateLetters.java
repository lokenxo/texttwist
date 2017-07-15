package com.texttwist.server.tasks;

import com.texttwist.server.Server;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 25/06/2017.
 * Description: Task: Generate Letters
 */
public class GenerateLetters implements Callable<DefaultListModel<String>> {

    @Override
    public DefaultListModel<String> call() throws Exception {
        DefaultListModel<String> l = new DefaultListModel<>();

        String word = Server.dict.getRandomWord(6, 7);
        for (int i = 0;i < word.length(); i++){
            l.addElement(String.valueOf(word.charAt(i)));
        }

        return l;
    }

}
