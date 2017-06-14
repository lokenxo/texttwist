package com.texttwist.client.pages;

import com.texttwist.client.constants.Palette;
import com.texttwist.client.ui.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by loke on 14/06/2017.
 */
public class MatchRequests extends Page{

    public TTContainer matchsContainer;
    MatchRequests(JFrame window) throws IOException {
        super(window);
        createUIComponents();
        window.setVisible(true);
    }

    //TODO Spostare i metodi di fetches nella classe MatchRequestService per separare MVC
    private DefaultListModel fetchMatches(){
        DefaultListModel<String> matchsList = new DefaultListModel<String>();
        matchsList.addElement("Pippo ti ha sfidato  -------  Accetta/Declina");
        matchsList.addElement("Paperino ti ha sfidato  -------  Accetta/Declina");
        matchsList.addElement("Minnie ti ha sfidato  -------  Accetta/Declina");
        matchsList.addElement("Luca ti ha sfidato  -------  Accetta/Declina");
        matchsList.addElement("Gino ti ha sfidato  -------  Accetta/Declina");
        matchsList.addElement("Filippo ti ha sfidato  -------  Accetta/Declina");
        matchsList.addElement("Yuri ti ha sfidato  -------  Accetta/Declina");
        return matchsList;
    }

    @Override
    public void createUIComponents() throws IOException {
        addLogo(root);
        matchsContainer = new TTContainer(
                null,
                new Dimension(1150,220),
                Palette.root_backgroundColor,
                -1,
                root);

        TTLabel title = new TTLabel(
                new Point(150,0),
                new Dimension(350,50),
                "Pending matches",
                new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 38),
                null,
                matchsContainer);

        TTScrollList highscoreList = new TTScrollList(
                new Point(20, 60),
                new Dimension(520, 142),
                fetchMatches(),
                matchsContainer);
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
