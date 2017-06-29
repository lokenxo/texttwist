package com.texttwist.client.pages;

import com.texttwist.client.App;
import com.texttwist.client.controllers.HighscoresController;
import constants.Palette;
import com.texttwist.client.ui.*;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by loke on 14/06/2017.
 */
public class Highscores extends Page{

    public TTContainer highscoreContainer;
    public Boolean isPartialScore;
    public HighscoresController highscoreController;
    public DefaultListModel<Pair<String, Integer>> ranks = new DefaultListModel<>();

    public Highscores(JFrame window, Boolean isPartialScore) throws IOException {
        super(window);
        this.isPartialScore = isPartialScore;

        highscoreController = new HighscoresController();
        System.out.println("SHSHSHSHs");
        System.out.println(App.match.ranks);
        System.out.println(App.match.globalRanks);

        System.out.println(ranks);
        System.out.println("SHSHSHSssssssHs");

        if(this.isPartialScore){
            this.ranks = App.match.ranks;
        } else {
            this.highscoreController.fetchHighscores(window);
            this.ranks = App.match.globalRanks;
        }
        createUIComponents();

        window.setVisible(true);
    }

    @Override
    public void createUIComponents() throws IOException {
        addLogo(root);
        highscoreContainer = new TTContainer(
                null,
                new Dimension(1150,220),
                Palette.root_backgroundColor,
                -1,
                root);

        TTLabel title = new TTLabel(
                new Point(200,0),
                new Dimension(350,50),
                this.isPartialScore ? "Scores of the match" : "Highscores",
                new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 38),
                null,
                highscoreContainer);

        TTScrollList highscoreList = new TTScrollList(
                new Point(20, 60),
                new Dimension(515, 142),
                this.ranks,
                highscoreContainer);
        addFooter(root);

        addBack(footer,
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return new Menu(Page.window);
                    }
                });

    }
}
