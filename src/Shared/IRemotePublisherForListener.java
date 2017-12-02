package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface IRemotePublisherForListener extends Remote {
    /**
     * Method to subscribe a client to a property
     * @param listener The client who wants to subscribe
     * @param property The property that the client wants to subscribe to
     * @throws RemoteException for exception with RMI
     */
    void subscribeRemoteListener(IRemotePropertyListener listener, String property) throws RemoteException;

    /**
     * Method to unsubscribe a client from a property
     * @param listener The client who wants to unsubscribe
     * @param property The property that the clients wants to unsubscibe from
     * @throws RemoteException for exception with RMI
     */
    void unsubscribeRemoteListener(IRemotePropertyListener listener, String property) throws RemoteException;
}
