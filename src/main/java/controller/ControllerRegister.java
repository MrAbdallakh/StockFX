package controller;

import algorithm.AccountInformation;
import algorithm.SHA256;
import app.SceneSwitch;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerRegister implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private PasswordField password, confirmedPassword;

    @FXML
    private TextField username;

    @FXML
    private Button back;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pane.setOnKeyPressed((KeyEvent event) -> {

            if (event.getCode() == KeyCode.ESCAPE) {
                backLogin();

            } else if (event.getCode() == KeyCode.ENTER) {
                createAccount();
            }
        });
    }


    @FXML
    private void onActionBack() {
        backLogin();
    }


    private void backLogin() { new SceneSwitch().sceneSwitch("Login.fxml", back); }


    @FXML
    private void onActionCreate() { createAccount(); }


    private void createAccount() {

        if (!username.getText().isEmpty() && !password.getText().isEmpty() && !confirmedPassword.getText().isEmpty()) {
            if (!password.getText().equals(confirmedPassword.getText())) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Register failed");
                alert.setContentText("Password and Confirmed Password have to be the same!");
                alert.show();

            } else if(password.getText().length() < 8){

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Register failed");
                alert.setContentText("The minimum size of password have to be 8 characters!");
                alert.show();

            }else {

                File dir = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\StockFX");
                if (!dir.exists()) {
                    dir.mkdirs();
                }


                File file = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\StockFX\\" + username.getText() + ".txt");

                try {
                    if (!file.createNewFile()) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Register failed");
                        alert.setContentText("The Username already exist, please take a other name to register!");
                        alert.show();
                    } else {

                        new AccountInformation().createAccount(file, new SHA256(password.getText()).getValue());

                        new SceneSwitch().sceneSwitch("Login.fxml", back);
                    }
                } catch (Exception e) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Register failed");
                    alert.setContentText("Account is not possible to register because technical problem!");
                    alert.show();
                }
            }
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Register failed");
            alert.setContentText("The TextFields are empty, please enter your username and password!");
            alert.show();
        }
    }
}