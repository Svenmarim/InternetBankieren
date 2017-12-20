package Client;

/**
 * InternetBankieren Created by Sven de Vries on 20-12-2017
 */
public interface ControlledScreen {

    /**
     * This method will allow the injection of the Parent ScreenPane
     * @param screenParent The given screenparent
     */
    void setScreenParent(ScreensController screenParent);
}
