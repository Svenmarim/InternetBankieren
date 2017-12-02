package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface IRemotePublisherForListener extends Remote {
    void subscribeRemoteListener(IRemotePropertyListener listener, String property) throws RemoteException;

    void unsubscribeRemoteListener(IRemotePropertyListener listener, String property) throws RemoteException;
}
