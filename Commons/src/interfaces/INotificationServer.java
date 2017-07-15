package interfaces;

import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Author:      Lorenzo Iovino on 19/06/2017.
 * Description: Main
 */
public interface INotificationServer extends Remote {
    void registerForCallback (INotificationClient ClientInterface) throws RemoteException;
    void unregisterForCallback (INotificationClient ClientInterface) throws RemoteException;
    void sendInvitations(String username, DefaultListModel<String> users) throws RemoteException;
}
