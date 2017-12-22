package Client.Controllers;

import Client.ClientMain;
import Client.IControllers;
import Client.ScreensController;
import Shared.IBankForCentralBank;
import Shared.TempBank;
import javafx.scene.control.TableView;

import java.rmi.RemoteException;
import java.util.List;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class ManageBanksController implements IControllers {
    private ScreensController myController;

    //FXML fields
    public TableView tabelBanks;

    public void openCreateBank() {
        myController.setScreen(ClientMain.screenCreateBankId);
    }

    public void deleteBank() {
        try {
            TempBank bank = (TempBank) tabelBanks.getSelectionModel().getSelectedItem();
            myController.getClient().deleteBank(bank);
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

    public void setBanks(List<IBankForCentralBank> banks){
        //TODO set banks
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
