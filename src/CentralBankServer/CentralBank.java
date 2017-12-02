package CentralBankServer;

import BankServer.Bank;
import BankServer.Session;
import Shared.Transaction;
import Shared.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class CentralBank extends UnicastRemoteObject implements ICentralBankForBank, ICentralBankForClient {
    private IBankForCentralBank bank;

    public static void main(String[] args) throws RemoteException {

    }

    public CentralBank() throws RemoteException {

    }

    @Override
    public void createBankAccount(String bankName, String hashedPassword, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException {

    }

    @Override
    public boolean loginAdmin(String hashedPassword) throws RemoteException {
        return false;
    }

    @Override
    public Session loginClient(String iban, String hashedPassword) throws RemoteException {
        return null;
    }

    @Override
    public void logOutAdmin() throws RemoteException {

    }

    @Override
    public void logOutClient(Session session) throws RemoteException {

    }

    @Override
    public void createBank(String name, String shortcut) throws RemoteException {

    }

    @Override
    public void deleteBank(Bank bank) throws RemoteException {

    }

    @Override
    public boolean transaction(String iban, String name, Transaction transaction) throws RemoteException {
        return false;
    }
}
