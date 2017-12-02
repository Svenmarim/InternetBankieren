package BankServer;

import Shared.IBankForCentralBank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class Bank extends UnicastRemoteObject implements IBankForCentralBank {
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
}
