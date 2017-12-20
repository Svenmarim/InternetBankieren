package Client.Controllers;

import Client.ControlledScreen;
import Client.ScreensController;
import javafx.scene.control.TextField;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class CreateBankController implements ControlledScreen {
    private ScreensController myController;

    //FXML fields
    public TextField tbBankName;
    public TextField tbShortcut;

    public void createBank() {
//        String name = tbBankName.getText();
//        String shortcut = tbShortcut.getText();
//        try {
//            if (client.createBank(name, shortcut)){
//                changeScreenTo(Screens.MANAGEBANKS);
//            } else {
//                showErrorMessage("Bank name or shortcut already excist.");
//            }
//        } catch (RemoteException e) {
//            showErrorMessage(e.getMessage());
//        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
