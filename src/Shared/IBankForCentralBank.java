package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface IBankForCentralBank extends Remote {
    /**
     * Method for getting the name of the bank
     * @return The name of the bank
     * @throws RemoteException for exception with RMI
     */
    String getName() throws RemoteException;

    /**
     * Method for getting the shortcut of the bank
     * @return The shortcut of the bank
     * @throws RemoteException for exception with RMI
     */
    String getShortcut() throws RemoteException;

    /**
     * Method for getting all the sessions of that bank
     * @return The list of sessions of that bank
     * @throws RemoteException for exception with RMI
     */
    List<IBankForClient> getSessions() throws RemoteException;

    /**
     * Method to log the client in on the app
     * @param iban Identical bank account number
     * @param hashedPassword The password for the clients account but then hashed
     * @return Current session for client to work with
     * @throws RemoteException for exception with RMI
     */
    IBankForClient loginClient(String iban, String hashedPassword) throws RemoteException;

    /**
     * Method for logging out the client
     * @param session The session to log out with
     * @throws RemoteException for exception with RMI
     */
    void logOutClient(IBankForClient session) throws RemoteException;

    /**
     * Method to send the transaction from central bank to bank
     * @param iban Identical account number for receiver
     * @param transaction transaction history to be added to bank account receiver
     * @return if transaction was succeeded
     * @throws RemoteException for exception with RMI
     */
    boolean transaction(String iban, Transaction transaction) throws RemoteException;
}
