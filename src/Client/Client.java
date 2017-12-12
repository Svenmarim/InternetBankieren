package Client;

import Shared.Address;
import BankServer.Bank;
import Shared.*;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 2-12-2017
 */
public class Client extends UnicastRemoteObject implements IRemotePropertyListener {
    private boolean admin;
    private ICentralBankForClient centralBank;
    private IBankForClient session;
    private IRemotePublisherForListener publisherForListener;
    private IRemotePublisherForDomain publisherForDomain;

    public Client() throws RemoteException {

    }

    public void login(String iban, String password){

    }

    public void logOut(){

    }

    public void createBank(String name, String shortcut){

    }

    public void deleteBank(Bank bank){

    }

    public void isSessionValid(){

    }

    public void createBankAccount(String password, String passwordRepeat, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email){

    }

    public void editBankAccount(String password, String passwordRepeat, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email){

    }

    private String hashPassword(String password){
        return null;
    }

    public void editBankAccountsLimits(double limitIn, double limitOut){

    }

    public void deleteBankAccountsAddress(Address address){

    }

    public void makeBankAccountsTransaction(double amount, String name, String ibanReceiver, String description, boolean addToAddress){

    }

    public void makeBankAccountsRequest(double amount, String name, String ibanReceiver, String description){

    }

    public void receiveBankAccountsTransaction(Transaction transaction){

    }

    @Override
    public void propertyChange(PropertyChangeEvent event) throws RemoteException {

    }
}
