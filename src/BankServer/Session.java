package BankServer;

import Shared.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class Session extends UnicastRemoteObject implements IBankForClient {
    private Date lastActivity;
    private BankAccount bankAccount;
    private ICentralBankForBank centralBank;

    @Override
    public Date getLastActivity() {
        return lastActivity;
    }

    public Session(double amount, String iban, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email, double limitIn, double limitOut) throws RemoteException {
        getCentralBank();
        bankAccount = new BankAccount(amount, iban, firstName, lastName, postalCode, houseNumber, dateOfBirth, email, limitIn, limitOut);
        lastActivity = new Date();
    }

    @Override
    public boolean isSessionValid() {
        //Check if sessions last activity is longer then 5 minutes ago
        return false;
    }

    @Override
    public void editBankAccount(String hashedPassword, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) {
        //Edit account in database
        bankAccount.editAccount(firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
    }

    @Override
    public void editBankAccountsLimits(double limitIn, double limitOut) {
        //Edit limits in database
        bankAccount.editLimits(limitIn, limitOut);
    }

    @Override
    public void deleteBankAccountsAddress(Address address) {
        //Delete bank accounts address in database
        bankAccount.deleteAddress(address);
    }

    @Override
    public boolean makeBankAccountsTransaction(double amount, String name, String ibanReceiver, String description, boolean addToAddress) throws RemoteException {
        //Call central bank transaction method
        //if true then continue
        bankAccount.makeTransaction(amount, name, ibanReceiver, description, addToAddress);
        return true;
    }

    @Override
    public boolean makeBankAccountsRequest(double amount, String name, String ibanReceiver, String description) {
        return false;
    }

    @Override
    public void receiveBankAccountsTransaction(Transaction transaction) {
        bankAccount.receiveTransaction(transaction);
    }

    @Override
    public void changeAmountBankAccount(double amount) {
        bankAccount.changeAmount(amount);
    }

    public void getCentralBank() {
        // Locate registry at IP address and port number
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry("192.168.42.1", 1099);
        } catch (RemoteException ex) {
            System.out.println("Session: Cannot locate registry");
            System.out.println("Session: RemoteException: " + ex.getMessage());
            registry = null;
        }

        // Print result locating registry
        if (registry != null) {
            System.out.println("Session: Registry located");
        } else {
            System.out.println("Session: Cannot locate registry");
            System.out.println("Session: Registry is null pointer");
        }

        //Bind using registry
        if (registry != null) {
            try {
                centralBank = (ICentralBankForBank) registry.lookup("CentralBank");
            } catch (RemoteException e) {
                System.out.println("Session: RemoteException: " + e.getMessage());
                centralBank = null;
            } catch (NotBoundException e) {
                System.out.println("Session: NotBoundException: " + e.getMessage());
                centralBank = null;
            }
        }
    }
}
