package Client.Controllers;

import Client.ControlledScreen;
import Client.ScreensController;
import javafx.scene.control.TextField;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class LimitsController implements ControlledScreen {
    private ScreensController myController;

    //FXML fields
    public TextField tbEuroIn;
    public TextField tbCentIn;
    public TextField tbEuroOut;
    public TextField tbCentOut;

    public void editBankAccountsLimits() {
//        double limitIn = Double.parseDouble(tbEuroIn.getText() + "," + tbCentIn.getText());
//        double limitOut = Double.parseDouble(tbEuroOut.getText() + "," + tbCentOut.getText());
//        if (limitIn > 0 && limitOut > 0){
//            client.editBankAccountsLimits(limitIn, limitOut);
//            changeScreenTo(Screens.BANKACCOUNT);
//        } else {
//            showErrorMessage("Limits can not be 0.");
//        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
