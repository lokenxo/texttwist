package interfaces;
import models.TTResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by loke on 15/06/2017.
 */
public interface ITTAuth extends Remote {
    TTResponse login(String userName, String password) throws RemoteException;
    TTResponse register(String userName, String password) throws RemoteException;
    TTResponse logout(String userName, String token) throws RemoteException;

}
