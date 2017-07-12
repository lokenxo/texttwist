package com.texttwist.client.controllers;

import com.texttwist.client.App;
import javax.swing.*;
import java.io.IOException;

/**
 * Author:      Lorenzo Iovino on 18/06/2017.
 * Description: Controller of the Match Setup Page
 */
public class MatchSetupController {

    public Object play(DefaultListModel<String> userNames) {
        try {
            return App.gameService.play(userNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
