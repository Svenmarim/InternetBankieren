package CentralBankServer;

import Shared.TempBank;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * InternetBankieren Created by Sven de Vries on 8-12-2017
 */
public class DatabaseCentralBank {
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

    public boolean loginAdmin(String encryptedPassword) {
        String passwordInDatabase = null;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM bankieren.adminaccount")) {
            try (ResultSet myRs = myStmt.executeQuery()) {
                while (myRs.next()) {
                    passwordInDatabase = myRs.getString("password");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return encryptedPassword.equals(passwordInDatabase);
    }

    public boolean checkIbanExistence(String iban) {
        boolean correct = false;
        setConnection();
        String shortcut = iban.substring(4, 8);
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM bankieren." + shortcut.toLowerCase() + "_bankaccount WHERE iban = ?")) {
            myStmt.setString(1, iban);
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                correct = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return correct;
    }

    public void insertBankAccount(String iban, String encryptedPassword, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) {
        setConnection();
        String shortcut = iban.substring(4, 8);
        try (PreparedStatement myStmt = conn.prepareStatement("INSERT INTO bankieren." + shortcut.toLowerCase() + "_bankaccount VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            myStmt.setString(1, iban);
            myStmt.setString(2, encryptedPassword);
            myStmt.setDouble(3, 50);
            myStmt.setString(4, firstName);
            myStmt.setString(5, lastName);
            myStmt.setString(6, postalCode);
            myStmt.setInt(7, houseNumber);
            myStmt.setDate(8, new java.sql.Date(dateOfBirth.getTime()));
            myStmt.setString(9, email);
            myStmt.setDouble(10, 20);
            myStmt.setDouble(11, 10);
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public boolean checkBankAvailablility(String name, String shortcut) {
        boolean available = true;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM bankieren.bank WHERE name = ? OR shortcut = ?")) {
            myStmt.setString(1, name);
            myStmt.setString(2, shortcut);
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                available = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return available;
    }

    public boolean insertBank(String name, String shortcut) {
        int rowsAffected = 0;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("INSERT INTO bankieren.bank (name, shortcut) VALUES (?, ?)")) {
            myStmt.setString(1, name);
            myStmt.setString(2, shortcut.toUpperCase());
            rowsAffected = myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return rowsAffected == 1;
    }

    public void insertBankAccountTable(String shortcut) {
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("CREATE TABLE bankieren." + shortcut + "_bankaccount (" +
                "`iban` NVARCHAR(50) NOT NULL, " +
                "`password` NVARCHAR(100) NOT NULL, " +
                "`amount` DOUBLE NOT NULL, " +
                "`firstName` NVARCHAR(50) NOT NULL, " +
                "`lastName` NVARCHAR(50) NOT NULL, " +
                "`postalCode` NVARCHAR(50) NOT NULL, " +
                "`houseNumber` INT NOT NULL, " +
                "`dateOfBirth` DATETIME NOT NULL, " +
                "`email` NVARCHAR(50) NOT NULL, " +
                "`limitIn` DOUBLE NOT NULL, " +
                "`limitOut` DOUBLE NOT NULL, " +
                "PRIMARY KEY (`iban`))")) {
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void insertAddressTable(String shortcut) {
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("CREATE TABLE bankieren." + shortcut + "_address (" +
                "`iban` NVARCHAR(50) NOT NULL, " +
                "`bankAccount_iban` NVARCHAR(50) NOT NULL, " +
                "`name` NVARCHAR(50) NOT NULL, " +
                "PRIMARY KEY (`iban`))")) {
            myStmt.executeUpdate();

            try (PreparedStatement myStmt2 = conn.prepareStatement("ALTER TABLE bankieren." + shortcut + "_address " +
                    "ADD CONSTRAINT `" + shortcut + "BankAccount_Address` " +
                    "FOREIGN KEY (`bankAccount_iban`) " +
                    "REFERENCES `bankieren`.`" + shortcut + "_bankaccount` (`iban`) " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE NO ACTION")) {
                myStmt2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void insertTransactionTable(String shortcut) {
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("CREATE TABLE bankieren." + shortcut + "_transaction (" +
                "`bankAccount_iban` NVARCHAR(50)NOT NULL, " +
                "`date` DATETIME NOT NULL, " +
                "`iban` NVARCHAR(50) NOT NULL, " +
                "`amount` DOUBLE NOT NULL, " +
                "`description` NVARCHAR(50) NOT NULL)")) {
            myStmt.executeUpdate();

            try (PreparedStatement myStmt2 = conn.prepareStatement("ALTER TABLE bankieren." + shortcut + "_transaction " +
                    "ADD CONSTRAINT `" + shortcut + "BankAccount_Transaction` " +
                    "FOREIGN KEY (`bankAccount_iban`) " +
                    "REFERENCES `bankieren`.`" + shortcut + "_bankaccount` (`iban`) " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE NO ACTION")) {
                myStmt2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void deleteBank(TempBank bank) {
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("DELETE FROM bankieren.bank WHERE name = ?")) {
            myStmt.setString(1, bank.getName());
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void deleteBankAccountTable(String shortcut) {
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("DROP TABLE bankieren." + shortcut + "_bankaccount")) {
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void deleteAddressTable(String shortcut) {
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("DROP TABLE bankieren." + shortcut + "_address")) {
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void deleteTransactionTable(String shortcut) {
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("DROP TABLE bankieren." + shortcut + "_transaction")) {
            myStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public List<TempBank> getAllBanks() {
        List<TempBank> banks = new ArrayList<>();
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM bankieren.bank")) {
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                String name = myRs.getString("name");
                String shortcut = myRs.getString("shortcut");
                banks.add(new TempBank(name, shortcut));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return banks;
    }
}
