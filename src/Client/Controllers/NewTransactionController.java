package Client.Controllers;

import Client.ControlledScreen;
import Client.ScreensController;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public class NewTransactionController implements ControlledScreen {
    private ScreensController myController;

    //FXML fields
    public TextField tbEuroAmount;
    public TextField tbCentAmount;
    public TextField tbNameReceiver;
    public TextField tbIbanReceiver;
    public CheckBox cbAddToAddressBook;
    public TextArea tbDescription;

    public void openAddressBookTransaction() {
//        changeScreenTo(Screens.ADDRESSBOOKTRANSACTION);
    }

    public void makeBankAccountsRequest() {
//        double amount = Double.parseDouble(tbEuroAmount.getText() + "," + tbCentAmount.getText());
//        String nameReceiver = tbNameReceiver.getText();
//        String ibanReceiver = tbIbanReceiver.getText();
//        String description = tbDescription.getText();
//        boolean addToAddress = cbAddToAddressBook.isSelected();
//        if (amount > 0 && !nameReceiver.equals("") && !ibanReceiver.equals("")){
//            client.makeBankAccountsRequest(amount, nameReceiver, ibanReceiver, description, addToAddress);
//            changeScreenTo(Screens.BANKACCOUNT);
//        } else {
//            showErrorMessage("Amount and receiver details can not be empty.");
//        }
    }

    public void makeBankAccountsTransaction() {
//        double amount = Double.parseDouble(tbEuroAmount.getText() + "," + tbCentAmount.getText());
//        String nameReceiver = tbNameReceiver.getText();
//        String ibanReceiver = tbIbanReceiver.getText();
//        String description = tbDescription.getText();
//        boolean addToAddress = cbAddToAddressBook.isSelected();
//        if (amount > 0 && !nameReceiver.equals("") && !ibanReceiver.equals("")){
//            client.makeBankAccountsTransaction(amount, nameReceiver, ibanReceiver, description, addToAddress);
//            changeScreenTo(Screens.BANKACCOUNT);
//        } else {
//            showErrorMessage("Amount and receiver details can not be empty.");
//        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
