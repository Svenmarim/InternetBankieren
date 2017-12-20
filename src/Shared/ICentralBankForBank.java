package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public interface ICentralBankForBank extends Remote{
    /**
     * Method to set the bank in the list from banks in central bank
     * @param bank the bank that needs to be added to the list
     * @throws RemoteException for exception with RMI
     */
    void startUpBank(IBankForCentralBank bank) throws RemoteException;
}
