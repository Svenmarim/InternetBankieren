package Client.Controllers;

import Client.ClientMain;
import Client.IControllers;
import Client.ScreensController;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public void setTransaction(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.format(date); //Set this value in table
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
