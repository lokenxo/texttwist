package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by loke on 19/06/2017.
 */
public interface INotificationServer extends Remote {
    /* registrazione per la callback */
    public void registerForCallback (INotificationClient ClientInterface) throws RemoteException;

    /* cancella registrazione per la callback */
    public void  unregisterForCallback (INotificationClient ClientInterface) throws RemoteException;

}
