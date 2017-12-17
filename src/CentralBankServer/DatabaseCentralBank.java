package CentralBankServer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
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

    public boolean loginAdmin(String hashedPassword){
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
        return hashedPassword.equals(passwordInDatabase);
    }
}
