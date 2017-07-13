package com.texttwist.client.controllers;

import com.texttwist.client.App;
import javax.swing.*;

/**
 * Author:      Lorenzo Iovino on 13/072017.
 * Description: Controller of the Match Request Page
 */
public class MatchRequestController {

    public DefaultListModel<String> getPendingList(){
        return App.gameService.pendingList;
    }

    public void joinMatch(String matchName){
        App.gameService.joinMatch(matchName);
    }
}
