package Client.Controllers;

import Client.ClientMain;
import Client.IControllers;
import Client.ScreensController;
import Shared.Address;
import javafx.scene.control.TableView;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class AddressBookTransactionController implements IControllers {
    private ScreensController myController;

    //FXML fields
    public TableView tabelAddresses;

    public void chooseAddress() {
        Address address = (Address) tabelAddresses.getSelectionModel().getSelectedItem();
        myController.getNewTransactionController().tbNameReceiver.setText(address.getName());
        myController.getNewTransactionController().tbIbanReceiver.setText(address.getIban());
        myController.setScreen(ClientMain.screenNewTransactionId);
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
