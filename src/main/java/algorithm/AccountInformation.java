package algorithm;

import javafx.scene.control.Alert;

import java.io.*;

public class AccountInformation {

    // Save and Load is in ControllerStock.java

    private String name, password;

    public String getName(){
        return name;
    }

    public String getPassword(){
        return password;
    }


    public void createAccount(File file, String password) {

        try {
            FileWriter writer = new FileWriter(file);
            writer.write("false\n");
            writer.write("Password:" + password + "\n");
            writer.write("Account:1000");
            writer.close();

        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Register failed");
            alert.setContentText("Account is not possible to register because technical problem!");
            alert.show();
        }
    }


    public boolean checkUser(File file, String username, String checkPassword) throws IOException {

        if (!file.exists()){
            return false;
        }

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        bufferedReader.readLine();
        String line = bufferedReader.readLine();

        bufferedReader.close();

        name = username;

        String[] splitLine = line.split(":");

        password = splitLine[1];

        return splitLine[1].equals(checkPassword);
    }
}