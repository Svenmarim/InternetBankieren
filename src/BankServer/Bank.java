package BankServer;

import Shared.*;

import java.beans.PropertyChangeEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class Bank extends UnicastRemoteObject implements IBankForCentralBank, IRemotePublisherForDomain, IRemotePublisherForListener {
    private String name;
    private String shortcut;
    private List<IBankForClient> sessions;
    private DatabaseBankServer database;
    private ICentralBankForBank centralBank;
    private final Map<String, List<IRemotePropertyListener>> propertyListeners;
    private String propertiesString;
    private final ExecutorService pool;

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

        // Ensure that hash map is synchronized
        propertyListeners = Collections.synchronizedMap(new HashMap<>());

        // Register null-String as property
        propertyListeners.put(null, Collections.synchronizedList(new ArrayList<>()));

        // Initialize string of all registered properties
        setPropertiesString();

        // Initialize thread pool
        pool = Executors.newFixedThreadPool(10);
    }

    @Override
    public IBankForClient loginClient(String iban, String encryptedPassword) throws RemoteException {
        BankAccount bankAccount = database.login(iban.toUpperCase(), encryptedPassword);
        if (bankAccount != null) {
            Session session = new Session(bankAccount);
            sessions.add(session);
            //Add property for listener
            registerProperty(iban.toUpperCase());
            return session;
        }
        return null;
    }

    @Override
    public void logOutClient(IBankForClient session) throws RemoteException {
        for (IBankForClient s : sessions) {
            if (s.getIban().equals(session.getIban())) {
                //Remove property for listener
                unregisterProperty(s.getIban().toUpperCase());

                sessions.remove(s);
                break;
            }
        }
    }

    @Override
    public boolean transaction(String iban, Transaction transaction) throws RemoteException {
        database.insertTransaction(iban, transaction);
        database.updateAmount(iban, database.getAmount(iban) + transaction.getAmount());
        for (IBankForClient session : sessions) {
            if (session.getIban().equals(iban)) {
                session.receiveBankAccountsTransaction(transaction);
                inform(session.getIban().toUpperCase(), null, session.getAccountDetails());
                inform(session.getIban().toUpperCase(), null, transaction);
            }
        }
        return true;
    }

    @Override
    public void registerProperty(String property) {
        if (property.equals("")) {
            throw new RuntimeException("a property cannot be an empty string");
        }

        if (propertyListeners.containsKey(property)) {
            return;
        }

        propertyListeners.put(property, Collections.synchronizedList(new ArrayList<>()));

        setPropertiesString();
    }

    @Override
    public void unregisterProperty(String property) {
        // Check whether property is registered
        checkInBehalfOfProgrammer(property);

        if (property != null) {
            // Unregister this property
            propertyListeners.remove(property);
        } else {
            // Unregister all properties
            List<String> keyset = new ArrayList<>(propertyListeners.keySet());
            for (String key : keyset) {
                if (key != null) {
                    propertyListeners.remove(key);
                }
            }
        }

        setPropertiesString();
    }

    @Override
    public void inform(String property, Object oldValue, Object newValue) {
        // Check whether property is registered
        checkInBehalfOfProgrammer(property);

        // Determine property listeners to be informed
        List<IRemotePropertyListener> listenersToBeInformed;
        listenersToBeInformed = new ArrayList<>();

        if (property != null) {
            // Listeners that are subscribed to given property
            listenersToBeInformed.addAll(propertyListeners.get(property));
            // Listeners that are subscribed to null-String
            listenersToBeInformed.addAll(propertyListeners.get(null));
        } else {
            // Inform all listeners, including listeners that are subscribed to null-String
            List<String> keyset = new ArrayList<>(propertyListeners.keySet());
            for (String key : keyset) {
                listenersToBeInformed.addAll(propertyListeners.get(key));
            }
        }

        // Inform property listeners concurrently
        for (IRemotePropertyListener listener : listenersToBeInformed) {

            // Define property change event to be sent to listener
            final PropertyChangeEvent event = new PropertyChangeEvent(
                    this, property, oldValue, newValue);

            // Create command (runnable) to be executed by thread pool
            InformListenerRunnable informListenerRunnable
                    = new InformListenerRunnable(listener, event);

            // Execute command at some time in the future
            pool.execute(informListenerRunnable);
        }
    }

    @Override
    public List<String> getProperties() {
        List<String> properties = new ArrayList<>(propertyListeners.keySet());
        return Collections.unmodifiableList(properties);
    }

    @Override
    public void subscribeRemoteListener(IRemotePropertyListener listener, String property) {
        // Check whether property is registered
        checkInBehalfOfProgrammer(property);

        // Subscribe property listener to property
        propertyListeners.get(property).add(listener);
    }

    @Override
    public void unsubscribeRemoteListener(IRemotePropertyListener listener, String property) {
        if (property != null) {
            // Unsubscribe property listener from given property
            List<IRemotePropertyListener> listeners = propertyListeners.get(property);
            if (listeners != null) {
                listeners.remove(listener);
                propertyListeners.get(null).remove(listener);
            }
        } else {
            // Unsubscribe property listener from all properties
            List<String> keyset = new ArrayList<>(propertyListeners.keySet());
            for (String key : keyset) {
                propertyListeners.get(key).remove(listener);
            }
        }
    }

    // Set string of all registered properties
    private void setPropertiesString() {
        List<String> properties = getProperties();
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        boolean firstProperty = true;
        for (String property : properties) {
            if (firstProperty) {
                firstProperty = false;
            } else {
                sb.append(", ");
            }
            sb.append(property);
        }
        sb.append(" }");
        propertiesString = sb.toString();
    }

    // Check whether property is registered
    private void checkInBehalfOfProgrammer(String property) throws RuntimeException {
        if (!propertyListeners.containsKey(property)) {
            throw new RuntimeException("property " + property + " is not a "
                    + "published property, please make a choice out of: "
                    + propertiesString);
        }
    }

    // Inner class to enable concurrent method invocation of propertyChange()
    private class InformListenerRunnable implements Runnable {

        // Property listener to be informed
        IRemotePropertyListener listener;

        // Property change event to be sent to listener
        PropertyChangeEvent event;

        public InformListenerRunnable(IRemotePropertyListener listener, PropertyChangeEvent event) {
            this.listener = listener;
            this.event = event;
        }

        @Override
        public void run() {
            // Property listener is remote
            IRemotePropertyListener remoteListener = listener;
            try {
                remoteListener.propertyChange(event);
            } catch (RemoteException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void shutDownBank() {
        try {
            centralBank.shutDownBank(this);
            System.out.println("Bank: Bank removed in central bank");
            if (database.setBankOffline(this)) {
                System.out.println("Bank: Bank offline in database");
                System.exit(0);
            } else {
                System.out.println("Bank: ERROR! Bank NOT offline in database");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void bindPublisherInRegistry() {
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
            registry.rebind(shortcut, this);
            System.out.println("Bank: Bank bound to registry");
            centralBank.startUpBank(this);
            System.out.println("Bank: Bank added in central bank");
            if (database.setBankOnline(this)) {
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
