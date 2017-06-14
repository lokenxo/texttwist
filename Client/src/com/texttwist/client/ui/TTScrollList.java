package com.texttwist.client.ui;

import com.texttwist.client.constants.Palette;

import javax.swing.*;
import java.awt.*;

/**
 * Created by loke on 14/06/2017.
 */
public class TTScrollList extends JList {

    public TTScrollList(Point position, Dimension dimension, ListModel listModel, JPanel parent){
        super(listModel);
        setBackground(Palette.scrollPanel_backgroundColor);
        setFont(Palette.inputBox_font);
        setBounds(position.x, position.y, dimension.width, dimension.height);
       // setPreferredSize(dimension);
        setForeground(Palette.fontColor);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(position.x, position.y, dimension.width, dimension.height);
       // scrollPane.setMinimumSize(dimension);
        scrollPane.setBackground(Palette.scrollPanel_backgroundColor);
        scrollPane.setViewportView(this);

        parent.add(scrollPane);
    }
}
