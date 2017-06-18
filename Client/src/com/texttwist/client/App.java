package com.texttwist.client;

import com.texttwist.client.pages.AuthService;
import com.texttwist.client.pages.Home;
import com.texttwist.client.pages.SessionService;

import javax.swing.*;
import java.awt.*;

/**
 * Created by loke on 13/06/2017.
 */
public class App extends JFrame {

    public static AuthService authService;
    public static SessionService sessionService;

    public App(){
        setPreferredSize( new Dimension( 640, 480 ));
        setSize(new Dimension(640,480));
        setLocation(100,100);
        setResizable( false );
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Init services
        authService = new AuthService();
        sessionService = new SessionService();

        Home home = new Home(this);
    }
}
