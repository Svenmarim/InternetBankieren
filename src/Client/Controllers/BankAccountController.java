package Client.Controllers;

import Client.ControlledScreen;
import Client.ScreensController;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class BankAccountController implements ControlledScreen {
    private ScreensController myController;

    //FXML fields
    public Label lbName;
    public Label lbIban;
    public Label lbAmount;
    public TableView tabelTransactions;

    public void openAccount() {
//        changeScreenTo(Screens.ACCOUNT);
    }

    public void openLimits() {
//        changeScreenTo(Screens.LIMITS);
    }

    public void openAddressBook() {
//        changeScreenTo(Screens.ADDRESSBOOK);
    }

    public void openTransaction() {
//        changeScreenTo(Screens.NEWTRANSACTION);
    }

    public void logoutClient() {
//        try {
//            client.logout();
//            changeScreenTo(Screens.LOGIN);
//        } catch (RemoteException e) {
//            showErrorMessage(e.getMessage());
//        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
