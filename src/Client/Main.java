package Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * InternetBankieren Created by Sven de Vries on 12-12-2017
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Screens/login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:assets/ideal_logo.jpg"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }
}
