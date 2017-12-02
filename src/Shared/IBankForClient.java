package Shared;

import BankServer.Address;
import BankServer.Transaction;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface IBankForClient extends Remote {
    boolean isSessionValid() throws RemoteException;

    void editBankAccount(String password, String passwordRepeat, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException;

    void editBankAccountsLimits(double limitIn, double limitOut) throws RemoteException;

    void deleteBankAccountsAddress(Address address) throws RemoteException;

    boolean makeBankAccountsTransaction(double amount, String name, String ibanReceiver, String description) throws RemoteException;

    boolean makeBankAccountsRequest(double amount, String name, String ibanReceiver, String description) throws RemoteException;

    boolean receiveBankAccountsTransaction(Transaction transaction) throws RemoteException;

    void changeAmountBankAccount(double amount) throws RemoteException;
}
