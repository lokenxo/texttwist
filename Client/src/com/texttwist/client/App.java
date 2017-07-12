package com.texttwist.client;

import com.texttwist.client.services.GameService;
import com.texttwist.client.services.AuthService;
import com.texttwist.client.pages.HomePage;
import models.Session;
import utilities.Logger;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * Author:      Lorenzo Iovino on 13/06/2017.
 * Description: The App entrypoint.
 *              Here is possible to declare services globally accessible.
 */
public class App extends JFrame {
    public static AuthService authService;
    public static GameService gameService;
    public static Logger logger;
    public static Session session;
    public static JFrame app;

    public App() throws IOException {
        setPreferredSize( new Dimension( 640, 480 ));
        setSize(new Dimension(640,480));
        setLocation(100,100);
        setResizable( false );
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*Setup logger*/
        String id = ManagementFactory.getRuntimeMXBean().getName();
        logger = new Logger(new File("./client_"+id+".log"), "Client"+id, true);
        logger.write("Client starting ...");

        /*Load fonts*/
        try {
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
                    new File("./Client/resources/fonts/DK Trained Monkey.otf").getCanonicalPath())));
        } catch (IOException|FontFormatException e) {
            logger.write("APP: Font not found!");
        }

        /*Services*/
        gameService = new GameService();
        authService = new AuthService();

        app = this;

        new HomePage(this);
    }

    public static Point getWindowsPosition(){
        return new Point(app.getX(), app.getY());
    }
}
