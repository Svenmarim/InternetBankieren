package CentralBankServer;

import Shared.ICentralBankForBank;
import Shared.ICentralBankForClient;

import java.rmi.server.UnicastRemoteObject;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class CentralBank extends UnicastRemoteObject implements ICentralBankForBank, ICentralBankForClient {
}
