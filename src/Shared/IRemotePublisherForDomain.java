package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface IRemotePublisherForDomain extends Remote {
    void registerProperty(String property) throws RemoteException;

    void unregisterProperty(String property) throws RemoteException;

    void inform(String property, Object oldvalue, Object newValue) throws RemoteException;

    List<String> getProperties() throws RemoteException;
}
