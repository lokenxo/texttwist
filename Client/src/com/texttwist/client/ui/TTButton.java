package com.texttwist.client.ui;

import com.texttwist.client.constants.Palette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by loke on 13/06/2017.
 */
public class TTButton extends JButton{

    public TTButton(Point position, Dimension dimension, String caption, JPanel parent) {
        super();

        setBackground(Palette.button_backgroundColor);
        setFont(Palette.button_font);
        setForeground(Palette.fontColor);
        setBounds(position.x,position.y,dimension.width, dimension.height);
        setPreferredSize(dimension);
        setText(caption);

        parent.add(this);

    }
}
