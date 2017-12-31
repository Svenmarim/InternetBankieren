package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface IBankForClient extends Remote {
    /**
     * Method for getting the addressbook
     * @return The addressbook
     * @throws RemoteException for exception with RMI
     */
    List<Address> getAddressbook() throws RemoteException;

    /**
     * Method for getting the transaction history
     * @return The transaction history
     * @throws RemoteException for exception with RMI
     */
    List<Transaction> getTransactionHistory() throws RemoteException;

    /**
     * Method for getting the the limit of money for transferring to people inside the addressbook
     * @return The limit of money for transferring to people inside the addressbook
     * @throws RemoteException for exception with RMI
     */
    Double getLimitIn() throws RemoteException;

    /**
     * Method for getting the limit of money for transferring to people outside the addressbook
     * @return The limit of money for transferring to people outside the addressbook
     * @throws RemoteException for exception with RMI
     */
    Double getLimitOut() throws RemoteException;

    /**
     * Method for getting the iban of the bank account in that session
     * @return The iban number
     * @throws RemoteException for exception with RMI
     */
    String getIban() throws RemoteException;

    /**
     * Method for getting the account details
     * @return The account details
     * @throws RemoteException for exception with RMI
     */
    TempAccount getAccountDetails() throws RemoteException;

    /**
     * Method to check if session is still valid
     * @return If session is still valid or not
     * @throws RemoteException for exception with RMI
     */
    boolean isSessionValid() throws RemoteException;

    /**
     * Method to change account details
     * @param encryptedPassword Encrypted password of account holder
     * @param firstName First name of account holder
     * @param lastName Last name of account holder
     * @param postalCode Postal code of account holder
     * @param houseNumber House number of account holder
     * @param dateOfBirth Date of birth of account holder
     * @param email Email of account holder
     * @throws RemoteException for exception with RMI
     */
    void editBankAccount(String encryptedPassword, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException;

    /**
     * Method to delete own bank account
     */
    void deleteBankAccount() throws RemoteException;

    /**
     * Method for changing the limits of an bank account
     * @param limitIn The limit of transfering money inside your addressbook
     * @param limitOut The limit of transfering money outside your addressbook
     * @throws RemoteException for exception with RMI
     */
    void editBankAccountsLimits(double limitIn, double limitOut) throws RemoteException;

    /**
     * Method for deleting addresses from your addressbook
     * @param address Given address to delete
     * @throws RemoteException for exception with RMI
     */
    void deleteBankAccountsAddress(Address address) throws RemoteException;

    /**
     * Method for making a transfer from client to session
     * @param amount The amount to transfer
     * @param name The name of the receiver
     * @param ibanReceiver The iban of the receiver
     * @param description The description of the transfer
     * @param addToAddress If the current account holder wants to add the receiver to addressbook or not
     * @return If transfer is succeeded or not
     * @throws RemoteException for exception with RMI
     */
    boolean makeBankAccountsTransaction(double amount, String name, String ibanReceiver, String description, boolean addToAddress) throws RemoteException;

    /**
     * Method for making a request to get money
     * @param amount The amount you want to get
     * @param name The name of the other account to get the money from
     * @param ibanReceiver The iban number of the other account to get the money from
     * @param description The description why you get the money
     * @return If the request is succeeded or not
     * @throws RemoteException for exception with RMI
     */
    boolean makeBankAccountsRequest(double amount, String name, String ibanReceiver, String description, boolean addToAddress) throws RemoteException;

    /**
     * Method for receiving an tranfer from some other account
     * @param transaction transaction history details
     * @return If received transfer correctly or not
     * @throws RemoteException for exception with RMI
     */
    void receiveBankAccountsTransaction(Transaction transaction) throws RemoteException;
}
