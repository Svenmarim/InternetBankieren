package Client.Controllers;

import Client.ClientMain;
import Client.IControllers;
import Client.ScreensController;
import Shared.TempAccount;
import Shared.Transaction;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
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

    public void setAccountDetails() {
        try {
            TempAccount account = myController.getClient().getAccountDetails();
            lbName.setText(account.getFirstName() + " " + account.getLastName());
            lbIban.setText(account.getIban());
            lbAmount.setText("€" + account.getAmount()); //TODO show 2 decimals
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    public void setTransactionHistory() {
        try {
            List<Transaction> transactionHistory = myController.getClient().getTransactionHistory();
            clmDate.setCellValueFactory(new PropertyValueFactory<Transaction, Date>("date"));
            clmDate.setCellFactory(column -> new TableCell<Transaction, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        this.setText(format.format(item));
                    }
                }
            });
            clmIban.setCellValueFactory(new PropertyValueFactory<Transaction, String>("iban"));
            clmAmount.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("amount"));
            clmAmount.setCellFactory(column -> new TableCell<Transaction, Double>() {
                private DecimalFormat textFieldFormat = new DecimalFormat("0.00");

                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        if (item < 0) {
                            this.setText("- €" + textFieldFormat.format(item).substring(1));
                        } else {
                            this.setText("+ €" + textFieldFormat.format(item));
                        }
                    }
                }
            });
            clmDescription.setCellValueFactory(new PropertyValueFactory<Transaction, String>("description"));
            tabelTransactions.setItems((FXCollections.observableArrayList(transactionHistory)));
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
