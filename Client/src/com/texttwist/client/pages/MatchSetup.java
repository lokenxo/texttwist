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
public class MatchSetup extends Page{

    public TTContainer matchSetupContainer;
    MatchSetup(JFrame window) throws IOException {
        super(window);
        createUIComponents();
        window.setVisible(true);
    }

    private Callable addUserToInvitationList(){
        return null;
    }

    //TODO Spostare i metodi di fetches nella classe MatchSetupService per separare MVC
    private DefaultListModel fetchUsers(){
        DefaultListModel<String> usersList = new DefaultListModel<String>();
        usersList.addElement("Pippo");
        usersList.addElement("Paperino");
        usersList.addElement("Gaia");
        usersList.addElement("Luigi");
        usersList.addElement("Marco");
        usersList.addElement("Minnie");
        usersList.addElement("Franco");
        usersList.addElement("Qua");
        usersList.addElement("Luca");
        usersList.addElement("Qui");
        usersList.addElement("Jorge");
        usersList.addElement("David");
        usersList.addElement("Quo");
        usersList.addElement("Raphael");
        usersList.addElement("Miguel");
        usersList.addElement("Carmen");
        usersList.addElement("Beatriz");
        return usersList;
    }

    @Override
    public void createUIComponents() throws IOException {
        addLogo(root);

        matchSetupContainer = new TTContainer(
                null,
                new Dimension(1150,220),
                Palette.root_backgroundColor,
                -1,
                root);

        TTLabel title = new TTLabel(
                new Point(170,0),
                new Dimension(350,50),
                "Invite players",
                new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 38),
                null,
                matchSetupContainer);

        TTLabel playerFinder_flavourText = new TTLabel(
                new Point(20,40),
                new Dimension(350,50),
                "<html>Search players to invite...</html>",
                new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 18),
                null,
                matchSetupContainer);

       TTSearchBar searchUserBar = new TTSearchBar(
               new Point(20, 80),
               new Dimension(250, 40),
               "Username",
               new DefaultListModel(),
               addUserToInvitationList(),
               matchSetupContainer);

        TTLabel playerToSendInvite_flavourText = new TTLabel(
                new Point(305,40),
                new Dimension(350,50),
                "Click on user for remove it",
                new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 18),
                null,
                matchSetupContainer);

        TTScrollList playerToSendInvite = new TTScrollList(
                new Point(305, 80),
                new Dimension(232, 135),
                new DefaultListModel(),
                matchSetupContainer);

        addFooter(root);
        addNext(footer,
                new Font(Palette.inputBox_font.getFontName(), Font.BOLD, 40),
                null,
                "Play!",
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return new Game(Page.window);
                    }
                });

        addBack(footer,
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return new Menu(Page.window);
                    }
                });

    }
}
