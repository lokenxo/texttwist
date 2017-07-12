package com.texttwist.client.controllers;

import com.texttwist.client.App;
import com.texttwist.client.pages.HighscoresPage;
import com.texttwist.client.tasks.FetchHighscore;
import javafx.util.Pair;
import javax.swing.*;

/**
 * Author:      Lorenzo Iovino on 27/06/2017.
 * Description: Controller of the Highscore Page
 */
public class HighscoresController {

    private HighscoresPage highscoresPage;

    public HighscoresController(HighscoresPage highscoresPage){
        this.highscoresPage = highscoresPage;
    }

    public SwingWorker fetchHighscores () {
        return new FetchHighscore(highscoresPage);
    }

    public DefaultListModel<Pair<String,Integer>> getRanks(Boolean isPartialRank) {
        return isPartialRank ? App.gameService.ranks : App.gameService.globalRanks;
    }

}
