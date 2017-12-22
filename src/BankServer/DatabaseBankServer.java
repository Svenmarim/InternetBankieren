package BankServer;

import Shared.Address;
import Shared.Transaction;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * InternetBankieren Created by Sven de Vries on 8-12-2017
 */
public class DatabaseBankServer {
    private Connection conn;

    private void setConnection() {
        Properties prop = new Properties();
        InputStream input;

        try {
            input = new FileInputStream("assets/database.properties");
            prop.load(input);

            String url = prop.getProperty("url");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection to database SUCCEED");
        } catch (ClassNotFoundException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn = null;
        }
    }

    public List<Bank> getOfflineBanks() {
        List<Bank> banks = new ArrayList<>();
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM bankieren.bank WHERE online = 0")) {
            try (ResultSet myRs = myStmt.executeQuery()) {
                while (myRs.next()) {
                    String name = myRs.getString("name");
                    String shortcut = myRs.getString("shortcut");
                    banks.add(new Bank(name, shortcut));
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return banks;
    }

    public boolean setBankOnline(Bank bank) {
        int rowsAffected = 0;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("UPDATE bankieren.bank SET online = 1 WHERE name = ?")) {
            myStmt.setString(1, bank.getName());
            rowsAffected = myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return rowsAffected == 1;
    }

    public void setBankOffline(Bank bank) {
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("UPDATE bankieren.bank SET online = 0 WHERE name = ?")) {
            myStmt.setString(1, bank.getName());
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
            System.out.println("Bank: Bank offline in database");
        }
        System.exit(0);
    }

    public BankAccount login(String iban, String hashedPassword, String shortcut) {
        BankAccount bankAccount = null;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM bankieren." + shortcut.toLowerCase() + "_bankaccount WHERE iban = ? AND password = ?")) {
            myStmt.setString(1, iban);
            myStmt.setString(2, hashedPassword);
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                Double amount = myRs.getDouble("amount");
                String firstName = myRs.getString("firstName");
                String lastName = myRs.getString("lastName");
                String postalCode = myRs.getString("postalCode");
                int houseNumber = myRs.getInt("houseNumber");
                Date dateOfBirth = myRs.getDate("dateOfBirth");
                String email = myRs.getString("email");
                Double limitIn = myRs.getDouble("limitIn");
                Double limitOut = myRs.getDouble("limitOut");
                bankAccount = new BankAccount(amount, iban, firstName, lastName, postalCode, houseNumber, dateOfBirth, email, limitIn, limitOut);
                bankAccount = setAddresses(bankAccount, shortcut);
                bankAccount = setTransactions(bankAccount, shortcut);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return bankAccount;
    }

    private BankAccount setAddresses(BankAccount bankAccount, String shortcut) throws SQLException {
        PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM bankieren." + shortcut.toLowerCase() + "_address WHERE bankAccount_iban = ?");
        myStmt.setString(1, bankAccount.getIban());
        ResultSet myRs = myStmt.executeQuery();
        while (myRs.next()) {
            String name = myRs.getString("name");
            String iban = myRs.getString("iban");
            bankAccount.addAddress(new Address(name, iban));
        }
        return bankAccount;
    }

    private BankAccount setTransactions(BankAccount bankAccount, String shortcut) throws SQLException {
        PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM bankieren." + shortcut.toLowerCase() + "_transaction WHERE bankAccount_iban = ?");
        myStmt.setString(1, bankAccount.getIban());
        ResultSet myRs = myStmt.executeQuery();
        while (myRs.next()) {
            Date date = myRs.getDate("date");
            String iban = myRs.getString("iban");
            Double amount = myRs.getDouble("amount");
            String description = myRs.getString("description");
            bankAccount.addTransaction(new Transaction(date, iban, amount, description));
        }
        return bankAccount;
    }

    public void insertBankAccount(BankAccount bankAccount, String hashedPassword, String shortcut){
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("INSERT INTO bankieren." + shortcut.toLowerCase() + "_bankaccount VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            myStmt.setString(1, bankAccount.getIban());
            myStmt.setString(2, hashedPassword);
            myStmt.setDouble(3, bankAccount.getAmount());
            myStmt.setString(4, bankAccount.getFirstName());
            myStmt.setString(5, bankAccount.getLastName());
            myStmt.setString(6, bankAccount.getPostalCode());
            myStmt.setInt(7, bankAccount.getHouseNumber());
            myStmt.setDate(8, (Date)bankAccount.getDateOfBirth());
            myStmt.setString(9, bankAccount.getEmail());
            myStmt.setDouble(10, bankAccount.getLimitInAddressbook());
            myStmt.setDouble(11, bankAccount.getLimitOutAddressbook());
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void insertAddress(String iban, Address address, String shortcut){
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("INSERT INTO bankieren." + shortcut.toLowerCase() + "_address VALUES (?, ?, ?)")) {
            myStmt.setString(1, address.getIban());
            myStmt.setString(2, iban);
            myStmt.setString(3, address.getName());
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void insertTransaction(String iban, Transaction transaction, String shortcut){
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("INSERT INTO bankieren." + shortcut.toLowerCase() + "_transaction VALUES (?, ?, ?, ?, ?)")) {
            myStmt.setString(1, iban);
            myStmt.setDate(2, (Date)transaction.getDate());
            myStmt.setString(3, transaction.getIban());
            myStmt.setDouble(4, transaction.getAmount());
            myStmt.setString(5, transaction.getDescription());
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
}
