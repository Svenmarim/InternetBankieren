package Client.Controllers;

import Client.ControlledScreen;
import Client.ScreensController;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class AccountController implements ControlledScreen {
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
//        try {
//            client.deleteBankAccount();
//            changeScreenTo(Screens.LOGIN);
//        } catch (RemoteException e) {
//            showErrorMessage(e.getMessage());
//        }
    }

    public void editBankAccount() {
//        String password = tbPassword.getText();
//        String passwordRepeat = tbRepeatPassword.getText();
//        String firstName = tbFirstName.getText();
//        String lastName = tbLastName.getText();
//        String postalCode = tbPostalCode.getText();
//        int houseNumber = Integer.parseInt(tbHouseNumber.getText());
//        Date dateOfBirth = Date.from(dtpDateOfBirth.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
//        String email = tbEmail.getText();
//        if (password.equals(passwordRepeat) && !password.equals("")){
//            if (!firstName.equals("") && !lastName.equals("") && !postalCode.equals("") && houseNumber != 0 && dateOfBirth.before(new Date()) && !email.equals("")){
//                try {
//                    client.editBankAccount(password, firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
//                    changeScreenTo(Screens.BANKACCOUNT);
//                } catch (RemoteException e) {
//                    showErrorMessage(e.getMessage());
//                }
//            } else {
//                showErrorMessage("Personal details are not valid.");
//            }
//        } else {
//            showErrorMessage("Password can not be empty or is not the same as repeated password.");
//        }
    }

    public void cancelToBankAccount() {
//        changeScreenTo(Screens.BANKACCOUNT);
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
