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
        users.add(new User("a","a"));
        users.add(new User("b","b"));
        users.add(new User("c","c"));
    }

    public boolean register(String userName, String password) {
       if(!exists(userName)){
            return users.add(new User(userName, password));
        } else {
           return false;
       }
    }

    public boolean exists(String userName) {
        synchronized(users) {
            Iterator<User> i = users.iterator();
            while (i.hasNext()) {
                if (i.next().userName.equals(userName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean checkPassword(String userName, String password) {
        synchronized(users) {
            Iterator<User> i = users.iterator();
            while (i.hasNext()) {
                User account = i.next();
                if (account.userName.equals(userName) && account.password.equals(password)) {
                    return true;
                }
            }
            return false;
        }
    }

    public User findUser(String userName){
        synchronized(users) {
            Iterator<User> i = users.iterator();
            while (i.hasNext()) {
                User u = i.next();
                if (u.userName.equals(userName)) {
                    return u;
                }
            }
            return null;
        }
    }

    public int size(){
        return users.size();
    }

}
