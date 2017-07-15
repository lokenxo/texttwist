package com.texttwist.server.tasks;

import com.texttwist.server.models.Accounts;
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

        Accounts.getInstance().users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.score.compareTo(o1.score);
            }
        });
        JedisService.removeAll("users");
        for(int i = 0; i< Accounts.getInstance().users.size(); i++){
            l.addElement(Accounts.getInstance().users.get(i).userName+":"+ Accounts.getInstance().users.get(i).score);
            JedisService.add("users", Accounts.getInstance().users.get(i));
        }
        return l;
    }
}