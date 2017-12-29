package Client.Controllers;

import Client.ClientMain;
import Client.IControllers;
import Client.ScreensController;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class NewTransactionController implements IControllers {
    private ScreensController myController;

    //FXML fields
    public TextField tbEuroAmount;
    public TextField tbCentAmount;
    public TextField tbNameReceiver;
    public TextField tbIbanReceiver;
    public CheckBox cbAddToAddressBook;
    public TextArea tbDescription;

    public void openAddressBookTransaction() {
        myController.setScreen(ClientMain.screenAddressBookTransactionId);
    }

    public void makeBankAccountsRequest() {
        double amount = Double.parseDouble(tbEuroAmount.getText() + "." + tbCentAmount.getText());
        amount = amount*100;
        amount = (double)((int) amount);
        amount = amount /100;
        String nameReceiver = tbNameReceiver.getText();
        String ibanReceiver = tbIbanReceiver.getText();
        String description = tbDescription.getText();
        boolean addToAddress = cbAddToAddressBook.isSelected();
        if (amount > 0 && !nameReceiver.equals("") && !ibanReceiver.equals("")){
            try {
                if (myController.getClient().makeBankAccountsRequest(amount, nameReceiver, ibanReceiver, description, addToAddress)){
                    myController.setScreen(ClientMain.screenBankAccountId);
                } else {
                    myController.showErrorMessage("The request is not sended.");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            myController.showErrorMessage("Amount and receiver details can not be empty.");
        }
    }

    public void makeBankAccountsTransaction() {
        double amount = Double.parseDouble(tbEuroAmount.getText() + "." + tbCentAmount.getText());
        amount = amount*100;
        amount = (double)((int) amount);
        amount = amount /100;
        String nameReceiver = tbNameReceiver.getText();
        String ibanReceiver = tbIbanReceiver.getText();
        String description = tbDescription.getText();
        boolean addToAddress = cbAddToAddressBook.isSelected();
        if (amount > 0 && !nameReceiver.equals("") && !ibanReceiver.equals("")){
            try {
                if (myController.getClient().makeBankAccountsTransaction(amount, nameReceiver, ibanReceiver, description, addToAddress)){
                    myController.setScreen(ClientMain.screenBankAccountId);
                } else {
                    myController.showErrorMessage("Check if other bank is online, otherwise amount or receiver details are not valid or you are trying to transfer a higher amount of money that is above your transfer limit.");
                }
            } catch (RemoteException e) {
                myController.showErrorMessage(e.getMessage());
            }
        } else {
            myController.showErrorMessage("Amount and receiver details can not be empty.");
        }
    }

    public void setCheckboxEnable(){
        cbAddToAddressBook.setDisable(false);
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
