package Client.Controllers;

import Client.IControllers;
import Client.ScreensController;
import Shared.Address;
import javafx.scene.control.TableView;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class AddressBookController implements IControllers {
    private ScreensController myController;

    //FXML fields
    public TableView tabelAddresses;

    public void deleteBankAccountsAddress() {
        Address address = (Address) tabelAddresses.getSelectionModel().getSelectedItem();
        myController.getClient().deleteBankAccountsAddress(address);
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
