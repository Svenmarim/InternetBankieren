package BankServer;

import Shared.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class Bank extends UnicastRemoteObject implements IBankForCentralBank, IRemotePublisherForDomain, IRemotePublisherForListener {
    private String name;
    private String shortcut;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getShortcut() {
        return shortcut;
    }

    public static void main(String[] args) throws RemoteException {

    }

    public Bank(String name, String shortcut) throws RemoteException{
        this.name = name;
        this.shortcut = shortcut;
    }

    @Override
    public Session loginClient(String iban, String hashedPassword) throws RemoteException {
        return null;
    }

    @Override
    public void logOutClient(Session session) throws RemoteException {

    }

    @Override
    public boolean transaction(String iban, String name, Transaction transaction) throws RemoteException {
        return false;
    }

    @Override
    public void registerProperty(String property) throws RemoteException {

    }

    @Override
    public void unregisterProperty(String property) throws RemoteException {

    }

    @Override
    public void inform(String property, Object oldvalue, Object newValue) throws RemoteException {

    }

    @Override
    public List<String> getProperties() throws RemoteException {
        return null;
    }

    @Override
    public void subscribeRemoteListener(IRemotePropertyListener listener, String property) throws RemoteException {

    }

    @Override
    public void unsubscribeRemoteListener(IRemotePropertyListener listener, String property) throws RemoteException {

    }
}
