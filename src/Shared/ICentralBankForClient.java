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
    /**
     * Method to create an bank account on a specific bank
     * @param bankName The bank the account is made for
     * @param hashedPassword The password from the account
     * @param firstName The first name of the account holder
     * @param lastName The last name of the account holder
     * @param postalCode The postal code of the account holder
     * @param houseNumber The house number of the account holder
     * @param dateOfBirth The date of birth of the account holder
     * @param email The email address of the account holder
     * @throws RemoteException for exception with RMI
     */
    void createBankAccount(String bankName, String hashedPassword, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException;

    /**
     * Method to login the admin
     * @param hashedPassword The password of the admin account
     * @return If login was succeeded or not
     * @throws RemoteException for exception with RMI
     */
    boolean loginAdmin(String hashedPassword) throws RemoteException;

    /**
     * Method to login the client
     * @param iban Iban number from the clients account
     * @param hashedPassword Password from the clients account
     * @return The session from the client he just logged in to
     * @throws RemoteException for exception with RMI
     */
    Session loginClient(String iban, String hashedPassword) throws RemoteException;

    /**
     * Method to logout the admin
     * @throws RemoteException for exception with RMI
     */
    void logOutAdmin() throws RemoteException;

    /**
     * Method to logout the client
     * @param session The session from the client he wants to logout to
     * @throws RemoteException for exception with RMI
     */
    void logOutClient(Session session) throws RemoteException;

    /**
     * Method to create a bank as an admin
     * @param name The name of the bank
     * @param shortcut The shortcut of the bank
     * @throws RemoteException for exception with RMI
     */
    void createBank(String name, String shortcut) throws RemoteException;

    /**
     * Method to delete an existing bank
     * @param bank The bank to be deleted
     * @throws RemoteException for exception with RMI
     */
    void deleteBank(Bank bank) throws RemoteException;
}
