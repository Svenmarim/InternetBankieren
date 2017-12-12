package CentralBankServer;

import BankServer.Bank;
import BankServer.Session;
import Shared.Transaction;
import Shared.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class CentralBank extends UnicastRemoteObject implements ICentralBankForBank, ICentralBankForClient {
    private IBankForCentralBank bank;

    public static void main(String[] args) throws RemoteException {
        //Print port number for registry
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("CentralBank: IP Address: " + localhost.getHostAddress());

        try {
            CentralBank centralBank = new CentralBank();
            System.out.println("CentralBank: CentralBank created");
        } catch (RemoteException e) {
            System.out.println("CentralBank: Cannot create CentralBank");
            System.out.println("CentralBank: RemoteException: " + e.getMessage());
        }
    }

    public CentralBank() throws RemoteException {
        //Create registry at port number
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(1099);
            System.out.println("CentralBank: Registry created");
        } catch (RemoteException e) {
            System.out.println("CentralBank: Cannot create registry");
            System.out.println("CentralBank: RemoteException: " + e.getMessage());
        }

        //Bind using registry
        try {
            registry.rebind("CentralBank", this);
            System.out.println("CentralBank: CentralBank bound to registry");
        } catch (RemoteException e) {
            System.out.println("CentralBank: Cannot bind CentralBank");
            System.out.println("CentralBank: RemoteException: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("CentralBank: Port already in use. \nCentralBank: Please check if the server isn't already running");
            System.out.println("CentralBank: NullPointerException: " + e.getMessage());
        }
    }

    @Override
    public void createBankAccount(String bankName, String hashedPassword, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException {

    }

    @Override
    public boolean loginAdmin(String hashedPassword) throws RemoteException {
        return false;
    }

    @Override
    public Session loginClient(String iban, String hashedPassword) throws RemoteException {
        return null;
    }

    @Override
    public void logOutAdmin() throws RemoteException {

    }

    @Override
    public void logOutClient(Session session) throws RemoteException {

    }

    @Override
    public void createBank(String name, String shortcut) throws RemoteException {

    }

    @Override
    public void deleteBank(Bank bank) throws RemoteException {

    }

    @Override
    public boolean transaction(String iban, String name, Transaction transaction) throws RemoteException {
        return false;
    }
}
