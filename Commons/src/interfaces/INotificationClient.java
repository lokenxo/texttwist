package interfaces;

import models.Response;

import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by loke on 19/06/2017.
 */
public interface INotificationClient extends Remote{

        Response sendInvite(String userName, DefaultListModel<String> users) throws RemoteException;
}
