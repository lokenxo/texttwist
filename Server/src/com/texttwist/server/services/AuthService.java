package com.texttwist.server.services;

import com.texttwist.server.Server;
import constants.Config;
import interfaces.IAuth;
import interfaces.INotificationClient;
import models.Response;
import org.json.simple.JsonObject;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;

import static com.texttwist.server.Server.notificationServer;

/**
 * Author:      Lorenzo Iovino on 15/06/2017.
 * Description: AuthService
 */
public class AuthService extends UnicastRemoteObject implements IAuth {

    private SecureRandom random = new SecureRandom();

    public AuthService() throws RemoteException{
        Server.logger.write("AuthService Service running at "+ Config.AuthServerPort+" port...");
    }

    @Override
    public Response register(String userName, String password) throws RemoteException {
        Server.logger.write("Invoked register with username=" + userName + " AND " + " password=" + password);
        if ((userName != null && !userName.isEmpty()) && (password != null && !password.equals(""))) {
            if(AccountsService.getInstance().register(userName, password)){
                Server.logger.write("Registration successfull");
                return new Response("Registration successfull", 200, null);
            } else {
                Server.logger.write("Registration unsuccessfull");
                return new Response("<html><center>Registration unsuccessfull: <br/> Username exist!</center></html>", 400, null);
            }
        }
        return new Response("<html><center>Registration unsuccessfull! <br/> All fields are mandatories</center></html>", 400, null);
    }

    @Override
    public Response login(String userName, String password) throws RemoteException {
        Server.logger.write("Invoked login with username=" + userName + " AND " + " password=" + password);
        if ((userName != null && !userName.isEmpty()) && (password != null && !password.equals(""))) {
            if(AccountsService.getInstance().exists(userName) && AccountsService.getInstance().checkPassword(userName, password)) {
                JsonObject data = new JsonObject();
                String token = nextSessionId();
                data.put("token", token);
                SessionsService.getInstance().add(userName,token);
                Server.logger.write("Login successfull");
                return new Response("Login successfull", 200, data);
            }
        }
        Server.logger.write("Login unsuccessfull");
        return new Response("Login unsuccessfull", 400, null);
    }

    @Override
    public Response logout(String userName, String token, INotificationClient stub) throws RemoteException {
        Server.logger.write("Invoked logout with username=" + userName + " AND " + " token=" + token);
        notificationServer.unregisterForCallback(stub);

        if ((userName != null && !userName.isEmpty()) && (token != null && !token.isEmpty())) {
            boolean res = SessionsService.getInstance().remove(userName);
            if(res) {
                Server.logger.write("Logout successfull");

            }
        }

        SessionsService.getInstance().remove(userName);
        Server.logger.write("Logout successfull (but something gone wrong)");
        return new Response("Logout successfull (but something gone wrong)", 200, null);
    }

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }

}
