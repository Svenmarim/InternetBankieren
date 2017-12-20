package Client;

import Shared.Address;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.activation.MimeTypeParameterList;
import java.io.IOException;
import java.rmi.RemoteException;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

/**
 * InternetBankieren Created by Sven de Vries on 13-12-2017
 */
public class ScreensController extends StackPane {

    private HashMap<String, Node> screens = new HashMap<>();
    private Client client;

    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    public Node getScreen(String name) {
        return screens.get(name);
    }

    public void loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = myLoader.load();
            ControlledScreen myScreenController = myLoader.getController();
            myScreenController.setScreenParent(this);
            addScreen(name, loadScreen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setScreen(final String name) {
        if (screens.get(name) != null) {
//            final DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty()) {
//                Timeline fade = new Timeline(
//                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
//                        new KeyFrame(new Duration(200), new EventHandler<ActionEvent>() {
//                            @Override
//                            public void handle(ActionEvent event) {
                                getChildren().remove(0);
                                getChildren().add(0, screens.get(name));
                                ClientMain.resizeScreen();
//                                Timeline fadeIn = new Timeline(
//                                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
//                                        new KeyFrame(new Duration(200), new KeyValue(opacity, 1.0)));
//                                fadeIn.play();
//                            }
//                        }, new KeyValue(opacity, 0.0)));
//                fade.play();
            } else {
                //First time start up
//                setOpacity(0.0);
                getChildren().add(screens.get(name));
                ClientMain.resizeScreen();
//                Timeline fadeIn = new Timeline(
//                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
//                        new KeyFrame(new Duration(2000), new KeyValue(opacity, 1.0)));
//                fadeIn.play();
            }
        } else {
            System.out.println("Screen hasn't been loaded!!!");
        }
    }

    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }

//    public ScreensController() {
//        try {
//            client = new Client();
//            System.out.println("Client: Client created");
//        } catch (RemoteException e) {
//            System.out.println("Client: Cannot create Client");
//            System.out.println("Client: RemoteException: " + e.getMessage());
//            System.exit(0);
//        }
//    }
//
//
//    private void initAccountScreen() throws IOException {
//        Stage stage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("Screens/account.fxml"));
//        stage.setTitle("Account");
//        stage.setResizable(false);
//        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
//        stage.setScene(new Scene(root));
//        stage.setOnCloseRequest(event -> changeScreenTo(Screens.BANKACCOUNT));
//        stage.show();
//    }
//
//    private void initAddressBookScreen() throws IOException {
//        Stage stage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("Screens/addressBook.fxml"));
//        stage.setTitle("Address Book");
//        stage.setResizable(false);
//        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
//        stage.setScene(new Scene(root));
//        stage.setOnCloseRequest(event -> changeScreenTo(Screens.BANKACCOUNT));
//        stage.show();
//    }
//
//    private void initAddressBookTransactionScreen() throws IOException {
//        Stage stage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("Screens/addressBookTransaction.fxml"));
//        stage.setTitle("Address Book");
//        stage.setResizable(false);
//        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
//        stage.setScene(new Scene(root));
//        stage.setOnCloseRequest(event -> changeScreenTo(Screens.NEWTRANSACTION));
//        stage.show();
//    }
//
//    private void initBankAccountScreen() throws IOException {
//        Stage stage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("Screens/bankAccount.fxml"));
//        stage.setTitle("Bank Account");
//        stage.setResizable(false);
//        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
//        stage.setScene(new Scene(root));
//        stage.setOnCloseRequest(event -> logoutClient());
//        stage.show();
//    }
//
//    private void initCreateBankScreen() throws IOException {
//        Stage stage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("Screens/createBank.fxml"));
//        stage.setTitle("Create Bank");
//        stage.setResizable(false);
//        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
//        stage.setScene(new Scene(root));
//        stage.setOnCloseRequest(event -> changeScreenTo(Screens.MANAGEBANKS));
//        stage.show();
//    }
//
//    private void initCreateBankAccountScreen() throws IOException {
//        Stage stage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("Screens/createBankAccount.fxml"));
//        stage.setTitle("Create Bank Account");
//        stage.setResizable(false);
//        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
//        stage.setScene(new Scene(root));
//        stage.setOnCloseRequest(event -> changeScreenTo(Screens.LOGIN));
//        stage.show();
//    }
//
//    private void initLimitsScreen() throws IOException {
//        Stage stage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("Screens/limits.fxml"));
//        stage.setTitle("Limits");
//        stage.setResizable(false);
//        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
//        stage.setScene(new Scene(root));
//        stage.setOnCloseRequest(event -> changeScreenTo(Screens.BANKACCOUNT));
//        stage.show();
//    }
//
//    private void initLoginScreen() throws IOException {
//        Stage stage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("Screens/login.fxml"));
//        stage.setTitle("Login");
//        stage.setResizable(false);
//        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
//        stage.setScene(new Scene(root));
//        stage.setOnCloseRequest(event -> System.exit(0));
//        stage.show();
//    }
//
//    private void initManageBanksScreen() throws IOException {
//        Stage stage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("Screens/manageBanks.fxml"));
//        stage.setTitle("Manage Banks");
//        stage.setResizable(false);
//        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
//        stage.setScene(new Scene(root));
//        stage.setOnCloseRequest(event -> logoutAdmin());
//        stage.show();
//    }
//
//    private void initnewTransactionScreen() throws IOException {
//        Stage stage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("Screens/newTransaction.fxml"));
//        stage.setTitle("New Transaction");
//        stage.setResizable(false);
//        stage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
//        stage.setScene(new Scene(root));
//        stage.setOnCloseRequest(event -> changeScreenTo(Screens.BANKACCOUNT));
//        stage.show();
//    }
//
//    private void showErrorMessage(String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("ERROR!");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
}
