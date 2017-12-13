package Client;

import Shared.Address;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.time.ZoneId;
import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 13-12-2017
 */
public class Controller {
    public TextField tbIban;
    public TextField tbFirstName;
    public TextField tbLastName;
    public TextField tbPostalCode;
    public TextField tbHouseNumber;
    public TextField tbEmail;
    public TextField tbBankName;
    public TextField tbShortcut;
    public TextField tbEuroIn;
    public TextField tbCentIn;
    public TextField tbCentOut;
    public TextField tbEuroOut;
    public TextField tbEuroAmount;
    public TextField tbCentAmount;
    public TextField tbNameReceiver;
    public TextField tbIbanReceiver;
    public TextArea tbDescription;
    public PasswordField tbPassword;
    public PasswordField tbRepeatPassword;
    public Label lbName;
    public Label lbIban;
    public Label lbAmount;
    public TableView tabelTransactions;
    public TableView tabelAddresses;
    public TableView tabelBanks;
    public DatePicker dtpDateOfBirth;
    public CheckBox cbAddToAddressBook;
    public ComboBox cmbbank;
    public AnchorPane screenEditAccount;
    public AnchorPane screenAddressBook;
    public AnchorPane screenAddressBookTransaction;
    public AnchorPane screenBankAccount;
    public AnchorPane screenCreateBank;
    public AnchorPane screenCreateBankAccount;
    public AnchorPane screenLimits;
    public AnchorPane screenLogin;
    public AnchorPane screenManageBanks;
    public AnchorPane screenNewTransaction;

    private Client client;

    public Controller() {
        try {
            client = new Client();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void login() {
        String iban = tbIban.getText();
        String password = tbPassword.getText();
        boolean succeed = client.login(iban, password);
        if (succeed && iban.equals("admin")){
            Stage currentStage = (Stage) screenLogin.getScene().getWindow();
            currentStage.close();
            //TODO show admin screen
        } else if (succeed) {
            //TODO show bank account screen
        } else {
            //TODO show error message
        }
    }

    public void logout() {
        client.logout();
    }

    public void cancelToLogin() {

    }

    public void createBank() {
        String name = tbBankName.getText();
        String shortcut = tbShortcut.getText();
        client.createBank(name, shortcut);
    }

    public void deleteBank() {
        String bank = String.valueOf(tabelBanks.getSelectionModel().getSelectedItem());
        String[] bankParts = bank.split(";");
        String bankName = bankParts[0];
        client.deleteBank(bankName);
    }

    public void createBankAccount() {
        String password = tbPassword.getText();
        String passwordRepeat = tbRepeatPassword.getText();
        String firstName = tbFirstName.getText();
        String lastName = tbLastName.getText();
        String postalCode = tbPostalCode.getText();
        int houseNumber = Integer.parseInt(tbHouseNumber.getText());
        Date dateOfBirth = Date.from(dtpDateOfBirth.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        String email = tbEmail.getText();
        String bankName = cmbbank.getValue().toString();
        client.createBankAccount(bankName, password, passwordRepeat, firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
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
        client.editBankAccount(password, passwordRepeat, firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
    }

    public void deleteBankAccount() {
        client.deleteBankAccount();
        cancelToLogin();
    }

    public void cancelToBankAccount() {

    }

    public void editBankAccountsLimits() {
        double limitIn = Double.parseDouble(tbEuroIn.getText() + "," + tbCentIn.getText());
        double limitOut = Double.parseDouble(tbEuroOut.getText() + "," + tbCentOut.getText());
        client.editBankAccountsLimits(limitIn, limitOut);
    }

    public void deleteBankAccountsAddress() {
        Address address = (Address)tabelAddresses.getSelectionModel().getSelectedItem();
        client.deleteBankAccountsAddress(address);
    }

    public void makeBankAccountsTransaction() {
        double amount = Double.parseDouble(tbEuroAmount.getText() + "," + tbCentAmount.getText());
        String nameReceiver = tbNameReceiver.getText();
        String ibanReceiver = tbIbanReceiver.getText();
        String description = tbDescription.getText();
        boolean addToAddress = cbAddToAddressBook.isSelected();
        client.makeBankAccountsTransaction(amount, nameReceiver, ibanReceiver, description, addToAddress);
    }

    public void chooseAddress() {
        Address address = (Address)tabelAddresses.getSelectionModel().getSelectedItem();
        //TODO Close AddressBookTransaction screen
        tbNameReceiver.setText(address.getName());
        tbIbanReceiver.setText(address.getIban());
    }

    public void makeBankAccountsRequest() {
        double amount = Double.parseDouble(tbEuroAmount.getText() + "," + tbCentAmount.getText());
        String nameReceiver = tbNameReceiver.getText();
        String ibanReceiver = tbIbanReceiver.getText();
        String description = tbDescription.getText();
        boolean addToAddress = cbAddToAddressBook.isSelected();
        client.makeBankAccountsRequest(amount, nameReceiver, ibanReceiver, description, addToAddress);
    }

    public void openCreateAccount(){

    }

    public void openAccount() {

    }

    public void openLimits() {

    }

    public void openAddressBook() {

    }

    public void openAddressBookTransaction() {

    }

    public void openTransaction() {

    }

    public void openCreateBank() {

    }
}
