package com.texttwist.client.pages;

import com.texttwist.client.App;
import com.texttwist.client.ui.TTDialog;
import com.texttwist.client.ui.TTLetter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by loke on 25/06/2017.
 */
public class GameController {


    public DefaultListModel<String> letters = new DefaultListModel<String>();
    public DefaultListModel<String> words = new DefaultListModel<String>();

    public GameController(){
    }

    public DefaultListModel<String> waitForPlayers() {
        return App.matchService.waitForPlayers();
    }

    public boolean addWordToWordsList(String word) {
        words.addElement(word);
        return true;
    }

}
