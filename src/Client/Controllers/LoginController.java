package Client.Controllers;

import Client.ClientMain;
import Client.IControllers;
import Client.ScreensController;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class LoginController implements IControllers {
    private ScreensController myController;

    //FXML fields
    public TextField tbIban;
    public PasswordField tbPassword;

    public void login() {
        String iban = tbIban.getText();
        String password = tbPassword.getText();
        try {
            if (myController.getClient().login(iban, password)){
                if (iban.equals("admin")) {
                    myController.setScreen(ClientMain.screenManageBanksId);
                } else {
                    myController.setScreen(ClientMain.screenBankAccountId);
                }
            } else {
                if (iban.equals("admin")) {
                    myController.showErrorMessage("Wrong username or password or there is already an admin logged in.");
                } else {
                    myController.showErrorMessage("Wrong iban or password or the bank server is offline.");
                }
            }
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    public void openCreateAccount() {
        myController.setScreen(ClientMain.screenCreateBankAccountId);
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
