package BankServer;

import Shared.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getShortcut() {
        return shortcut;
    }

    public Bank(String name, String shortcut) throws RemoteException {
        this.name = name;
        this.shortcut = shortcut;
        this.sessions = new ArrayList<>();
        this.database = new DatabaseBankServer();
    }

    @Override
    public IBankForClient loginClient(String iban, String hashedPassword) throws RemoteException {
        //TODO get bank account details from database
//        return new Session();
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

    public void createRegistry() {
        //Create registry at port number
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(1098);
            System.out.println("Bank: Registry created");
        } catch (RemoteException e) {
            System.out.println("Bank: Cannot create registry");
            try {
                registry = LocateRegistry.getRegistry("localhost", 1098);
                System.out.println("Bank: Registry located");
            } catch (RemoteException e1) {
                System.out.println("Bank: Cannot locate registry");
            }
        }

        //Bind using registry
        try {
            registry.rebind(name, this);
            System.out.println("Bank: Bank bound to registry");
        } catch (RemoteException e) {
            System.out.println("Bank: Cannot bind Bank");
            System.out.println("Bank: RemoteException: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Bank: Port already in use. \nBank: Please check if the server isn't already running");
            System.out.println("Bank: NullPointerException: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
