package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.net.URL;
import java.util.Objects;

public class SceneSwitch extends Application {

    private Stage primaryStage;


    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Login.fxml")));
        this.primaryStage.setScene(new Scene(root));
        this.primaryStage.setTitle("StockFX");
        this.primaryStage.setResizable(false);
        this.primaryStage.show();
    }


    public void sceneSwitch(String fxmlPath, Button b) {

        try {
            URL url = Objects.requireNonNull(getClass().getClassLoader().getResource(fxmlPath)).toURI().toURL();
            Parent root = FXMLLoader.load(url);
            Stage stage = (Stage) b.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            this.primaryStage = stage;
            this.primaryStage.setTitle("StockFX");
            this.primaryStage.show();

        } catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Back to Login");
            alert.setContentText("It is not possible to switch to Login because techincal problem!");
            alert.show();
        }
    }
}