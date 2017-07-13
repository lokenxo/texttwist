package com.texttwist.server.services;
import models.Session;
import models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * Author:      Lorenzo Iovino on 17/06/2017.
 * Description: SessionsService. It is a singleton that provides the model and methods for manage sessions
 */
public class SessionsService {

    private List<Session> sessions = Collections.synchronizedList(new ArrayList<Session>());

    private static class Holder {
        static final SessionsService INSTANCE = new SessionsService();
    }

    public static SessionsService getInstance() {
        return Holder.INSTANCE;
    }

    private Session getSession(String userName) {
        for (Session elem : sessions) {
            if (elem.account.userName.equals(userName)) {
                return elem;
            }
        }
        return null;
    }

    public boolean add(String userName, String token) {
        remove(userName);
        return sessions.add(new Session(new User(userName,"",0), token));
    }

    public boolean remove(String userName){
        if(exists(userName)) {
            Session s = getSession(userName);
            if(s != null) {
                sessions.remove(s);
                return true;
            }
        }
        return false;
    }

    public boolean exists(String userName) {
        for (Session elem : sessions) {
            if (elem.account.userName.equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidToken(String token) {
        for (Session session : sessions) {
            if (session.token.equals(token)) {
                return true;
            }
        }
        return false;
    }
}
