package com.texttwist.client.ui;

import com.texttwist.client.constants.Palette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by loke on 13/06/2017.
 */
public class TTImage extends JLabel{

    public TTImage(Point position, Dimension dimension, ImageIcon image , JPanel parent) throws IOException {
        super();

        //setBackground(Palette.inputBox_backgroundColor);
        //setFont(Palette.inputBox_font);
        setBounds(position.x,position.y,dimension.width, dimension.height);
        setPreferredSize(dimension);
        setIcon(image);

        parent.add(this);

    }
}
