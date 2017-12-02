package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface ICentralBankForBank extends Remote {
    /**
     * Method to send the transfer from session to central bank
     * @param iban Iban number from receiver
     * @param name Name of the receiver
     * @param transaction Transfer details for transaction history
     * @return If transfer was succeeded or not
     * @throws RemoteException for exception with RMI
     */
    boolean transaction(String iban, String name, Transaction transaction) throws RemoteException;
}
