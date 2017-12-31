package Client.Controllers;

import Client.ClientMain;
import Client.IControllers;
import Client.ScreensController;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.text.DecimalFormat;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class LimitsController implements IControllers {
    private ScreensController myController;
    private static DecimalFormat df = new DecimalFormat("#.##");

    //FXML fields
    public TextField tbEuroIn;
    public TextField tbCentIn;
    public TextField tbEuroOut;
    public TextField tbCentOut;

    public void editBankAccountsLimits() {
        double limitIn = Double.parseDouble(tbEuroIn.getText() + "." + tbCentIn.getText());
        limitIn = limitIn * 100;
        limitIn = (double) ((int) limitIn);
        limitIn = limitIn / 100;
        double limitOut = Double.parseDouble(tbEuroOut.getText() + "." + tbCentOut.getText());
        limitOut = limitOut * 100;
        limitOut = (double) ((int) limitOut);
        limitOut = limitOut / 100;
        if (limitIn > 0 && limitOut > 0) {
            try {
                if (myController.getClient().isSessionValid()) {
                    myController.getClient().editBankAccountsLimits(limitIn, limitOut);
                    myController.setScreen(ClientMain.screenBankAccountId);
                } else {
                    myController.showErrorMessage("Session has ended because of inactivity for more then 5 minutes.");
                    myController.getClient().logout();
                    myController.setScreen(ClientMain.screenLoginId);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            myController.showErrorMessage("Limits can not be 0 or negative.");
        }
    }

    public void setLimits() {
        try {
            double limitIn = myController.getClient().getLimitIn();
            double limitOut = myController.getClient().getLimitOut();
            int inEuro = (int) limitIn;
            int inCent = (int) (limitIn * 100 - inEuro * 100);

            int outEuro = (int) limitOut;
            int outCent = (int) (limitOut * 100 - outEuro * 100);

            tbEuroIn.setText(((Integer) inEuro).toString());
            tbCentIn.setText(String.format("%02d", inCent));
            tbEuroOut.setText(((Integer) outEuro).toString());
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
