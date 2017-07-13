package com.texttwist.server.tasks;

import com.texttwist.server.services.MessageService;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 25/06/2017.
 * Description: Jedis Service
 */
public class GenerateLetters implements Callable<DefaultListModel<String>> {


    public GenerateLetters(){

    }

    @Override
    public DefaultListModel<String> call() throws Exception {
        DefaultListModel<String> l = new DefaultListModel<String>();

        String word = MessageService.dict.getRandomWord(6, 7);
        for (int i = 0;i < word.length(); i++){
            l.addElement(String.valueOf(word.charAt(i)));
        }

        return l;
    }

}
