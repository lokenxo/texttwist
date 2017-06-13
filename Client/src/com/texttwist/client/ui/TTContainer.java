package com.texttwist.client.ui;

import com.texttwist.client.constants.Palette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by loke on 13/06/2017.
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
        setFont(Palette.inputBox_font);
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
