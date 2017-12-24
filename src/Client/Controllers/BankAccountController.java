package Client.Controllers;

import Client.ClientMain;
import Client.IControllers;
import Client.ScreensController;
import Shared.TempAccount;
import Shared.Transaction;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class BankAccountController implements IControllers {
    private ScreensController myController;

    //FXML fields
    public Label lbName;
    public Label lbIban;
    public Label lbAmount;
    public TableView tabelTransactions;
    public TableColumn clmDate;
    public TableColumn clmIban;
    public TableColumn clmAmount;
    public TableColumn clmDescription;

    public void openAccount() {
        myController.setScreen(ClientMain.screenAccountId);
    }

    public void openLimits() {
        myController.setScreen(ClientMain.screenLimitsId);
    }

    public void openAddressBook() {
        myController.setScreen(ClientMain.screenAddressBookId);
    }

    public void openTransaction() {
        myController.setScreen(ClientMain.screenNewTransactionId);
    }

    public void logoutClient() {
        try {
            myController.getClient().logout();
            myController.setScreen(ClientMain.screenLoginId);
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    public void setAccountDetails(){
        try {
            TempAccount account = myController.getClient().getAccountDetails();
            lbName.setText(account.getFirstName() + " " + account.getLastName());
            lbIban.setText(account.getIban());
            lbAmount.setText(String.valueOf(account.getAmount()));
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    public void setTransactionHistory(){
        try {
            List<Transaction> transactionHistory = myController.getClient().getTransactionHistory();
            clmDate.setCellValueFactory(new PropertyValueFactory<Transaction, Date>("date"));
            clmIban.setCellValueFactory(new PropertyValueFactory<Transaction, String>("iban"));
            clmAmount.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("amount"));
            clmDescription.setCellValueFactory(new PropertyValueFactory<Transaction, String>("description"));
            tabelTransactions.setItems((FXCollections.observableArrayList(transactionHistory)));
//            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            dateFormat.format(date);
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
