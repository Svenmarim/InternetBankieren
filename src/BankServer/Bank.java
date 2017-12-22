package BankServer;

import Shared.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class Bank extends UnicastRemoteObject implements IBankForCentralBank, IRemotePublisherForDomain, IRemotePublisherForListener {
    private String name;
    private String shortcut;
    private List<IBankForClient> sessions;
    private DatabaseBankServer database;
    private IRemotePropertyListener client;
    private ICentralBankForBank centralBank;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getShortcut() {
        return shortcut;
    }

    @Override
    public List<IBankForClient> getSessions() {
        return Collections.unmodifiableList(sessions);
    }

    public Bank(String name, String shortcut) throws RemoteException {
        this.name = name;
        this.shortcut = shortcut;
        this.sessions = new ArrayList<>();
        this.database = new DatabaseBankServer();
    }

    @Override
    public IBankForClient loginClient(String iban, String hashedPassword) throws RemoteException {
        BankAccount bankAccount = database.login(iban.toUpperCase(), hashedPassword, shortcut);
        if (bankAccount != null) {
            Session session = new Session(bankAccount);
            sessions.add(session);
            return session;
        }
        return null;
    }

    @Override
    public void logOutClient(IBankForClient session) throws RemoteException {
        sessions.remove(session);
    }

    @Override
    public boolean transaction(String iban, String name, Transaction transaction) throws RemoteException {
        return false;
    }

    @Override
    public void registerProperty(String property) throws RemoteException {

    }

    @Override
    public void unregisterProperty(String property) throws RemoteException {

    }

    @Override
    public void inform(String property, Object oldvalue, Object newValue) throws RemoteException {

    }

    @Override
    public List<String> getProperties() throws RemoteException {
        return null;
    }

    @Override
    public void subscribeRemoteListener(IRemotePropertyListener listener, String property) throws RemoteException {

    }

    @Override
    public void unsubscribeRemoteListener(IRemotePropertyListener listener, String property) throws RemoteException {

    }

    public void bindBankInRegistry() {
        //Create registry at port number
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry("localhost", 1234);
            System.out.println("Bank: Registry located");
        } catch (RemoteException e1) {
            System.out.println("Bank: Cannot locate registry");
            System.exit(0);
        }

        //Bind using registry
        try {
            registry.rebind(name, this);
            System.out.println("Bank: Bank bound to registry");
            centralBank.startUpBank(this);
            System.out.println("Bank: Bank added in central bank");
            if (database.setBankOnline(this)){
                System.out.println("Bank: Bank online in database");
            }
        } catch (RemoteException e) {
            System.out.println("Bank: Cannot bind Bank");
            System.out.println("Bank: RemoteException: " + e.getMessage());
            System.exit(0);
        } catch (NullPointerException e) {
            System.out.println("Bank: Port already in use");
            System.out.println("Bank: NullPointerException: " + e.getMessage());
            System.exit(0);
        }
    }

    public void getCentralBank() {
        // Locate registry at IP address and port number
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry("localhost", 1234);
            System.out.println("Bank: Registry located");
        } catch (RemoteException ex) {
            System.out.println("Bank: Cannot locate registry");
            System.out.println("Bank: RemoteException: " + ex.getMessage());
            System.exit(0);
        }

        //Get CentralBank from registry
        if (registry != null) {
            try {
                centralBank = (ICentralBankForBank) registry.lookup("CentralBank");
                System.out.println("Bank: CentralBank retrieved");
            } catch (RemoteException e) {
                System.out.println("Bank: RemoteException on ICentralBankForBank");
                System.out.println("Bank: RemoteException: " + e.getMessage());
                System.exit(0);
            } catch (NotBoundException e) {
                System.out.println("Bank: Cannot bind ICentralBankForBank");
                System.out.println("Bank: NotBoundException: " + e.getMessage());
                System.exit(0);
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
