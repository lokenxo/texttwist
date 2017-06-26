package com.texttwist.server.components;
import models.Account;
import models.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by loke on 17/06/2017.
 */
public class SessionsManager {

    private List<Session> sessions = Collections.synchronizedList(new ArrayList<Session>());

    private static class Holder {
        static final SessionsManager INSTANCE = new SessionsManager();
    }

    public static SessionsManager getInstance() {
        return Holder.INSTANCE;
    }

    private SessionsManager(){}

    public boolean add(String userName, String token) {
        remove(userName);
        return sessions.add(new Session(userName, token));
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

    public Session getSession(String userName) {
        synchronized(sessions) {
            Iterator<Session> i = sessions.iterator();
            while (i.hasNext()) {
                Session elem = i.next();
                if (elem.userName.equals(userName)) {
                    return elem;
                }
            }
            return null;
        }
    }

    public boolean exists(String userName) {
        synchronized(sessions) {
            Iterator<Session> i = sessions.iterator();
            while (i.hasNext()) {
                Session elem = i.next();
                if (elem.userName.equals(userName)) {
                    return true;
                }
            }
            return false;
        }
    }


    public boolean isValidToken(String token) {
        synchronized(sessions) {
            Iterator<Session> i = sessions.iterator();
            while (i.hasNext()) {
                if (i.next().token.equals(token)) {
                    return true;
                }
            }
            return false;
        }
    }

}
