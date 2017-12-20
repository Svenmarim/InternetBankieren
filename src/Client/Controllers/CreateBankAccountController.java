package Client.Controllers;

import Client.ControlledScreen;
import Client.ScreensController;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class CreateBankAccountController implements ControlledScreen {
    private ScreensController myController;

    //FXML fields
    public ComboBox cmbbank;
    public PasswordField tbPassword;
    public PasswordField tbRepeatPassword;
    public TextField tbFirstName;
    public TextField tbLastName;
    public TextField tbPostalCode;
    public TextField tbHouseNumber;
    public DatePicker dtpDateOfBirth;
    public TextField tbEmail;

    public void createBankAccount() {
//        String password = tbPassword.getText();
//        String passwordRepeat = tbRepeatPassword.getText();
//        String firstName = tbFirstName.getText();
//        String lastName = tbLastName.getText();
//        String postalCode = tbPostalCode.getText();
//        int houseNumber = Integer.parseInt(tbHouseNumber.getText());
//        Date dateOfBirth = Date.from(dtpDateOfBirth.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
//        String email = tbEmail.getText();
//        String bankName = cmbbank.getValue().toString();
//        if (password.equals(passwordRepeat) && !password.equals("")){
//            if (!firstName.equals("") && !lastName.equals("") && !postalCode.equals("") && houseNumber != 0 && dateOfBirth.before(new Date()) && !email.equals("") && !bankName.equals("")){
//                try {
//                    if (client.createBankAccount(bankName, password, firstName, lastName, postalCode, houseNumber, dateOfBirth, email)){
//                        changeScreenTo(Screens.BANKACCOUNT);
//                    } else {
//                        showErrorMessage("Something went wrong with creating your account.");
//                    }
//                } catch (RemoteException e) {
//                    showErrorMessage(e.getMessage());
//                }
//            } else {
//                showErrorMessage("Personal details are not valid or there is no bank selected.");
//            }
//        } else {
//            showErrorMessage("Password can not be empty or is not the same as repeated password.");
//        }
    }

    public void cancelToLogin() {
//        changeScreenTo(Screens.LOGIN);
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
