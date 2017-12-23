package Client.Controllers;

import Client.IControllers;
import Client.ScreensController;
import Shared.Address;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.rmi.RemoteException;
import java.util.List;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class AddressBookController implements IControllers {
    private ScreensController myController;

    //FXML fields
    public TableView tabelAddresses;
    public TableColumn clmName;
    public TableColumn clmIban;

    public void deleteBankAccountsAddress() {
        try {
            Address address = (Address) tabelAddresses.getSelectionModel().getSelectedItem();
            myController.getClient().deleteBankAccountsAddress(address);
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    public void setAddressBook(){
        try {
            List<Address> addressBook = myController.getClient().getAddressBook();
            clmName.setCellValueFactory(new PropertyValueFactory<Address, String>("name"));
            clmIban.setCellValueFactory(new PropertyValueFactory<Address, String>("iban"));
            tabelAddresses.setItems((FXCollections.observableArrayList(addressBook)));
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
