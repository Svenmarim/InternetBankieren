package Shared;

import BankServer.Session;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface IBankForCentralBank extends Remote {
    Session loginClient(String iban, String hashedPassword) throws RemoteException;

    void logOutClient(Session session) throws RemoteException;

    boolean transaction(String iban, String name, Transaction transaction) throws RemoteException;
}
