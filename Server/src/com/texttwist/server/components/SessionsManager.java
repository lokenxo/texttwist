package com.texttwist.server.components;
import interfaces.INotificationClient;
import models.Session;
import models.User;

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
        return sessions.add(new Session(new User(userName,"",0), token));
    }

    public void printAll(){
            Iterator<Session> i = sessions.iterator();
            while (i.hasNext()) {
                Session elem = i.next();
                System.out.println(elem.account.userName + " | " + elem.token);
            }

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
            Iterator<Session> i = sessions.iterator();
            while (i.hasNext()) {
                Session elem = i.next();
                if (elem.account.userName.equals(userName)) {
                    return elem;
                }
            }
            return null;

    }

    public boolean exists(String userName) {
            Iterator<Session> i = sessions.iterator();
            while (i.hasNext()) {
                Session elem = i.next();
                if (elem.account.userName.equals(userName)) {
                    return true;
                }
            }
            return false;

    }


    public boolean isValidToken(String token) {
            Iterator<Session> i = sessions.iterator();
            while (i.hasNext()) {
                if (i.next().token.equals(token)) {
                    return true;
                }
            }
            return false;

    }

}
