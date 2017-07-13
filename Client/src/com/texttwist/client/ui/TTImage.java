package com.texttwist.client.ui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


/**
 * Author:      Lorenzo Iovino on 13/06/2017.
 * Description: TTImage component
 */
public class TTImage extends JLabel{

    public TTImage(Point position, Dimension dimension, ImageIcon image, JPanel parent) throws IOException {
        super();
        setBounds(position.x,position.y,dimension.width, dimension.height);
        setPreferredSize(dimension);
        setIcon(image);

        parent.add(this);
    }
}
