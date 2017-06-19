package com.texttwist.client.ui;
import com.texttwist.client.App;
import constants.Palette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Callable;


/**
 * Created by loke on 14/06/2017.
 */
public class TTSearchBar extends TTContainer{

    private DefaultListModel matchedUsers = new DefaultListModel();
    public DefaultListModel<String> list = new DefaultListModel<String>();

    private Callable<Object> add(TTInputField ctx){
        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                String username = ctx.getText();
                if(!username.equals("") && !username.equals(App.sessionService.account.userName)) {
                    ctx.setText("");
                    list.addElement(username);
                }
                return null;
            }
        };
    }


    public TTSearchBar(Point position,
                       Dimension dimension,
                       String placeholder,
                       TTContainer parent) throws Exception {

        super(position, dimension, Palette.inputBox_backgroundColor, -1, parent);
        setBackground(Palette.scrollPanel_backgroundColor);
        setFont(Palette.inputBox_font);
        setBounds(position.x, position.y, dimension.width, dimension.height);
        setPreferredSize(dimension);
        setForeground(Palette.fontColor);

        TTLabel playerFinder_flavourText = new TTLabel(
                new Point(20,40),
                new Dimension(350,50),
                "<html>Add player</html>",
                new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 18),
                null,
                parent);

        TTInputField usernameField = new TTInputField(
                new Point(20,80),
                new Dimension(250,45),
                placeholder,
                parent);

        /*TTScrollList userList = new TTScrollList(
                new Point(20,120),
                new Dimension(250,95),
                matchedUsers,
                parent
        );*/

        TTButton addUser = new TTButton(
                new Point(70,140),
                new Dimension(150,50),
                "Add!",
                add(usernameField),
                parent);


        TTLabel playerToSendInvite_flavourText = new TTLabel(
                new Point(305,40),
                new Dimension(350,50),
                "Double-Click on item for remove",
                new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 18),
                null,
                parent);

        TTScrollList playerToSendInvite = new TTScrollList(
                new Point(305, 80),
                new Dimension(232, 135),
                list,
                parent);


        playerToSendInvite.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                super.mouseClicked(evt);
                JList thisList = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {

                    // Double-click detected
                    int index = thisList.locationToIndex(evt.getPoint());
                    list.remove(index);
                }
            }
        });

        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == 10){
                    try {
                        add(usernameField).call();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        parent.add(this);
    }
}
