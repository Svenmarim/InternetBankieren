package Client;

import Shared.Address;
import BankServer.Bank;
import Shared.*;

import javax.xml.bind.DatatypeConverter;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 2-12-2017
 */
public class Client extends UnicastRemoteObject implements IRemotePropertyListener {
    private boolean admin;
    private ICentralBankForClient centralBank;
    private IBankForClient session;
    private IRemotePublisherForListener publisherForListener;

    public Client() throws RemoteException {
        getCentralBank();
    }

    public boolean login(String iban, String password) throws RemoteException {
        if (iban.equals("admin")) {
            admin = centralBank.loginAdmin(hashPassword(password));
            return admin;
        } else if (iban.length() == 18) {
            session = centralBank.loginClient(iban, hashPassword(password));
            if (session != null){
                bindClientInRegistry(iban);
            }
            return session != null;
        }
        return false;
    }

    public void logout() throws RemoteException {
        if (admin) {
            centralBank.logOutAdmin();
            admin = false;
        } else {
            centralBank.logOutClient(session);
            this.session = null;
        }
    }

    public boolean createBank(String name, String shortcut) throws RemoteException {
        return centralBank.createBank(name, shortcut);
    }

    public void deleteBank(String bankName) {

    }

    private void isSessionValid() {

    }

    public boolean createBankAccount(String bankName, String password, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException {
        String iban = centralBank.createBankAccount(bankName, hashPassword(password), firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
        return login(iban, password);
    }

    public void editBankAccount(String password, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException {
        session.editBankAccount(hashPassword(password), firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
    }

    public void deleteBankAccount() throws RemoteException {
        session.deleteBankAccount();
        centralBank.logOutClient(session);
        this.session = null;
    }

    public void editBankAccountsLimits(double limitIn, double limitOut) {

    }

    public void deleteBankAccountsAddress(Address address) {

    }

    public void makeBankAccountsTransaction(double amount, String name, String ibanReceiver, String description, boolean addToAddress) {

    }

    public void makeBankAccountsRequest(double amount, String name, String ibanReceiver, String description, boolean addToAddress) {

    }

    public void receiveBankAccountsTransaction(Transaction transaction) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent event) throws RemoteException {

    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void bindClientInRegistry(String iban) {
        //Create registry at port number
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry("localhost", 1234);
            System.out.println("Client: Registry located");
        } catch (RemoteException e1) {
            System.out.println("Client: Cannot locate registry");
            System.exit(0);
        }

        //Bind using registry
        try {
            registry.rebind(iban, this);
            System.out.println("Client: Client bound to registry");
        } catch (RemoteException e) {
            System.out.println("Client: Cannot bind Client");
            System.out.println("Client: RemoteException: " + e.getMessage());
            System.exit(0);
        } catch (NullPointerException e) {
            System.out.println("Client: Port already in use. \nClient: Please check if the server isn't already running");
            System.out.println("Client: NullPointerException: " + e.getMessage());
            System.exit(0);
        }
    }

    private void getCentralBank() {
        // Locate registry at IP address and port number
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry("localhost", 1234);
            System.out.println("Client: Registry located");
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            System.exit(0);
        }

        //Get CentralBank from registry
        if (registry != null) {
            try {
                centralBank = (ICentralBankForClient) registry.lookup("CentralBank");
                System.out.println("Client: CentralBank retrieved");
            } catch (RemoteException e) {
                System.out.println("Client: RemoteException on ICentralBankForClient");
                System.out.println("Client: RemoteException: " + e.getMessage());
                System.exit(0);
            } catch (NotBoundException e) {
                System.out.println("Client: Cannot bind ICentralBankForClient");
                System.out.println("Client: NotBoundException: " + e.getMessage());
                System.exit(0);
            }
        }
    }
}
