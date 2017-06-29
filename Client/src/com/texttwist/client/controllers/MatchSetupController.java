package com.texttwist.client.controllers;

import com.texttwist.client.App;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by loke on 18/06/2017.
 */
public class MatchSetupController {

    public Object play(DefaultListModel<String> userNames) {
        try {
            return App.game.play(userNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
