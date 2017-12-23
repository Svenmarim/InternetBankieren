package Client;

import Shared.Address;
import Shared.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;

/**
 * InternetBankieren Created by Sven de Vries on 2-12-2017
 */
public class Client extends UnicastRemoteObject implements IRemotePropertyListener {
    private boolean admin;
    private ICentralBankForClient centralBank;
    private IBankForClient session;
    private IRemotePublisherForListener publisherForListener;

    public List<Address> getAddressBook() throws RemoteException {
        return session.getAddressbook();
    }

    public List<Transaction> getTransactionHistory() throws RemoteException {
        return session.getTransactionHistory();
    }

    public Double getLimitIn() throws RemoteException {
        return session.getLimitIn();
    }

    public Double getLimitOut() throws RemoteException {
        return session.getLimitOut();
    }

    public List<TempBank> getBanks() throws RemoteException {
        return centralBank.getAllBanks();
    }

    public TempAccount getAccountDetails() throws RemoteException {
        TempAccount account = session.getAccountDetails();
        account.setPassword(decryptPassword(account.getEncryptedPassword()));
        return account;
    }

    public Client() throws RemoteException {
        getCentralBank();
    }

    public boolean login(String iban, String password) throws RemoteException {
        if (iban.equals("admin")) {
            admin = centralBank.loginAdmin(encryptPassword(password));
            return admin;
        } else if (iban.length() == 18) {
            session = centralBank.loginClient(iban, encryptPassword(password));
            if (session != null) {
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

    public void deleteBank(TempBank bank) throws RemoteException {
        centralBank.deleteBank(bank);
    }

    private void isSessionValid() {

    }

    public boolean createBankAccount(String bankName, String password, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException {
        String iban = centralBank.createBankAccount(bankName, encryptPassword(password), firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
        return login(iban, password);
    }

    public void editBankAccount(String password, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException {
        session.editBankAccount(encryptPassword(password), firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
    }

    public void deleteBankAccount() throws RemoteException {
        session.deleteBankAccount();
        centralBank.logOutClient(session);
        this.session = null;
    }

    public void editBankAccountsLimits(double limitIn, double limitOut) throws RemoteException {
        session.editBankAccountsLimits(limitIn, limitOut);
    }

    public void deleteBankAccountsAddress(Address address) throws RemoteException {
        session.deleteBankAccountsAddress(address);
    }

    public boolean makeBankAccountsTransaction(double amount, String name, String ibanReceiver, String description, boolean addToAddress) throws RemoteException {
        return session.makeBankAccountsTransaction(amount, name, ibanReceiver, description, addToAddress);
    }

    public boolean makeBankAccountsRequest(double amount, String name, String ibanReceiver, String description, boolean addToAddress) throws RemoteException {
        return session.makeBankAccountsRequest(amount, name, ibanReceiver, description, addToAddress);
    }

    public void receiveBankAccountsTransaction(Transaction transaction) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent event) throws RemoteException {

    }

    private String encryptPassword(String password) {
        BASE64Encoder enc = new BASE64Encoder();
        try {
            return enc.encode(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private String decryptPassword(String encryptedPassword) {
        BASE64Decoder dec = new BASE64Decoder();
        try {
            return new String(dec.decodeBuffer(encryptedPassword), "UTF-8");
        } catch (IOException e) {
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
