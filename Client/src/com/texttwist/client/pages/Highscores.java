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
        highscoreList.addElement("USA");
        highscoreList.addElement("India");
        highscoreList.addElement("Vietnam");
        highscoreList.addElement("Canada");
        highscoreList.addElement("Denmark");
        highscoreList.addElement("France");

        highscoreList.addElement("France");
        highscoreList.addElement("Great Britain");
        highscoreList.addElement("Japan");

        highscoreList.addElement("France");
        highscoreList.addElement("Great Britain");
        highscoreList.addElement("Japan");

        highscoreList.addElement("France");
        highscoreList.addElement("Great Britain");
        highscoreList.addElement("Japan");
        highscoreList.addElement("Great Britain");
        highscoreList.addElement("Japan");
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
                new Point(0, 60),
                new Dimension(575, 142),
                fetchHighscores(),
                highscoreContainer);
        addBack(root,
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return new Menu(Page.window);
                    }
                });

    }
}
