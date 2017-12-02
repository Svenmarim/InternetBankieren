package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface ICentralBankForBank extends Remote {
    boolean transaction(String iban, String name, Transaction transaction) throws RemoteException;
}
