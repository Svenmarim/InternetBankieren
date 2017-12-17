package Client;

import CentralBankServer.CentralBank;
import Shared.Address;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
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
        //Print ip address and port number for registry
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Client: IP Address: " + localhost.getHostAddress());
        System.out.println("Client: Port number: 1097");

        try {
            client = new Client();
            System.out.println("Client: Client created");
        } catch (RemoteException e) {
            System.out.println("Client: Cannot create Client");
            System.out.println("Client: RemoteException: " + e.getMessage());
        }
    }

    public void login() {
        String iban = tbIban.getText();
        String password = tbPassword.getText();
        try {
            if (client.login(iban, password)){
                if (iban.equals("admin")) {
                    changeScreenTo(Screens.MANAGEBANKS);
                } else {
                    changeScreenTo(Screens.BANKACCOUNT);
                }
            } else {
                showErrorMessage("Wrong username or password.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void logoutClient() {
        try {
            client.logout();
            changeScreenTo(Screens.LOGIN);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void logoutAdmin() {
        try {
            client.logout();
            changeScreenTo(Screens.LOGIN);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cancelToLogin() {
        changeScreenTo(Screens.LOGIN);
    }

    public void createBank() {
        String name = tbBankName.getText();
        String shortcut = tbShortcut.getText();
        try {
            if (client.createBank(name, shortcut)){
                changeScreenTo(Screens.MANAGEBANKS);
            } else {
                showErrorMessage("Bank name or shortcut already excist.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
        if (password.equals(passwordRepeat) && !password.equals("")){
            if (!firstName.equals("") && !lastName.equals("") && !postalCode.equals("") && houseNumber != 0 && dateOfBirth.before(new Date()) && !email.equals("") && !bankName.equals("")){
                client.createBankAccount(bankName, password, passwordRepeat, firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
                changeScreenTo(Screens.BANKACCOUNT);
            } else {
                showErrorMessage("Personal details are not valid or there is no bank selected.");
            }
        } else {
            showErrorMessage("Password can not be empty or is not the same as repeated password.");
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
                client.editBankAccount(password, passwordRepeat, firstName, lastName, postalCode, houseNumber, dateOfBirth, email);
                changeScreenTo(Screens.BANKACCOUNT);
            } else {
                showErrorMessage("Personal details are not valid.");
            }
        } else {
            showErrorMessage("Password can not be empty or is not the same as repeated password.");
        }
    }

    public void deleteBankAccount() {
        try {
            client.deleteBankAccount();
            changeScreenTo(Screens.LOGIN);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cancelToBankAccount() {
        changeScreenTo(Screens.BANKACCOUNT);
    }

    public void editBankAccountsLimits() {
        double limitIn = Double.parseDouble(tbEuroIn.getText() + "," + tbCentIn.getText());
        double limitOut = Double.parseDouble(tbEuroOut.getText() + "," + tbCentOut.getText());
        if (limitIn > 0 && limitOut > 0){
            client.editBankAccountsLimits(limitIn, limitOut);
            changeScreenTo(Screens.BANKACCOUNT);
        } else {
            showErrorMessage("Limits can not be 0.");
        }
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
        if (amount > 0 && !nameReceiver.equals("") && !ibanReceiver.equals("")){
            client.makeBankAccountsTransaction(amount, nameReceiver, ibanReceiver, description, addToAddress);
            changeScreenTo(Screens.BANKACCOUNT);
        } else {
            showErrorMessage("Amount and receiver details can not be empty.");
        }
    }

    public void chooseAddress() {
        Address address = (Address) tabelAddresses.getSelectionModel().getSelectedItem();
        tbNameReceiver.setText(address.getName());
        tbIbanReceiver.setText(address.getIban());
        changeScreenTo(Screens.NEWTRANSACTION);
    }

    public void makeBankAccountsRequest() {
        double amount = Double.parseDouble(tbEuroAmount.getText() + "," + tbCentAmount.getText());
        String nameReceiver = tbNameReceiver.getText();
        String ibanReceiver = tbIbanReceiver.getText();
        String description = tbDescription.getText();
        boolean addToAddress = cbAddToAddressBook.isSelected();
        if (amount > 0 && !nameReceiver.equals("") && !ibanReceiver.equals("")){
            client.makeBankAccountsRequest(amount, nameReceiver, ibanReceiver, description, addToAddress);
            changeScreenTo(Screens.BANKACCOUNT);
        } else {
            showErrorMessage("Amount and receiver details can not be empty.");
        }
    }

    public void openCreateAccount() {
        changeScreenTo(Screens.CREATEBANKACCOUNT);
    }

    public void openAccount() {
        changeScreenTo(Screens.ACCOUNT);
    }

    public void openLimits() {
        changeScreenTo(Screens.LIMITS);
    }

    public void openAddressBook() {
        changeScreenTo(Screens.ADDRESSBOOK);
    }

    public void openAddressBookTransaction() {
        changeScreenTo(Screens.ADDRESSBOOKTRANSACTION);
    }

    public void openTransaction() {
        changeScreenTo(Screens.NEWTRANSACTION);
    }

    public void openCreateBank() {
        changeScreenTo(Screens.CREATEBANK);
    }

    private void initAccountScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/account.fxml"));
        stage.setTitle("Account");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> changeScreenTo(Screens.BANKACCOUNT));
        stage.show();
    }

    private void initAddressBookScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/addressBook.fxml"));
        stage.setTitle("Address Book");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> changeScreenTo(Screens.BANKACCOUNT));
        stage.show();
    }

    private void initAddressBookTransactionScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/addressBookTransaction.fxml"));
        stage.setTitle("Address Book");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> changeScreenTo(Screens.NEWTRANSACTION));
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
        stage.setOnCloseRequest(event -> changeScreenTo(Screens.MANAGEBANKS));
        stage.show();
    }

    private void initCreateBankAccountScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/createBankAccount.fxml"));
        stage.setTitle("Create Bank Account");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> changeScreenTo(Screens.LOGIN));
        stage.show();
    }

    private void initLimitsScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Screens/limits.fxml"));
        stage.setTitle("Limits");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> changeScreenTo(Screens.BANKACCOUNT));
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
        stage.setOnCloseRequest(event -> changeScreenTo(Screens.BANKACCOUNT));
        stage.show();
    }

    private void showErrorMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ERROR!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void changeScreenTo(Screens screen) {
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
        }
    }
}
