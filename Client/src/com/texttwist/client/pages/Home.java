package com.texttwist.client.pages;
import com.texttwist.client.constants.Palette;
import com.texttwist.client.ui.*;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;

public class Home extends Page {

    private TTContainer loginDataContainer;
    private TTContainer logoContainer;

    public Home(JFrame window) {
        super(window);
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
                new Point(70,60),
                new Dimension(210,50),
                "Username",
                loginDataContainer);
        TTPasswordField passwordField = new TTPasswordField(
                new Point(290,60),
                new Dimension(210,50),
                loginDataContainer);
        TTButton loginBtn = new TTButton(
                new Point(70,120),
                new Dimension(430,50),
                "Go!",
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
                new Callable<Page>() {
                    @Override
                    public Page call() throws Exception {
                        return new Register(Page.window);
                    }
                },
                loginDataContainer);
    }
}
