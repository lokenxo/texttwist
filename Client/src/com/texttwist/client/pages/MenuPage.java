package com.texttwist.client.pages;

import com.texttwist.client.controllers.MenuController;
import constants.Palette;
import com.texttwist.client.ui.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 17/06/2017.
 * Description: Menu Page
 */
public class MenuPage extends Page{

    private MenuController menuController;

    public MenuPage(JFrame window) throws IOException {
        super(window);
        menuController = new MenuController();
        createUIComponents();
        window.setVisible(true);
    }

    @Override
    public void createUIComponents() throws IOException {
        addLogo(root);

        TTContainer menuBar = new TTContainer(
            null,
            new Dimension(1150, 280),
            Palette.root_backgroundColor,
            -1,
            root
        );

        new TTLabel(
            new Point(25,15),
            new Dimension(350,20),
            "Welcome back, " + menuController.getSession().account.userName + "!",
            new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 24),
            null,
            menuBar
        );

        new TTButton(
            new Point(25,70),
            new Dimension(250,75),
            "New GameService!",
            new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    //TODO CHIAMA API PER REGISTRAZIONE E SE TUTTO OKEY MANDA A PAGINA LOGIN
                    return new MatchSetupPage(window);
                }
            },
            menuBar
        );

        new TTButton(
            new Point(290,70),
            new Dimension(250,75),
            "In pending",
            new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    //TODO CHIAMA API PER REGISTRAZIONE E SE TUTTO OKEY MANDA A PAGINA LOGIN
                    return new MatchRequestsPage(window);
                }
            },
            menuBar
        );

        new TTButton(
            new Point(25, 155),
            new Dimension(250, 75),
            "Highscores",
            new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return new HighscoresPage(Page.window, false);
                }
            },
            menuBar
        );

        new TTButton(
            new Point(290, 155),
            new Dimension(250, 75),
            "Logout",
            new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    menuController.logout();
                    return new HomePage(Page.window);
                }
            },
            menuBar
        );

    }
}
