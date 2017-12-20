package Client.Controllers;

import Client.ControlledScreen;
import Client.ScreensController;
import javafx.scene.control.TableView;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class AddressBookTransactionController implements ControlledScreen {
    private ScreensController myController;

    //FXML fields
    public TableView tabelAddresses;

    public void chooseAddress() {
//        Address address = (Address) tabelAddresses.getSelectionModel().getSelectedItem();
//        tbNameReceiver.setText(address.getName());
//        tbIbanReceiver.setText(address.getIban());
//        changeScreenTo(Screens.NEWTRANSACTION);
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
