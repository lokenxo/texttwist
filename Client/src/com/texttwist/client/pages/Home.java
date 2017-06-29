package com.texttwist.client.pages;
import com.texttwist.client.controllers.HomeController;
import constants.Palette;
import com.texttwist.client.ui.*;
import com.texttwist.client.ui.TTDialog;
import models.Response;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;

public class Home extends Page {

    private TTContainer loginDataContainer;
    private HomeController homeController;
    private TTContainer logoContainer;

    public Home(JFrame window) {
        super(window);
        homeController = new HomeController();
        createUIComponents();
        window.setVisible(true);
    }

    @Override
    public void createUIComponents(){
        addLogo(root);
        loginDataContainer = new TTContainer(
               null,
                new Dimension(1150,250),
                Palette.root_backgroundColor,
                -1,
                root);

        TTInputField usernameField = new TTInputField(
                new Point(50,60),
                new Dimension(220,50),
                "Username",
                loginDataContainer);

        TTPasswordField passwordField = new TTPasswordField(
                new Point(280,60),
                new Dimension(220,50),
                loginDataContainer);

        TTButton loginBtn = new TTButton(
                new Point(50,120),
                new Dimension(450,50),
                "Go!",
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        //TODO CHIAMA API PER LOGIN E SE TUTTO OKEY MANDA A PAGINA DEL MENU
                        Response res = homeController.login(usernameField.getText(), String.valueOf(passwordField.getPassword()));
                        if (res.code == 200){
                            //OK, go to next page and show popup
                            return new Menu(window);
                        } else {
                            return new TTDialog("alert", res.message,
                                new Callable() {
                                @Override
                                public Object call() throws Exception {
                                    return new Home(window);
                                }
                            },null);
                        }
                    }
                },
                loginDataContainer);

        TTLabel registerText = new TTLabel(
                new Point(70,200),
                new Dimension(350,50),
                "Don't have an account?",
                new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 24),
                null,
                loginDataContainer);

        TTLabelBtn registerField = new TTLabelBtn(
                new Point(360, 200),
                new Dimension(210, 50),
                "Register!",
                new Font(Palette.inputBox_font.getFontName(), Font.BOLD, 34),
                null,
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return new Register(Page.window);
                    }
                },
                loginDataContainer);

    }
}
