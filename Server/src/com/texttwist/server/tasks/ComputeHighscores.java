package com.texttwist.server.tasks;

import com.texttwist.server.managers.AccountsManager;
import com.texttwist.server.services.JedisService;
import models.User;

import javax.swing.*;
import java.util.Comparator;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 28/06/2017.
 * Description: Task: Compute Highscores
 */
public class ComputeHighscores implements Callable<DefaultListModel<String>> {

    @Override
    public DefaultListModel<String> call() throws Exception {
        DefaultListModel<String> l = new DefaultListModel<>();

        AccountsManager.getInstance().users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.score.compareTo(o1.score);
            }
        });
        JedisService.removeAll("users");
        for(int i = 0; i< AccountsManager.getInstance().users.size(); i++){
            l.addElement(AccountsManager.getInstance().users.get(i).userName+":"+ AccountsManager.getInstance().users.get(i).score);
            JedisService.add("users", AccountsManager.getInstance().users.get(i));
        }
        return l;
    }
}