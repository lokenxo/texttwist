package com.texttwist.client.pages;

import com.texttwist.client.ui.TTContainer;

import javax.swing.*;
import java.awt.*;

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
