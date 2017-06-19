package com.texttwist.server.components;

import models.Account;
import models.Session;
import models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by loke on 18/06/2017.
 */
public class AccountsManager {

    private List<Account> accounts = Collections.synchronizedList(new ArrayList<Account>());

    private static class Holder {
        static final AccountsManager INSTANCE = new AccountsManager();
    }

    public static AccountsManager getInstance() {
        return AccountsManager.Holder.INSTANCE;
    }

    private AccountsManager(){}

    public boolean register(String userName, String password) {
       if(!exists(userName)){
            return accounts.add(new Account(userName, password));
        } else {
           return false;
       }
    }

    public boolean exists(String userName) {
        synchronized(accounts) {
            Iterator<Account> i = accounts.iterator();
            while (i.hasNext()) {
                if (i.next().userName.equals(userName)) {
                    return true;
                }
            }
            return false;
        }
    }


    public boolean checkPassword(String userName, String password) {
        synchronized(accounts) {
            Iterator<Account> i = accounts.iterator();
            while (i.hasNext()) {
                Account account = i.next();
                if (account.userName.equals(userName) && account.password.equals(password)) {
                    return true;
                }
            }
            return false;
        }
    }

    public int size(){
        return accounts.size();
    }

}
