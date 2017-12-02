package Shared;

import BankServer.Bank;
import BankServer.Session;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface ICentralBankForClient extends Remote {
    void createBankAccount(String bankName, String hashedPassword, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException;

    boolean loginAdmin(String hashedPassword) throws RemoteException;

    Session loginClient(String iban, String hashedPassword) throws RemoteException;

    void logOutAdmin() throws RemoteException;

    void logOutClient(Session session) throws RemoteException;

    void createBank(String name, String shortcut) throws RemoteException;

    void deleteBank(Bank bank) throws RemoteException;
}
