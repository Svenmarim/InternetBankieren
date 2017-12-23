package Client.Controllers;

import Client.ClientMain;
import Client.IControllers;
import Client.ScreensController;
import Shared.IBankForCentralBank;
import Shared.TempBank;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.rmi.RemoteException;
import java.util.List;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class ManageBanksController implements IControllers {
    private ScreensController myController;

    //FXML fields
    public TableView tabelBanks;
    public TableColumn clmName;
    public TableColumn clmShortcut;

    public void openCreateBank() {
        myController.setScreen(ClientMain.screenCreateBankId);
    }

    public void deleteBank() {
        try {
            TempBank bank = (TempBank) tabelBanks.getSelectionModel().getSelectedItem();
            myController.getClient().deleteBank(bank);
            setBanks();
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    public void logoutAdmin() {
        try {
            myController.getClient().logout();
            myController.setScreen(ClientMain.screenLoginId);
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    public void setBanks(){
        try {
            List<TempBank> banks = myController.getClient().getBanks();
            clmName.setCellValueFactory(new PropertyValueFactory<TempBank, String>("name"));
            clmShortcut.setCellValueFactory(new PropertyValueFactory<TempBank, String>("shortcut"));
            tabelBanks.setItems((FXCollections.observableArrayList(banks)));
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
