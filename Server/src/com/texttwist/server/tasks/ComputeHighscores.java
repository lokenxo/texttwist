package com.texttwist.server.tasks;

import com.texttwist.server.components.AccountsManager;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Created by loke on 28/06/2017.
 */
public class ComputeHighscores implements Callable<DefaultListModel<String>> {

    public ComputeHighscores(){}

    @Override
    public DefaultListModel<String> call() throws Exception {
        DefaultListModel<String> l = new DefaultListModel<>();
        for(int i =0; i< AccountsManager.getInstance().users.size(); i++){
            l.addElement(AccountsManager.getInstance().users.get(i).userName+":"+AccountsManager.getInstance().users.get(i).score);
        }
        return l;
    }
}