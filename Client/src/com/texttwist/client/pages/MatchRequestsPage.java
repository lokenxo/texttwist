package com.texttwist.client.pages;

import com.texttwist.client.App;
import constants.Palette;
import com.texttwist.client.ui.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * MatchRequest Page
 */
public class MatchRequestsPage extends Page{

    public TTContainer matchsContainer;
    MatchRequestsPage(JFrame window) throws IOException {
        super(window);
        createUIComponents();
        window.setVisible(true);
    }

    @Override
    public void createUIComponents() throws IOException {
        addLogo(root);
        matchsContainer = new TTContainer(
            null,
            new Dimension(1150,220),
            Palette.root_backgroundColor,
            -1,
            root
        );

        new TTLabel(
            new Point(150,0),
            new Dimension(350,50),
            "Pending matches",
            new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 38),
            null,
            matchsContainer
        );

        TTScrollList pendingMatches = new TTScrollList(
            new Point(20, 60),
            new Dimension(520, 142),
            App.gameService.pendingList,
            matchsContainer
        );

        pendingMatches.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                super.mouseClicked(evt);
                JList thisList = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    int index = thisList.locationToIndex(evt.getPoint());
                    App.gameService.joinMatch(App.gameService.pendingList.get(index));
                }
            }
        });

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
