package com.texttwist.client.pages;

import com.texttwist.client.constants.Palette;
import com.texttwist.client.ui.TTContainer;
import com.texttwist.client.ui.TTImage;
import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by loke on 13/06/2017.
 */
public class Entrypoint extends JFrame {

    protected TTContainer root;

    public Entrypoint(){
        setPreferredSize( new Dimension( 640, 480 ));
        setSize(new Dimension(640,480));
        setLocation(100,100);
        setResizable( false );
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Home home = new Home(this);
    }
}
