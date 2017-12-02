package Shared;

import java.beans.PropertyChangeEvent;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface IRemotePropertyListener extends Remote {
    void propertyChange(PropertyChangeEvent event) throws RemoteException;
}
