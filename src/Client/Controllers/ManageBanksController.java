package Client.Controllers;

import Client.ControlledScreen;
import Client.ScreensController;
import javafx.scene.control.TableView;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class ManageBanksController implements ControlledScreen {
    private ScreensController myController;

    //FXML fields
    public TableView tabelBanks;

    public void openCreateBank() {
//        changeScreenTo(Screens.CREATEBANK);
    }

    public void deleteBank() {
//        String bank = String.valueOf(tabelBanks.getSelectionModel().getSelectedItem());
//        String[] bankParts = bank.split(";");
//        String bankName = bankParts[0];
//        client.deleteBank(bankName);
    }

    public void logoutAdmin() {
//        try {
//            client.logout();
//            changeScreenTo(Screens.LOGIN);
//        } catch (RemoteException e) {
//            showErrorMessage(e.getMessage());
//        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
