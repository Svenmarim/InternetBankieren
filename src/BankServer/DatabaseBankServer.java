package BankServer;

import Shared.Address;
import Shared.IBankForCentralBank;
import Shared.TempAccount;
import Shared.Transaction;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.Date;
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

    public boolean setBankOffline(Bank bank) {
        int rowsAffected = 0;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("UPDATE bankieren.bank SET online = 0 WHERE name = ?")) {
            myStmt.setString(1, bank.getName());
            rowsAffected = myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return rowsAffected == 1;
    }

    public BankAccount login(String iban, String hashedPassword) {
        BankAccount bankAccount = null;
        setConnection();
        String shortcut = iban.substring(4, 8);
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

    public void insertAddress(String iban, Address address){
        setConnection();
        String shortcut = iban.substring(4, 8);
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

    public void insertTransaction(String iban, Transaction transaction){
        setConnection();
        String shortcut = iban.substring(4, 8);
        try (PreparedStatement myStmt = conn.prepareStatement("INSERT INTO bankieren." + shortcut.toLowerCase() + "_transaction VALUES (?, ?, ?, ?, ?)")) {
            myStmt.setString(1, iban);
            myStmt.setDate(2, (java.sql.Date)transaction.getDate());
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

    public void updateAmount(String iban, double amount) {
        setConnection();
        String shortcut = iban.substring(4, 8);
        try (PreparedStatement myStmt = conn.prepareStatement("UPDATE bankieren." + shortcut.toLowerCase() + "_bankaccount SET amount = ? WHERE iban = ?")) {
            myStmt.setDouble(1, amount);
            myStmt.setString(2, iban);
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void editBankAccount(String encryptedPassword, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email, String iban) {
        setConnection();
        String shortcut = iban.substring(4, 8);
        try (PreparedStatement myStmt = conn.prepareStatement("UPDATE bankieren." + shortcut.toLowerCase() + "_bankaccount SET password = ?, firstName = ?, lastName = ?, postalCode = ?, houseNumber = ?, dateOfBirth = ?, email = ? WHERE iban = ?")) {
            myStmt.setString(1, encryptedPassword);
            myStmt.setString(2, firstName);
            myStmt.setString(3, lastName);
            myStmt.setString(4, postalCode);
            myStmt.setInt(5, houseNumber);
            myStmt.setDate(6, (java.sql.Date)dateOfBirth);
            myStmt.setString(7, email);
            myStmt.setString(8, iban);
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void deleteBankAccount(String iban) {
        setConnection();
        String shortcut = iban.substring(4, 8);
        try (PreparedStatement myStmt = conn.prepareStatement("DELETE FROM bankieren." + shortcut.toLowerCase() + "_bankaccount WHERE iban = ?")) {
            myStmt.setString(1, iban);
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void editBankAccountLimits(double limitIn, double limitOut, String iban) {
        setConnection();
        String shortcut = iban.substring(4, 8);
        try (PreparedStatement myStmt = conn.prepareStatement("UPDATE bankieren." + shortcut.toLowerCase() + "_bankaccount SET limitIn = ?, limitOut = ? WHERE iban = ?")) {
            myStmt.setDouble(1, limitIn);
            myStmt.setDouble(2, limitOut);
            myStmt.setString(3, iban);
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void deleteBankAccountAddress(Address address, String iban) {
        setConnection();
        String shortcut = iban.substring(4, 8);
        try (PreparedStatement myStmt = conn.prepareStatement("DELETE FROM bankieren." + shortcut.toLowerCase() + "_address WHERE iban = ? AND bankAccount_iban = ?")) {
            myStmt.setString(1, address.getIban());
            myStmt.setString(2, iban);
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public double getAmount(String iban) {
        double amount = 0;
        setConnection();
        String shortcut = iban.substring(4, 8);
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM bankieren." + shortcut.toLowerCase() + "_bankaccount WHERE iban = ?")) {
            myStmt.setString(1, iban);
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                amount = myRs.getDouble("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return amount;
    }

    public TempAccount getAccountDetails(String iban) {
        TempAccount account = null;
        setConnection();
        String shortcut = iban.substring(4, 8);
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM bankieren." + shortcut.toLowerCase() + "_bankaccount WHERE iban = ?")) {
            myStmt.setString(1, iban);
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                double amount = myRs.getDouble("amount");
                String password = myRs.getString("password");
                String firstName = myRs.getString("firstName");
                String lastName = myRs.getString("lastName");
                String postalCode = myRs.getString("postalCode");
                int houseNumber = myRs.getInt("houseNumber");
                Date dateOfBirth = myRs.getDate("dateOfBirth");
                String email = myRs.getString("email");
                account = new TempAccount(amount, iban, password, firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return account;
    }
}
