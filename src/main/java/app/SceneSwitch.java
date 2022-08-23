package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


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


    public void sceneSwitch(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("StockFX");
        this.primaryStage.show();
    }
}