package Shared;

import BankServer.Transaction;

import java.rmi.Remote;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public interface ICentralBankForBank extends Remote {
    boolean transaction(String iban, String name, Transaction transaction);
}
