package BankServer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return bankAccount;
    }

    public void insertBankAccount(){
        //INSERT INTO bankieren.abna_bankaccount (iban, password, amount, firstName, lastName, postalCode, houseNumber, dateOfBirth, email, limitIn, limitOut) VALUES ('NL56ABNA0123456789', 'B146A357C57FDDD450F6B5C446108672', '50', 'Sven', 'de Vries', '6005NA', '27', '1995-09-21', 's_devries@live.nl', '20', '10')
    }
}
