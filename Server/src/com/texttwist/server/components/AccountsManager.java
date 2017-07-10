package com.texttwist.server.components;

import models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by loke on 18/06/2017.
 */
public class AccountsManager {

    public List<User> users = Collections.synchronizedList(new ArrayList<User>());

    private static class Holder {
        static final AccountsManager INSTANCE = new AccountsManager();
    }

    public static AccountsManager getInstance() {
        return AccountsManager.Holder.INSTANCE;
    }

    private AccountsManager(){
        users.add(new User("a","a",0));
        users.add(new User("b","b",0));
        users.add(new User("c","c",0));
        users.add(new User("d","d",0));

    }

    public boolean register(String userName, String password) {
       if(!exists(userName)){
            return users.add(new User(userName, password,0));
        } else {
           return false;
       }
    }

    public boolean exists(String userName) {
            Iterator<User> i = users.iterator();
            while (i.hasNext()) {
                if (i.next().userName.equals(userName)) {
                    return true;
                }
            }
            return false;

    }

    public boolean checkPassword(String userName, String password) {
            Iterator<User> i = users.iterator();
            while (i.hasNext()) {
                User account = i.next();
                if (account.userName.equals(userName) && account.password.equals(password)) {
                    return true;
                }
            }
            return false;

    }

    public User findUser(String userName){
            Iterator<User> i = users.iterator();
            while (i.hasNext()) {
                User u = i.next();
                if (u.userName.equals(userName)) {
                    return u;
                }
            }
            return null;

    }

    public int size(){
        return users.size();
    }

}
