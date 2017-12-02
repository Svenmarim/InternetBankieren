package BankServer;

import Shared.IBankForClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class Session extends UnicastRemoteObject implements IBankForClient {
    @Override
    public boolean isSessionValid() throws RemoteException {
        return false;
    }

    @Override
    public void editBankAccount(String password, String passwordRepeat, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException {

    }

    @Override
    public void editBankAccountsLimits(double limitIn, double limitOut) throws RemoteException {

    }

    private void addBankAccountsAddress(String name, String iban) throws RemoteException {

    }

    @Override
    public void deleteBankAccountsAddress(Address address) throws RemoteException {

    }

    @Override
    public boolean makeBankAccountsTransaction(double amount, String name, String ibanReceiver, String description) throws RemoteException {
        return false;
    }

    @Override
    public boolean makeBankAccountsRequest(double amount, String name, String ibanReceiver, String description) throws RemoteException {
        return false;
    }

    @Override
    public boolean receiveBankAccountsTransaction(Transaction transaction) throws RemoteException {
        return false;
    }

    @Override
    public void changeAmountBankAccount(double amount) throws RemoteException {

    }
}
