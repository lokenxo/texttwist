package com.texttwist.client.ui;

import constants.Palette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


/**
 * Author:      Lorenzo Iovino on 13/06/2017.
 * Description: TTButton component
 */
public class TTContainer extends JPanel{

    public TTContainer(Point innerPadding, Dimension dimension, Color backgroundColor, int axis, TTContainer parent) {
        super();
        if(axis == -1){
            setLayout(null);
        } else {
            setLayout(new BoxLayout(this, axis));
        }

        setBackground(backgroundColor != null ? backgroundColor : new Color(0,0,0,0));
        setFont(Palette.textFont);
        setMaximumSize(dimension);
        if(innerPadding != null) {
            setLocation(innerPadding);
            setBorder(new EmptyBorder(new Insets(innerPadding.y, innerPadding.x, dimension.width, dimension.height)));
        }
        if(parent != null) {
            parent.add(this);
        }
    }
}
