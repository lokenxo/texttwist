package com.texttwist.client.controllers;

import javax.swing.*;
import static com.texttwist.client.App.gameService;

/**
 * Author:      Lorenzo Iovino on 18/06/2017.
 * Description: Controller of the Match Setup Page
 */
public class MatchSetupController {

    public void play(DefaultListModel<String> userNames) {
        gameService.beginMatch(userNames);
    }
}
