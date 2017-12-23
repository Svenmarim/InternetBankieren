package Client.Controllers;

import Client.ClientMain;
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
public class AddressBookTransactionController implements IControllers {
    private ScreensController myController;

    //FXML fields
    public TableView tabelAddresses;
    public TableColumn clmName;
    public TableColumn clmIban;

    public void chooseAddress() {
        Address address = (Address) tabelAddresses.getSelectionModel().getSelectedItem();
        myController.getNewTransactionController().tbNameReceiver.setText(address.getName());
        myController.getNewTransactionController().tbIbanReceiver.setText(address.getIban());
        myController.getNewTransactionController().cbAddToAddressBook.setSelected(false);
        myController.getNewTransactionController().cbAddToAddressBook.setDisable(true);
        myController.setScreen(ClientMain.screenNewTransactionId);
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
