package controller;

import algorithm.AccountInformation;
import algorithm.SHA256;
import app.SceneSwitch;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerLogin implements Initializable {

    @FXML
    private AnchorPane pane;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private Button createAccount, login;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pane.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {
                try {
                    login();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    @FXML
    private void onActionLogin() {
        try {
            login();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login failed");
            alert.setContentText("Login doesn't work!");
            alert.show();
        }
    }


    private void login() {
        if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
            File file = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\StockFX\\" + username.getText() + ".txt");

            try {
                if (new AccountInformation().checkUser(file, username.getText(), new SHA256(password.getText()).getValue())) {

                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    String line = bufferedReader.readLine();
                    bufferedReader.close();

                    if (Boolean.parseBoolean(line)) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login failed");
                        alert.setContentText("Account is closed!");
                        alert.show();
                    } else {

                        URL url = Objects.requireNonNull(getClass().getClassLoader().getResource("Stock.fxml")).toURI().toURL();
                        Parent root = FXMLLoader.load(url);
                        Stage stage = (Stage) login.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setResizable(false);

                        new SceneSwitch().sceneSwitch(stage);
                    }
                } else {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login failed");
                    alert.setContentText("Password or Username is incorrect!");
                    alert.show();
                }
            } catch (Exception e) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login failed");
                alert.setContentText("Password can not be confirmed because of technical problem!");
                alert.show();
            }
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login failed");
            alert.setContentText("Both fields have to be filled!");
            alert.show();
        }
    }


    @FXML
    private void onActionCreateAccount() throws Exception {

        URL url = Objects.requireNonNull(getClass().getClassLoader().getResource("Register.fxml")).toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) createAccount.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);

        new SceneSwitch().sceneSwitch(stage);

    }
}