package Client.Controllers;

import Client.ClientMain;
import Client.IControllers;
import Client.ScreensController;
import Shared.TempAccount;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.time.ZoneId;
import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class AccountController implements IControllers {
    private ScreensController myController;

    //FXML fields
    public PasswordField tbPassword;
    public PasswordField tbRepeatPassword;
    public TextField tbFirstName;
    public TextField tbLastName;
    public TextField tbPostalCode;
    public TextField tbHouseNumber;
    public DatePicker dtpDateOfBirth;
    public TextField tbEmail;

    public void deleteBankAccount() {
        try {
            myController.getClient().deleteBankAccount();
            myController.setScreen(ClientMain.screenLoginId);
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    public void editBankAccount() {
        String password = tbPassword.getText();
        String passwordRepeat = tbRepeatPassword.getText();
        String firstName = tbFirstName.getText();
        String lastName = tbLastName.getText();
        String postalCode = tbPostalCode.getText();
        int houseNumber = Integer.parseInt(tbHouseNumber.getText());
        Date dateOfBirth = Date.from(dtpDateOfBirth.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        String email = tbEmail.getText();
        if (password.equals(passwordRepeat) && !password.equals("")){
            if (!firstName.equals("") && !lastName.equals("") && !postalCode.equals("") && houseNumber != 0 && dateOfBirth.before(new Date()) && !email.equals("")){
                try {
                    myController.getClient().editBankAccount(password, firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
                    myController.setScreen(ClientMain.screenBankAccountId);
                } catch (RemoteException e) {
                    myController.showErrorMessage(e.getMessage());
                }
            } else {
                myController.showErrorMessage("Personal details are not valid.");
            }
        } else {
            myController.showErrorMessage("Password can not be empty or is not the same as repeated password.");
        }
    }

    public void cancelToBankAccount() {
        myController.setScreen(ClientMain.screenBankAccountId);
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    public void setAccountDetails() {
        try {
            TempAccount account = myController.getClient().getAccountDetails();

        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }
}
