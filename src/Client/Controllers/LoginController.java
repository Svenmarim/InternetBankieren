package Client.Controllers;

import Client.ClientMain;
import Client.ControlledScreen;
import Client.ScreensController;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class LoginController implements ControlledScreen {
    private ScreensController myController;

    //FXML fields
    public TextField tbIban;
    public PasswordField tbPassword;

    public void login() {
        String iban = tbIban.getText();
        String password = tbPassword.getText();
//        try {
//            if (client.login(iban, password)){
                if (iban.equals("admin")) {
                    myController.setScreen(ClientMain.screenManageBanksId);
                } else {
                    myController.setScreen(ClientMain.screenBankAccountId);
                }
//            } else {
//                showErrorMessage("Wrong username or password.");
//            }
//        } catch (RemoteException e) {
//            showErrorMessage(e.getMessage());
//        }
    }

    public void openCreateAccount() {
        myController.setScreen(ClientMain.screenCreateBankAccountId);
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
