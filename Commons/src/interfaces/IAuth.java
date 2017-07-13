package interfaces;

import models.Response;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Author:      Lorenzo Iovino on 15/06/2017.
 * Description: IAuth
 */
public interface IAuth extends Remote {
    Response login(String userName, String password) throws RemoteException;
    Response register(String userName, String password) throws RemoteException;
    Response logout(String userName, String token, INotificationClient stub) throws RemoteException;
}
