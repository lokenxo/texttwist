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
public class Menu extends Page{

    private TTContainer menuBar;
    private MenuController menuController;

    public Menu(JFrame window) throws IOException {
        super(window);
        createUIComponents();
        menuController = new MenuController();
        window.setVisible(true);

    }

    @Override
    public void createUIComponents() throws IOException {
        addLogo(root);

        menuBar = new TTContainer(
                null,
                new Dimension(1150,280),
                Palette.root_backgroundColor,
                -1,
                root);

        TTLabel playerToSendInvite_flavourText = new TTLabel(
                new Point(25,15),
                new Dimension(350,20),
                "Welcome back, " + App.sessionService.account.userName + "!",
                new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 24),
                null,
                menuBar);

        TTButton newMatch = new TTButton(
                new Point(25,70),
                new Dimension(250,75),
                "New Match!",
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        //TODO CHIAMA API PER REGISTRAZIONE E SE TUTTO OKEY MANDA A PAGINA LOGIN
                        return new MatchSetup(window);
                    }
                },
                menuBar);
        TTButton matchRequests = new TTButton(
                new Point(290,70),
                new Dimension(250,75),
                "In pending",
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        //TODO CHIAMA API PER REGISTRAZIONE E SE TUTTO OKEY MANDA A PAGINA LOGIN
                        return new MatchRequests(window);
                    }
                },
                menuBar);
        TTCircleCounter circleCounter = new TTCircleCounter(
                new Point(290,70),
                new Dimension(25,25),
                menuBar.getGraphics(),
                menuBar);

        TTButton highscores = new TTButton(
                new Point(25, 155),
                new Dimension(250, 75),
                "Highscores",
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return new Highscores(Page.window);
                    }
                },
                menuBar);

        TTButton logout = new TTButton(
                new Point(290, 155),
                new Dimension(250, 75),
                "Logout",
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        menuController.logout("","");
                        return new Home(Page.window);
                    }
                },
                menuBar);

    }
}
