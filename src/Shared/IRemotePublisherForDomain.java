package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface IRemotePublisherForDomain extends Remote {
    /**
     * Method to register as client on a property
     * @param property The property the client wants to register to
     * @throws RemoteException for exception with RMI
     */
    void registerProperty(String property) throws RemoteException;

    /**
     * Method to unregister as client on a property
     * @param property The property the clients wants to unregister to
     * @throws RemoteException for exception with RMI
     */
    void unregisterProperty(String property) throws RemoteException;

    /**
     * Method to inform client something has changed
     * @param property The property that has changed
     * @param oldvalue The old value of that property
     * @param newValue The new value of that property
     * @throws RemoteException for exception with RMI
     */
    void inform(String property, Object oldvalue, Object newValue) throws RemoteException;

    /**
     * Method to get all the properties
     * @return List with all the properties
     * @throws RemoteException for exception with RMI
     */
    List<String> getProperties() throws RemoteException;
}
