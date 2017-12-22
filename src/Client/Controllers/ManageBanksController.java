package Client.Controllers;

import Client.ClientMain;
import Client.IControllers;
import Client.ScreensController;
import javafx.scene.control.TableView;

import java.rmi.RemoteException;

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
        String bank = String.valueOf(tabelBanks.getSelectionModel().getSelectedItem());
        String[] bankParts = bank.split(";");
        String bankName = bankParts[0];
        myController.getClient().deleteBank(bankName);
    }

    public void logoutAdmin() {
        try {
            myController.getClient().logout();
            myController.setScreen(ClientMain.screenLoginId);
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
