package com.texttwist.client.pages;

import com.texttwist.client.constants.Palette;
import com.texttwist.client.ui.*;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by loke on 14/06/2017.
 */
public class Highscores extends Page{

    public TTContainer highscoreContainer;
    Highscores(JFrame window) throws IOException {
        super(window);
        createUIComponents();
        window.setVisible(true);
    }

    private DefaultListModel fetchHighscores(){
        DefaultListModel<String> highscoreList = new DefaultListModel<String>();
        highscoreList.addElement("Pippo 41");
        highscoreList.addElement("Paperino 37");
        highscoreList.addElement("Gaia 34");
        highscoreList.addElement("Luigi 32");
        highscoreList.addElement("Marco 31");
        highscoreList.addElement("Minnie 30");
        highscoreList.addElement("Franco 30");
        highscoreList.addElement("Qua 29");
        highscoreList.addElement("Luca 27");
        highscoreList.addElement("Qui 26");
        highscoreList.addElement("Jorge 25");
        highscoreList.addElement("David 22");
        highscoreList.addElement("Quo 21");
        highscoreList.addElement("Raphael 21");
        highscoreList.addElement("Miguel 16");
        highscoreList.addElement("Carmen 14");
        highscoreList.addElement("Beatriz 12");
        return highscoreList;
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
                "Highscores",
                new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 38),
                null,
                highscoreContainer);

        TTScrollList highscoreList = new TTScrollList(
                new Point(20, 60),
                new Dimension(515, 142),
                fetchHighscores(),
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
