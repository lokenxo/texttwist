package com.texttwist.client.controllers;

import com.texttwist.client.pages.HighscoresPage;
import com.texttwist.client.tasks.FetchHighscore;
import javafx.util.Pair;
import javax.swing.*;
import java.io.ObjectOutput;
import java.util.concurrent.Callable;

import static com.texttwist.client.App.gameService;

/**
 * Author:      Lorenzo Iovino on 27/06/2017.
 * Description: Controller of the Highscore Page
 */
public class HighscoresController {

    public SwingWorker fetchHighscores(Callable<Void> callback) {
        return new FetchHighscore(callback);
    }

    public DefaultListModel<Pair<String,Integer>> getRanks(Boolean isPartialRank) {
        return isPartialRank ? gameService.ranks : gameService.globalRanks;
    }

}
