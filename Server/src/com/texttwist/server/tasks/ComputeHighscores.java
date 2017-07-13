package com.texttwist.server.tasks;

import com.texttwist.server.services.AccountsService;
import com.texttwist.server.services.JedisService;
import models.User;

import javax.swing.*;
import java.util.Comparator;
import java.util.concurrent.Callable;

/**
 * Created by loke on 28/06/2017.
 */
public class ComputeHighscores implements Callable<DefaultListModel<String>> {

    public ComputeHighscores(){}

    @Override
    public DefaultListModel<String> call() throws Exception {
        DefaultListModel<String> l = new DefaultListModel<>();

        AccountsService.getInstance().users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.score.compareTo(o1.score);
            }
        });
        JedisService.removeAll("users");
        for(int i = 0; i< AccountsService.getInstance().users.size(); i++){
            l.addElement(AccountsService.getInstance().users.get(i).userName+":"+ AccountsService.getInstance().users.get(i).score);
            JedisService.add("users", AccountsService.getInstance().users.get(i));
        }
        return l;
    }
}