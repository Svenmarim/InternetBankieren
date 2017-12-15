package Client;

import Shared.Address;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
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
        try {
            client.login(iban, password);
        } catch (RemoteException e) {
            e.printStackTrace();
            //TODO show error message
        }
        if (iban.equals("admin")) {
            changeScreens(Screens.MANAGEBANKS);
        } else {
            changeScreens(Screens.BANKACCOUNT);
        }
    }

    public void logoutClient() {
        try {
            client.logout();
        } catch (RemoteException e) {
            e.printStackTrace();
            //TODO show error message
        }
        changeScreens(Screens.LOGIN);
    }

    public void logoutAdmin() {
        try {
            client.logout();
        } catch (RemoteException e) {
            e.printStackTrace();
            //TODO show error message
        }
        changeScreens(Screens.LOGIN);
    }

    public void cancelToLogin() {
        changeScreens(Screens.LOGIN);
    }

    public void createBank() {
        String name = tbBankName.getText();
        String shortcut = tbShortcut.getText();
        try {
            client.createBank(name, shortcut);
        } catch (RemoteException e) {
            e.printStackTrace();
            //TODO show error message
        }
        changeScreens(Screens.MANAGEBANKS);
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
        changeScreens(Screens.BANKACCOUNT);
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
        changeScreens(Screens.BANKACCOUNT);
    }

    public void deleteBankAccount() {
        try {
            client.deleteBankAccount();
        } catch (RemoteException e) {
            e.printStackTrace();
            //TODO show error message
        }
        changeScreens(Screens.LOGIN);
    }

    public void cancelToBankAccount() {
        changeScreens(Screens.BANKACCOUNT);
    }

    public void editBankAccountsLimits() {
        double limitIn = Double.parseDouble(tbEuroIn.getText() + "," + tbCentIn.getText());
        double limitOut = Double.parseDouble(tbEuroOut.getText() + "," + tbCentOut.getText());
        client.editBankAccountsLimits(limitIn, limitOut);
        changeScreens(Screens.BANKACCOUNT);
    }

    public void deleteBankAccountsAddress() {
        Address address = (Address) tabelAddresses.getSelectionModel().getSelectedItem();
        client.deleteBankAccountsAddress(address);
    }

    public void makeBankAccountsTransaction() {
        double amount = Double.parseDouble(tbEuroAmount.getText() + "," + tbCentAmount.getText());
        String nameReceiver = tbNameReceiver.getText();
        String ibanReceiver = tbIbanReceiver.getText();
        String description = tbDescription.getText();
        boolean addToAddress = cbAddToAddressBook.isSelected();
        client.makeBankAccountsTransaction(amount, nameReceiver, ibanReceiver, description, addToAddress);
        changeScreens(Screens.BANKACCOUNT);
    }

    public void chooseAddress() {
        Address address = (Address) tabelAddresses.getSelectionModel().getSelectedItem();
        tbNameReceiver.setText(address.getName());
        tbIbanReceiver.setText(address.getIban());
        changeScreens(Screens.NEWTRANSACTION);
    }

    public void makeBankAccountsRequest() {
        double amount = Double.parseDouble(tbEuroAmount.getText() + "," + tbCentAmount.getText());
        String nameReceiver = tbNameReceiver.getText();
        String ibanReceiver = tbIbanReceiver.getText();
        String description = tbDescription.getText();
        boolean addToAddress = cbAddToAddressBook.isSelected();
        client.makeBankAccountsRequest(amount, nameReceiver, ibanReceiver, description, addToAddress);
        changeScreens(Screens.BANKACCOUNT);
    }

    public void openCreateAccount() {
        changeScreens(Screens.CREATEBANKACCOUNT);
    }

    public void openAccount() {
        changeScreens(Screens.ACCOUNT);
    }

    public void openLimits() {
        changeScreens(Screens.LIMITS);
    }

    public void openAddressBook() {
        changeScreens(Screens.ADDRESSBOOK);
    }

    public void openAddressBookTransaction() {
        changeScreens(Screens.ADDRESSBOOKTRANSACTION);
    }

    public void openTransaction() {
        changeScreens(Screens.NEWTRANSACTION);
    }

    public void openCreateBank() {
        changeScreens(Screens.CREATEBANK);
    }

    private void initAccountScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/account.fxml"));
        stage.setTitle("Account");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> changeScreens(Screens.BANKACCOUNT));
        stage.show();
    }

    private void initAddressBookScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/addressBook.fxml"));
        stage.setTitle("Address Book");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> changeScreens(Screens.BANKACCOUNT));
        stage.show();
    }

    private void initAddressBookTransactionScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/addressBookTransaction.fxml"));
        stage.setTitle("Address Book");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> changeScreens(Screens.NEWTRANSACTION));
        stage.show();
    }

    private void initBankAccountScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/bankAccount.fxml"));
        stage.setTitle("Bank Account");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> logoutClient());
        stage.show();
    }

    private void initCreateBankScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/createBank.fxml"));
        stage.setTitle("Create Bank");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> changeScreens(Screens.MANAGEBANKS));
        stage.show();
    }

    private void initCreateBankAccountScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/createBankAccount.fxml"));
        stage.setTitle("Create Bank Account");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> changeScreens(Screens.LOGIN));
        stage.show();
    }

    private void initLimitsScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/limits.fxml"));
        stage.setTitle("Limits");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> changeScreens(Screens.BANKACCOUNT));
        stage.show();
    }

    private void initLoginScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/login.fxml"));
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.show();
    }

    private void initManageBanksScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/manageBanks.fxml"));
        stage.setTitle("Manage Banks");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> logoutAdmin());
        stage.show();
    }

    private void initnewTransactionScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/newTransaction.fxml"));
        stage.setTitle("New Transaction");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> changeScreens(Screens.BANKACCOUNT));
        stage.show();
    }

    private void changeScreens(Screens screen) {
        if (screenEditAccount != null) {
            Stage currentStage = (Stage) screenEditAccount.getScene().getWindow();
            currentStage.close();
        } else if (screenAddressBook != null) {
            Stage currentStage = (Stage) screenAddressBook.getScene().getWindow();
            currentStage.close();
        } else if (screenAddressBookTransaction != null) {
            Stage currentStage = (Stage) screenAddressBookTransaction.getScene().getWindow();
            currentStage.close();
        } else if (screenBankAccount != null) {
            Stage currentStage = (Stage) screenBankAccount.getScene().getWindow();
            currentStage.close();
        } else if (screenCreateBank != null) {
            Stage currentStage = (Stage) screenCreateBank.getScene().getWindow();
            currentStage.close();
        } else if (screenCreateBankAccount != null) {
            Stage currentStage = (Stage) screenCreateBankAccount.getScene().getWindow();
            currentStage.close();
        } else if (screenLimits != null) {
            Stage currentStage = (Stage) screenLimits.getScene().getWindow();
            currentStage.close();
        } else if (screenLogin != null) {
            Stage currentStage = (Stage) screenLogin.getScene().getWindow();
            currentStage.close();
        } else if (screenManageBanks != null) {
            Stage currentStage = (Stage) screenManageBanks.getScene().getWindow();
            currentStage.close();
        } else if (screenNewTransaction != null) {
            Stage currentStage = (Stage) screenNewTransaction.getScene().getWindow();
            currentStage.close();
        }

        try {
            switch (screen) {
                case ACCOUNT:
                    initAccountScreen();
                    break;
                case ADDRESSBOOK:
                    initAddressBookScreen();
                    break;
                case ADDRESSBOOKTRANSACTION:
                    initAddressBookTransactionScreen();
                    break;
                case BANKACCOUNT:
                    initBankAccountScreen();
                    break;
                case CREATEBANK:
                    initCreateBankScreen();
                    break;
                case CREATEBANKACCOUNT:
                    initCreateBankAccountScreen();
                    break;
                case LIMITS:
                    initLimitsScreen();
                    break;
                case LOGIN:
                    initLoginScreen();
                    break;
                case MANAGEBANKS:
                    initManageBanksScreen();
                    break;
                case NEWTRANSACTION:
                    initnewTransactionScreen();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            //TODO show error message
        }
    }
}
