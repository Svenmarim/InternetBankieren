package Shared;

import java.beans.PropertyChangeEvent;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface IRemotePropertyListener extends Remote {
    /**
     * Method to signal to client if something has changed
     * @param event The event that is changed
     * @throws RemoteException for exception with RMI
     */
    void propertyChange(PropertyChangeEvent event) throws RemoteException;
}
