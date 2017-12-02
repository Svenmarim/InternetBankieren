package BankServer;

import BankServer.BankAccount;
import Shared.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class Session extends UnicastRemoteObject implements IBankForClient {
    private Date lastActivity;
    private BankAccount bankAccount;

    @Override
    public Date getLastActivity() {
        return lastActivity;
    }

    public Session(double amount, String iban, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email, double limitIn, double limitOut) throws RemoteException {
        bankAccount = new BankAccount(amount, iban, firstName, lastName, postalCode, houseNumber, dateOfBirth, email, limitIn, limitOut);
        lastActivity = new Date();
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        System.out.println(dateFormat.format(lastActivity)); //02/12/2017 12:08:43
    }

    @Override
    public boolean isSessionValid() throws RemoteException {
        return false;
    }

    @Override
    public void editBankAccount(String hashedPassword, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException {

    }

    @Override
    public void editBankAccountsLimits(double limitIn, double limitOut) throws RemoteException {

    }

    @Override
    public void deleteBankAccountsAddress(Address address) throws RemoteException {

    }

    @Override
    public boolean makeBankAccountsTransaction(double amount, String name, String ibanReceiver, String description, boolean addToAddress) throws RemoteException {
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
