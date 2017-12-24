package Client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * InternetBankieren Created by Sven de Vries on 12-12-2017
 */
public class ClientMain extends Application {
    public static String screenAccountId = "account";
    public static String screenAccountFile = "Screens/account.fxml";
    public static String screenAddressBookId = "addressBook";
    public static String screenAddressBookFile = "Screens/addressBook.fxml";
    public static String screenAddressBookTransactionId = "addressBookTransaction";
    public static String screenAddressBookTransactionFile = "Screens/addressBookTransaction.fxml";
    public static String screenBankAccountId = "bankAccount";
    public static String screenBankAccountFile = "Screens/bankAccount.fxml";
    public static String screenCreateBankId = "createBank";
    public static String screenCreateBankFile = "Screens/createBank.fxml";
    public static String screenCreateBankAccountId = "createBankAccount";
    public static String screenCreateBankAccountFile = "Screens/createBankAccount.fxml";
    public static String screenLimitsId = "limits";
    public static String screenLimitsFile = "Screens/limits.fxml";
    public static String screenLoginId = "login";
    public static String screenLoginFile = "Screens/login.fxml";
    public static String screenManageBanksId = "manageBanks";
    public static String screenManageBanksFile = "Screens/manageBanks.fxml";
    public static String screenNewTransactionId = "newTransaction";
    public static String screenNewTransactionFile = "Screens/newTransaction.fxml";
    private static Stage primaryStage;
    private static ScreensController mainContainer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ClientMain.primaryStage = primaryStage;

        mainContainer = new ScreensController();
        mainContainer.loadScreen(ClientMain.screenAccountId, ClientMain.screenAccountFile);
        mainContainer.loadScreen(ClientMain.screenAddressBookId, ClientMain.screenAddressBookFile);
        mainContainer.loadScreen(ClientMain.screenAddressBookTransactionId, ClientMain.screenAddressBookTransactionFile);
        mainContainer.loadScreen(ClientMain.screenBankAccountId, ClientMain.screenBankAccountFile);
        mainContainer.loadScreen(ClientMain.screenCreateBankId, ClientMain.screenCreateBankFile);
        mainContainer.loadScreen(ClientMain.screenCreateBankAccountId, ClientMain.screenCreateBankAccountFile);
        mainContainer.loadScreen(ClientMain.screenLimitsId, ClientMain.screenLimitsFile);
        mainContainer.loadScreen(ClientMain.screenLoginId, ClientMain.screenLoginFile);
        mainContainer.loadScreen(ClientMain.screenManageBanksId, ClientMain.screenManageBanksFile);
        mainContainer.loadScreen(ClientMain.screenNewTransactionId, ClientMain.screenNewTransactionFile);

        mainContainer.setScreen(ClientMain.screenLoginId);

        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void setProperties(String name){
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        switch (name){
            case "account":
                primaryStage.setTitle("Account");
                primaryStage.setOnCloseRequest(event -> {
                    event.consume();
                    mainContainer.setScreen(screenBankAccountId);
                });
                mainContainer.getAccountController().setAccountDetails();
                break;
            case "addressBook":
                primaryStage.setTitle("Address Book");
                primaryStage.setOnCloseRequest(event -> {
                    event.consume();
                    mainContainer.setScreen(screenBankAccountId);
                });
                mainContainer.getAddressBookController().setAddressBook();
                break;
            case "addressBookTransaction":
                primaryStage.setTitle("Address Book");
                primaryStage.setOnCloseRequest(event -> {
                    event.consume();
                    mainContainer.setScreen(screenNewTransactionId);
                });
                mainContainer.getAddressBookTransactionController().setAddressBook();
                break;
            case "bankAccount":
                primaryStage.setTitle("Bank Account");
                primaryStage.setOnCloseRequest(event -> {
                    event.consume();
                    mainContainer.getBankAccountController().logoutClient();
                });
                mainContainer.getBankAccountController().setAccountDetails();
                mainContainer.getBankAccountController().setTransactionHistory();
                break;
            case "createBank":
                primaryStage.setTitle("Create Bank");
                primaryStage.setOnCloseRequest(event -> {
                    event.consume();
                    mainContainer.setScreen(screenManageBanksId);
                });
                break;
            case "createBankAccount":
                primaryStage.setTitle("Create Bank Account");
                primaryStage.setOnCloseRequest(event -> {
                    event.consume();
                    mainContainer.setScreen(screenLoginId);
                });
                break;
            case "limits":
                primaryStage.setTitle("Limits");
                primaryStage.setOnCloseRequest(event -> {
                    event.consume();
                    mainContainer.setScreen(screenBankAccountId);
                });
                mainContainer.getLimitsController().setLimits();
                break;
            case "login":
                primaryStage.setTitle("Login");
                primaryStage.setOnCloseRequest(event -> System.exit(0));
                break;
            case "manageBanks":
                primaryStage.setTitle("Manage Banks");
                primaryStage.setOnCloseRequest(event -> {
                    event.consume();
                    mainContainer.getManageBanksController().logoutAdmin();
                });
                mainContainer.getManageBanksController().setBanks();
                break;
            case "newTransaction":
                primaryStage.setTitle("New Transaction");
                primaryStage.setOnCloseRequest(event -> {
                    event.consume();
                    mainContainer.setScreen(screenBankAccountId);
                });
                break;
        }
    }
}
