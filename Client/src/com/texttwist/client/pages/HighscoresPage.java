package com.texttwist.client.pages;

import com.texttwist.client.controllers.HighscoresController;
import constants.Palette;
import com.texttwist.client.ui.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Highscores Page
 */
public class HighscoresPage extends Page{

    private TTContainer highscoreContainer;
    private Boolean isPartialScore;
    public JFrame window;
    private HighscoresController highscoreController;

    public HighscoresPage(JFrame window, Boolean isPartialScore) throws IOException {
        super(window);
        this.window = window;
        this.isPartialScore = isPartialScore;
        highscoreController = new HighscoresController(this);
        highscoreController.fetchHighscores().execute();
        createUIComponents();
        window.setVisible(true);
    }

    public void showHighscoreList(){
        new TTScrollList(
            new Point(20, 60),
            new Dimension(515, 142),
            highscoreController.getRanks(isPartialScore),
            highscoreContainer
        );
        window.revalidate();
        window.repaint();
    }

    @Override
    public void createUIComponents() throws IOException {
        addLogo(root);

        highscoreContainer = new TTContainer(
            null,
            new Dimension(1150,220),
            Palette.root_backgroundColor,
            -1,
            root
        );

        TTLabel title = new TTLabel(
            this.isPartialScore ? new Point(150,0) : new Point(200,0),
            new Dimension(350,50),
            this.isPartialScore ? "Scores of the match" : "Highscores",
            new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 38),
            null,
            highscoreContainer
        );

        addFooter(root);

        addBack(footer,
            new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return new MenuPage(Page.window);
                }
            }
        );
    }
}
