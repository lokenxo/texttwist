package com.texttwist.server.tasks;

import com.texttwist.server.components.SessionsManager;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Created by loke on 19/06/2017.
 */
public class CheckOnlineUsers implements Callable<Boolean> {
    private final DefaultListModel<String> users;

    public CheckOnlineUsers( DefaultListModel<String> users) {
        this.users = users;
    }

    @Override
    public Boolean call() throws Exception {
        for(int i = 0; i < users.size(); i++){
            if(!(SessionsManager.getInstance().exists(users.get(i)))){
                return false;
            }
        }
        return true;
    }
}
