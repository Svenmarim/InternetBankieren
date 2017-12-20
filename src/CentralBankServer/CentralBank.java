package CentralBankServer;

import BankServer.Bank;
import Shared.Transaction;
import Shared.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class CentralBank extends UnicastRemoteObject implements ICentralBankForBank, ICentralBankForSession, ICentralBankForClient {
    private DatabaseCentralBank database;
    private List<IBankForCentralBank> banks;
    private boolean adminLoggedIn;

    public static void main(String[] args) {
        //Print ip address and port number for registry
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("CentralBank: IP Address: " + localhost.getHostAddress());
        System.out.println("CentralBank: Port number: 1234");

        try {
            CentralBank centralBank = new CentralBank();
            centralBank.createRegistry();
            System.out.println("CentralBank: CentralBank created");
        } catch (RemoteException e) {
            System.out.println("CentralBank: Cannot create CentralBank");
            System.out.println("CentralBank: RemoteException: " + e.getMessage());
        }
    }

    public CentralBank() throws RemoteException {
        this.banks = new ArrayList<>();
        this.database = new DatabaseCentralBank();
    }

    @Override
    public String createBankAccount(String bankName, String hashedPassword, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException {

        return null; //iban
    }

    @Override
    public boolean loginAdmin(String hashedPassword) {
        if(!adminLoggedIn){
            if (database.loginAdmin(hashedPassword)){
                this.adminLoggedIn = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public IBankForClient loginClient(String iban, String hashedPassword) throws RemoteException {
        String bankShortcut = iban.substring(4, 8).toUpperCase();
        for(IBankForCentralBank bank : banks){
            if(bank.getShortcut().equals(bankShortcut)){
                return bank.loginClient(iban, hashedPassword);
            }
        }
        return null;
    }

    @Override
    public void logOutAdmin() {
        this.adminLoggedIn = false;
    }

    @Override
    public void logOutClient(IBankForClient session) throws RemoteException {
        for(IBankForCentralBank bank : banks){
            if(bank.getSessions().contains(session)){
                bank.logOutClient(session);
            }
        }
    }

    @Override
    public boolean createBank(String name, String shortcut) throws RemoteException {
        return false;
    }

    @Override
    public void deleteBank(Bank bank) throws RemoteException {

    }

    @Override
    public void startUpBank(IBankForCentralBank bank) throws RemoteException{
        this.banks.add(bank);
    }

    @Override
    public boolean transaction(String iban, String name, Transaction transaction) throws RemoteException {
        return false;
    }

    private void createRegistry(){
        //Create registry at port number
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(1234);
            System.out.println("CentralBank: Registry created");
        } catch (RemoteException e) {
            System.out.println("CentralBank: Cannot create registry");
            System.out.println("CentralBank: RemoteException: " + e.getMessage());
            System.exit(0);
        }

        //Bind using registry
        try {
            registry.rebind("CentralBank", this);
            System.out.println("CentralBank: CentralBank bound to registry");
        } catch (RemoteException e) {
            System.out.println("CentralBank: Cannot bind CentralBank");
            System.out.println("CentralBank: RemoteException: " + e.getMessage());
            System.exit(0);
        } catch (NullPointerException e) {
            System.out.println("CentralBank: Port already in use. \nCentralBank: Please check if the server isn't already running");
            System.out.println("CentralBank: NullPointerException: " + e.getMessage());
            System.exit(0);
        }
    }
}
