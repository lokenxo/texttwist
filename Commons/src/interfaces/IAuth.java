package interfaces;
import models.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by loke on 15/06/2017.
 */
public interface IAuth extends Remote {
    Response login(String userName, String password) throws RemoteException;
    Response register(String userName, String password) throws RemoteException;
    Response logout(String userName, String token) throws RemoteException;

}
