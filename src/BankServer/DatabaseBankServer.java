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

    public List<Bank> getBanks() {
        List<Bank> banks = new ArrayList<>();
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM bankieren.bank")) {
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
}
