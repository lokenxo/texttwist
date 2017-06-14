package com.texttwist.client.ui;

import com.texttwist.client.constants.Palette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by loke on 14/06/2017.
 */
public class TTImageBtn extends TTImage {
    public TTImageBtn(Point position, Dimension dimension, ImageIcon image, Callable<Object> clickHandler, JPanel parent) throws IOException {
        super(position,dimension,image,parent);

        addMouseListener(new MouseAdapter() {
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
    }
}
