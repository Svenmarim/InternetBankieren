package Client.Controllers;

import Client.ClientMain;
import Client.IControllers;
import Client.ScreensController;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class LimitsController implements IControllers {
    private ScreensController myController;

    //FXML fields
    public TextField tbEuroIn;
    public TextField tbCentIn;
    public TextField tbEuroOut;
    public TextField tbCentOut;

    public void editBankAccountsLimits() {
        double limitIn = Double.parseDouble(tbEuroIn.getText() + "." + tbCentIn.getText());
        double limitOut = Double.parseDouble(tbEuroOut.getText() + "." + tbCentOut.getText());//TODO round to max 2 numbers
        if (limitIn > 0 && limitOut > 0) {
            try {
                myController.getClient().editBankAccountsLimits(limitIn, limitOut);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            myController.setScreen(ClientMain.screenBankAccountId);
        } else {
            myController.showErrorMessage("Limits can not be 0 or negative.");
        }
    }

    public void setLimits() {
        try {
            double limitIn = myController.getClient().getLimitIn();
            double limitOut = myController.getClient().getLimitOut();
            int inEuro = (int) limitIn;
            int inCent = (int) ((limitIn - inEuro) * 100);

            int outEuro = (int) limitOut;
            int outCent = (int) ((limitOut - outEuro) * 100);

            tbEuroIn.setText(((Integer)inEuro).toString());
            tbCentIn.setText(String.format("%02d", inCent));
            tbEuroOut.setText(((Integer)outEuro).toString());
            tbCentOut.setText(String.format("%02d", outCent));
        } catch (RemoteException e) {
            myController.showErrorMessage(e.getMessage());
        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
