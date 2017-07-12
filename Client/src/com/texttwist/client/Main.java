package com.texttwist.client;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Client started");

        //Load fonts
        try {
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
                    new File("./Client/resources/fonts/DK Trained Monkey.otf").getCanonicalPath())));

        } catch (IOException|FontFormatException e) {
            System.out.println("ERROR: Font not found!");
        }

        App entrypoint = new App();
    }
}
