package com.texttwist.server.tasks;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Created by loke on 25/06/2017.
 */
public class GenerateWords implements Callable<DefaultListModel<String>> {


    public GenerateWords(){

    }

    @Override
    public DefaultListModel<String> call() throws Exception {
        DefaultListModel l = new DefaultListModel<String>();
        l.addElement("D");
        l.addElement("S");
        l.addElement("Q");
        l.addElement("A");



        return l;
    }

}
