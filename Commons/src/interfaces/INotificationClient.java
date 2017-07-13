package interfaces;

import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Author:      Lorenzo Iovino on 19/06/2017.
 * Description: INotificationClient
 */
public interface INotificationClient extends Remote{
        void sendInvite(String userName, DefaultListModel<String> users) throws RemoteException;
}
