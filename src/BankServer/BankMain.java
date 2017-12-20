package BankServer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * InternetBankieren Created by Sven de Vries on 17-12-2017
 */
public class BankMain extends Application {
    public ComboBox cmbBank;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("chooseBank.fxml"));
        primaryStage.setTitle("Choose Bank");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }

    public void setBanksInComboBox(){
        cmbBank.getItems().clear();
        DatabaseBankServer database = new DatabaseBankServer();
        for(Bank bank : database.getOfflineBanks()){
            cmbBank.getItems().add(bank);
        }
    }

    public void chooseBankToStartUpFrom(){
        if (cmbBank.getValue() != null){
            //Creates bank
            Bank bank = (Bank) cmbBank.getValue();
            bank.getCentralBank();
            bank.bindBankInRegistry();
            System.out.println("Bank: Bank created");

            //Closes the GUI
            Stage currentStage = (Stage) cmbBank.getScene().getWindow();
            currentStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR!");
            alert.setHeaderText(null);
            alert.setContentText("Please select a bank.");
            alert.showAndWait();
        }
    }
}
