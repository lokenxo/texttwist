package com.texttwist.server.components.auth;
import interfaces.ITTAuth;
import models.TTResponse;
import org.json.simple.JsonObject;
import utilities.TTLogger;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;

/**
 * Created by loke on 15/06/2017.
 */
public class TTAuth extends UnicastRemoteObject implements ITTAuth {

    private SecureRandom random = new SecureRandom();


    public TTAuth() throws RemoteException{
    }

    @Override
    public TTResponse register(String userName, String password) throws RemoteException {
        TTLogger.write("LOGGER: Invoked register with username=" + userName + " AND " + " password=" + password);
        if ((userName != null && !userName.isEmpty()) && (password != null && !password.equals(""))) {
            boolean res = true;//TODO Inserisci nel db e verifica se c'è un utente con lo stesso nome

                if(userName.equals("ciro")){ //TODO rimuovere
                    res = false;
                }
            if(res) {
                TTLogger.write("LOGGER: Registration successfull");
                return new TTResponse("Registration successfull", 200, null);
            } else {
                TTLogger.write("LOGGER: Registration unsuccessfull");
                return new TTResponse("Registration unsuccessfull: Username exist!", 400, null);
            }
        }
        return new TTResponse("Registration unsuccessfull! All fields are mandatories", 400, null);

    }

    @Override
    public TTResponse login(String userName, String password) throws RemoteException {
        TTLogger.write("LOGGER: Invoked login with username=" + userName + " AND " + " password=" + password);
        if ((userName != null && !userName.isEmpty()) && (password != null && !password.equals(""))) {

            if((userName.equalsIgnoreCase("admin"))
                    && (password.equals("admin"))) {
                JsonObject data = new JsonObject();
                data.put("token", nextSessionId());
                TTLogger.write("LOGGER: Login successfull");
                return new TTResponse("Login successfull", 200, data);
            }
        }
        return new TTResponse("Login unsuccessfull", 400, null);
    }


    @Override
    public TTResponse logout(String userName, String token) throws RemoteException {
        TTLogger.write("LOGGER: Invoked logout with username=" + userName + " AND " + " token=" + token);
        if ((userName != null && !userName.isEmpty()) && (token != null && !token.isEmpty())) {
            return new TTResponse("Logout successfull", 200, null);
        }
        return new TTResponse("Logout successfull (but something gone wrong)", 200, null);
    }

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }

}
