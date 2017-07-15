package com.texttwist.client.controllers;

import com.texttwist.client.tasks.InvitePlayers;

import javax.swing.*;
import static com.texttwist.client.App.gameService;

/**
 * Author:      Lorenzo Iovino on 18/06/2017.
 * Description: Controller of the Match Setup Page
 */
public class MatchSetupController {

    public void play(DefaultListModel<String> userNames) {
        new InvitePlayers(userNames).execute();

    }
}
