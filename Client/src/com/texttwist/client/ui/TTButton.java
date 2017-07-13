package com.texttwist.client.ui;

import constants.Palette;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Callable;


/**
 * Author:      Lorenzo Iovino on 13/06/2017.
 * Description: TTContainer component
 */
public class TTButton extends JButton{

    public TTButton(Point position, Dimension dimension, String caption, Callable<Object> clickHandler, JPanel parent) {
        super();

        setBackground(Palette.button_backgroundColor);
        setFont(Palette.button_font);
        setForeground(Palette.fontColor);
        setBounds(position.x,position.y,dimension.width, dimension.height);
        setPreferredSize(dimension);
        setText(caption);

        addMouseListener(new MouseAdapter() {
             @Override
             public void mousePressed(MouseEvent e) {
                 super.mouseClicked(e);
                 setForeground(Palette.registerLblBtn_onmouseclick_color);
             }

             @Override
             public void mouseReleased(MouseEvent e) {
                 super.mouseClicked(e);
                 setForeground(Palette.registerLblBtn_color);
                 try {
                     clickHandler.call();
                 } catch (Exception e1) {
                     e1.printStackTrace();
                 }
             }
         });

        parent.add(this);
    }
}
