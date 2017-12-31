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
    private DatabaseBankServer database;

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

    @Override
    public Double getLimitIn() {
        return this.bankAccount.getLimitInAddressbook();
    }

    @Override
    public Double getLimitOut() {
        return this.bankAccount.getLimitOutAddressbook();
    }

    @Override
    public String getIban() {
        return this.bankAccount.getIban();
    }

    @Override
    public TempAccount getAccountDetails() {
        return database.getAccountDetails(bankAccount.getIban());
    }

    public Session(BankAccount bankAccount) throws RemoteException {
        this.bankAccount = bankAccount;
        this.lastActivity = new Date();
        this.database = new DatabaseBankServer();
        getCentralBank();
    }

    @Override
    public boolean isSessionValid() {
        //Returns false if difference is MORE then 5 minutes
        return new Date().getTime() - lastActivity.getTime() >= 5 * 60 * 1000;
    }

    @Override
    public void editBankAccount(String encryptedPassword, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) {
        this.lastActivity = new Date();
        database.editBankAccount(encryptedPassword, firstName, lastName, postalCode, houseNumber, dateOfBirth, email, bankAccount.getIban());
        bankAccount.editAccount(firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
    }

    @Override
    public void deleteBankAccount() {
        this.lastActivity = new Date();
        database.deleteBankAccount(bankAccount.getIban());
        this.bankAccount = null;
    }

    @Override
    public void editBankAccountsLimits(double limitIn, double limitOut) {
        this.lastActivity = new Date();
        database.editBankAccountLimits(limitIn, limitOut, bankAccount.getIban());
        bankAccount.editLimits(limitIn, limitOut);
    }

    @Override
    public void deleteBankAccountsAddress(Address address) {
        this.lastActivity = new Date();
        database.deleteBankAccountAddress(address, bankAccount.getIban());
        bankAccount.deleteAddress(address);
    }

    @Override
    public boolean makeBankAccountsTransaction(double amount, String nameReceiver, String ibanReceiver, String description, boolean addToAddress) throws RemoteException {
        this.lastActivity = new Date();
        for (Address a : getAddressbook()) {
            if (a.getIban().equals(ibanReceiver) && amount <= getLimitIn() && amount <= bankAccount.getAmount()) {
                System.out.println("Transaction activated to contact inside address book");
                return transaction(amount, description, ibanReceiver, nameReceiver, addToAddress);
            }
        }

        if (amount <= getLimitOut() && amount <= bankAccount.getAmount()) {
            System.out.println("Transaction activated to contact outside address book");
            return transaction(amount, description, ibanReceiver, nameReceiver, addToAddress);
        }
        return false;
    }

    private boolean transaction(double amount, String description, String ibanReceiver, String nameReceiver, boolean addToAddress) throws RemoteException {
        Transaction transactionReceiver = new Transaction(new Date(), bankAccount.getIban(), amount, description);
        if (centralBank.transaction(ibanReceiver, transactionReceiver)) {
            amount -= amount + amount;
            Transaction transaction = new Transaction(new Date(), ibanReceiver.toUpperCase(), amount, description);
            bankAccount.makeTransaction(nameReceiver, transaction, addToAddress);
            //Make changes to database
            database.insertTransaction(bankAccount.getIban(), transaction);
            database.updateAmount(bankAccount.getIban(), bankAccount.getAmount());
            if (addToAddress) {
                database.insertAddress(bankAccount.getIban(), new Address(nameReceiver, ibanReceiver.toUpperCase()));
            }
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
        //The updates of values in database, happens in Bank class
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
