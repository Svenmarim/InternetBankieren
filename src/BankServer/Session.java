package BankServer;

import Shared.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class Session extends UnicastRemoteObject implements IBankForClient {
    private Date lastActivity;
    private BankAccount bankAccount;
    private ICentralBankForSession centralBank;

    @Override
    public Date getLastActivity() {
        return this.lastActivity;
    }

    @Override
    public List<Address> getAddressbook() {
        return this.bankAccount.getAddressbook();
    }

    @Override
    public List<Transaction> getTransactionHistory() {
        return this.bankAccount.getTransactionHistory();
    }

    public Session(BankAccount bankAccount) throws RemoteException {
        this.bankAccount = bankAccount;
        this.lastActivity = new Date();
        getCentralBank();
    }

    @Override
    public boolean isSessionValid() {
        //Returns false if difference is MORE then 5 minutes
        return new Date().getTime() - lastActivity.getTime() >= 5*60*1000;
    }

    @Override
    public void editBankAccount(String hashedPassword, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) {
        this.lastActivity = new Date();
        //TODO Edit account in database
        bankAccount.editAccount(firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
    }

    @Override
    public void deleteBankAccount() {
        this.lastActivity = new Date();
        //TODO Delete account in database
        this.bankAccount = null;
    }

    @Override
    public void editBankAccountsLimits(double limitIn, double limitOut) {
        this.lastActivity = new Date();
        //TODO Edit limits in database
        bankAccount.editLimits(limitIn, limitOut);
    }

    @Override
    public void deleteBankAccountsAddress(Address address) {
        this.lastActivity = new Date();
        //TODO Delete bank accounts address in database
        bankAccount.deleteAddress(address);
    }

    @Override
    public boolean makeBankAccountsTransaction(double amount, String nameReceiver, String ibanReceiver, String description, boolean addToAddress) throws RemoteException {
        this.lastActivity = new Date();
        Transaction transaction = new Transaction(new Date(), bankAccount.getIban(), amount, description);
        if (centralBank.transaction(ibanReceiver, nameReceiver, transaction)){
            bankAccount.makeTransaction(amount, nameReceiver, ibanReceiver, description, addToAddress);
            return true;
        }
        return false;
    }

    @Override
    public boolean makeBankAccountsRequest(double amount, String name, String ibanReceiver, String description, boolean addToAddress) {
        this.lastActivity = new Date();
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

    private void getCentralBank() {
        // Locate registry at IP address and port number
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry("localhost", 1234);
            System.out.println("Session: Registry located");
        } catch (RemoteException ex) {
            System.out.println("Session: Cannot locate registry");
            System.out.println("Session: RemoteException: " + ex.getMessage());
            registry = null;
        }

        //Get CentralBank from registry
        if (registry != null) {
            try {
                centralBank = (ICentralBankForSession) registry.lookup("CentralBank");
                System.out.println("Session: CentralBank retrieved");
            } catch (RemoteException e) {
                System.out.println("Session: RemoteException on ICentralBankForSession");
                System.out.println("Session: RemoteException: " + e.getMessage());
            } catch (NotBoundException e) {
                System.out.println("Session: Cannot bind ICentralBankForSession");
                System.out.println("Session: NotBoundException: " + e.getMessage());
            }
        }
    }
}
