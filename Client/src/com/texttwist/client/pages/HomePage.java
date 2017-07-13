package com.texttwist.client.pages;

import com.texttwist.client.controllers.HomeController;
import constants.Palette;
import com.texttwist.client.ui.*;
import com.texttwist.client.ui.TTDialog;
import models.Response;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 23/06/2017.
 * Description: Home Page
 */
public class HomePage extends Page {

    private HomeController homeController;

    public HomePage(JFrame window) {
        super(window);
        homeController = new HomeController();

        createUIComponents();
        window.setVisible(true);
    }

    @Override
    public void createUIComponents(){
        addLogo(root);

        TTContainer loginDataContainer = new TTContainer(
           null,
            new Dimension(1150,250),
            Palette.root_backgroundColor,
            -1,
            root
        );

        TTInputField usernameField = new TTInputField(
            new Point(50,60),
            new Dimension(220,50),
            "Username",
            loginDataContainer
        );

        TTPasswordField passwordField = new TTPasswordField(
            new Point(280,60),
            new Dimension(220,50),
            loginDataContainer
        );

        new TTButton(
            new Point(50,120),
            new Dimension(450,50),
            "Go!",
            new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Response res = homeController.login(usernameField.getText(), String.valueOf(passwordField.getPassword()));
                    if (res.code == 200){
                        return new MenuPage(window);
                    } else {
                        return new TTDialog("alert", res.message,
                            new Callable() {
                            @Override
                            public Object call() throws Exception {
                                return new HomePage(window);
                            }
                        },null);
                    }
                }
            },
            loginDataContainer
        );

        new TTLabel(
            new Point(70,200),
            new Dimension(350,50),
            "Don't have an account?",
            new Font(Palette.textFont.getFontName(), Font.ITALIC, 26),
            null,
            loginDataContainer
        );

        new TTLabelBtn(
            new Point(360, 200),
            new Dimension(210, 50),
            "Register!",
            new Font(Palette.textFont.getFontName(), Font.BOLD, 30),
            null,
            new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return new RegisterPage(Page.window);
                }
            },
            loginDataContainer
        );
    }
}
