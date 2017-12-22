package Client;

import Client.Controllers.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * InternetBankieren Created by Sven de Vries on 13-12-2017
 */
public class ScreensController extends StackPane {

    private HashMap<String, Node> screens = new HashMap<>();
    private Client client;
    private NewTransactionController newTransactionController;
    private BankAccountController bankAccountController;
    private ManageBanksController manageBanksController;
    private LimitsController limitsController;
    private AddressBookController addressBookController;
    private AddressBookTransactionController addressBookTransactionController;

    public Client getClient() {
        return this.client;
    }

    public NewTransactionController getNewTransactionController() {
        return this.newTransactionController;
    }

    public BankAccountController getBankAccountController() {
        return this.bankAccountController;
    }

    public ManageBanksController getManageBanksController() {
        return this.manageBanksController;
    }

    public LimitsController getLimitsController() {
        return this.limitsController;
    }

    public AddressBookController getAddressBookController() {
        return this.addressBookController;
    }

    public AddressBookTransactionController getAddressBookTransactionController() {
        return this.addressBookTransactionController;
    }

    public ScreensController() {
        try {
            this.client = new Client();
            System.out.println("Client: Client created");
        } catch (RemoteException e) {
            System.out.println("Client: Cannot create Client");
            System.out.println("Client: RemoteException: " + e.getMessage());
            System.exit(0);
        }
    }

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
            IControllers myScreenController = myLoader.getController();
            if (myScreenController instanceof NewTransactionController) {
                this.newTransactionController = (NewTransactionController) myScreenController;
            } else if (myScreenController instanceof BankAccountController) {
                this.bankAccountController = (BankAccountController) myScreenController;
            } else if (myScreenController instanceof ManageBanksController) {
                this.manageBanksController = (ManageBanksController) myScreenController;
            } else if (myScreenController instanceof LimitsController) {
                this.limitsController = (LimitsController) myScreenController;
            } else if (myScreenController instanceof AddressBookController) {
                this.addressBookController = (AddressBookController) myScreenController;
            } else if (myScreenController instanceof AddressBookTransactionController) {
                this.addressBookTransactionController = (AddressBookTransactionController) myScreenController;
            }
            myScreenController.setScreenParent(this);
            addScreen(name, loadScreen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setScreen(final String name) {
        if (screens.get(name) != null) {
            final DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty()) {
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(200), event -> {
                            getChildren().remove(0);
                            getChildren().add(0, screens.get(name));
                            ClientMain.setProperties(name);
                            Timeline fadeIn = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                    new KeyFrame(new Duration(200), new KeyValue(opacity, 1.0)));
                            fadeIn.play();
                        }, new KeyValue(opacity, 0.0)));
                fade.play();
            } else {
                //First time start up
                setOpacity(0.0);
                getChildren().add(screens.get(name));
                ClientMain.setProperties(name);
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(2000), new KeyValue(opacity, 1.0)));
                fadeIn.play();
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

    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ERROR!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
