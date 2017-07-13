package com.texttwist.server.services;

import models.User;
import java.io.Serializable;
import java.util.*;


/**
 * Author:      Lorenzo Iovino on 18/06/2017.
 * Description: AccountsService
 */
public class AccountsService {

    public List<User> users = Collections.synchronizedList(new ArrayList<User>());

    private static class Holder {
        static final AccountsService INSTANCE = new AccountsService();
    }

    public static AccountsService getInstance() {
        return AccountsService.Holder.INSTANCE;
    }

    private AccountsService(){
        List<Serializable> l = JedisService.get("users");
        for(int i=0; i<l.size(); i++) {
            users.add((User) l.get(i));
        }
    }

    public boolean register(String userName, String password) {
       if(!exists(userName)){
           User newUser = new User(userName, password,0);
           Boolean res = users.add(newUser);
           JedisService.add("users", newUser);
           return res;
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
}
