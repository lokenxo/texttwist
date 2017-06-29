package com.texttwist.client.services;

import models.Session;

/**
 * Created by loke on 17/06/2017.
 */
public class SessionService {

    public Session account;
    public SessionService(){}

    public void create(String userName, String token) {
        account = new Session(userName, token);
    }

    public void remove(){
        account = null;
    }
}
