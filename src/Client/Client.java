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
    private ScreensController screensController;

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

    public List<TempBank> getOnlineBanks() throws RemoteException {
        return centralBank.getOnlineBanks();
    }

    public TempAccount getAccountDetails() throws RemoteException {
        TempAccount account = session.getAccountDetails();
        account.setPassword(decryptPassword(account.getPassword()));
        return account;
    }

    public Client(ScreensController screensController) throws RemoteException {
        this.screensController = screensController;
        getCentralBank();
    }

    public boolean login(String iban, String password) throws RemoteException {
        if (iban.equals("admin")) {
            admin = centralBank.loginAdmin(encryptPassword(password));
            return admin;
        } else if (iban.length() == 18) {
            session = centralBank.loginClient(iban, encryptPassword(password));
            if (session != null) {
                String shortcut = iban.substring(4, 8);
                getPublisher(shortcut);
                publisherForListener.subscribeRemoteListener(this, session.getIban().toUpperCase());
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
            publisherForListener.unsubscribeRemoteListener(this, session.getIban().toUpperCase());
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

    public boolean isSessionValid() throws RemoteException {
        return session.isSessionValid();
    }

    public boolean createBankAccount(TempBank bank, String password, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException {
        String iban = centralBank.createBankAccount(bank, encryptPassword(password), firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
        return login(iban, password);
    }

    public void editBankAccount(String password, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) throws RemoteException {
        session.editBankAccount(encryptPassword(password), firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
    }

    public void deleteBankAccount() throws RemoteException {
        centralBank.logOutClient(session);
        session.deleteBankAccount();
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

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getNewValue() instanceof TempAccount){
            screensController.getBankAccountController().updateAccountDetails((TempAccount)event.getNewValue());
        } else if (event.getNewValue() instanceof Transaction){
            screensController.getBankAccountController().updateTransactionHistory((Transaction)event.getNewValue());
        }
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

    private void getPublisher(String shortcut) {
        // Locate registry at IP address and port number
        Registry registry = getRegistry();

        //Get Publisher from registry
        if (registry != null) {
            try {
                publisherForListener = (IRemotePublisherForListener) registry.lookup(shortcut.toUpperCase());
                System.out.println("Client: publisher retrieved");
            } catch (RemoteException e) {
                System.out.println("Client: RemoteException on publisher");
                System.out.println("Client: RemoteException: " + e.getMessage());
                System.exit(0);
            } catch (NotBoundException e) {
                System.out.println("Client: Cannot bind publisher");
                System.out.println("Client: NotBoundException: " + e.getMessage());
                System.exit(0);
            }
        }
    }

    private void getCentralBank() {
        // Locate registry at IP address and port number
        Registry registry = getRegistry();

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

    private Registry getRegistry(){
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1234);
            System.out.println("Client: Registry located");
            return registry;
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            System.exit(0);
        }
        return null;
    }
}
