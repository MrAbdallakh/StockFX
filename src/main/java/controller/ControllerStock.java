package controller;

import algorithm.AccountInformation;
import app.SceneSwitch;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ControllerStock implements Initializable {

    @FXML
    private Label accountMoney, costStock, winLoseStock;

    @FXML
    private Button signOut;

    @FXML
    private TextField input;

    @FXML
    private TextArea protocol;

    private final HashMap<String, LinkedList<String>> stockProtocol = new HashMap<>();

    private final DecimalFormat decimalFormat = new DecimalFormat(".00");

    private String stockName = "AAPL", name, password;
    private double stockPrice;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.name = new AccountInformation().getName();
        this.password = new AccountInformation().getPassword();

        stockProtocol.put("AAPL", new LinkedList<>());
        stockProtocol.put("AMD", new LinkedList<>());
        stockProtocol.put("AMZN", new LinkedList<>());
        stockProtocol.put("BBY", new LinkedList<>());
        stockProtocol.put("DASH", new LinkedList<>());
        stockProtocol.put("DIS", new LinkedList<>());
        stockProtocol.put("DO", new LinkedList<>());
        stockProtocol.put("ET", new LinkedList<>());
        stockProtocol.put("FVRR", new LinkedList<>());
        stockProtocol.put("GOOG", new LinkedList<>());
        stockProtocol.put("INTC", new LinkedList<>());
        stockProtocol.put("META", new LinkedList<>());
        stockProtocol.put("MSFT", new LinkedList<>());
        stockProtocol.put("NFLX", new LinkedList<>());
        stockProtocol.put("ORCL", new LinkedList<>());
        stockProtocol.put("PINS", new LinkedList<>());
        stockProtocol.put("SNAP", new LinkedList<>());
        stockProtocol.put("TSLA", new LinkedList<>());
        stockProtocol.put("TWTR", new LinkedList<>());
        stockProtocol.put("UBER", new LinkedList<>());

        loadAccount();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1.0), event -> {
                    Stock stock;

                    try {

                        stock = YahooFinance.get(this.stockName);
                        this.stockPrice = Double.parseDouble(decimalFormat.format(Objects.requireNonNull(stock).getQuote().getPrice()));

                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Stock error");
                        alert.setContentText("Stock is not possible not get the Price.");
                        alert.show();
                    }


                    // Calculate the win/lose from buying stock(s)

                    double value, buyingStockPrice = 0;

                    for (String line : this.stockProtocol.get(this.stockName)) {

                        String[] splitLine = line.split(";");

                        double multiplyValue = this.stockPrice / Double.parseDouble(splitLine[1]);
                        value = Double.parseDouble(this.decimalFormat.format(Double.parseDouble(splitLine[2]) * multiplyValue));
                        buyingStockPrice += (value - Double.parseDouble(splitLine[2]));

                    }

                    winLoseStock.setText(String.valueOf(this.decimalFormat.format(buyingStockPrice)));

                    if(buyingStockPrice > 0){
                       winLoseStock.setTextFill(Color.GREEN);
                    }else if (buyingStockPrice < 0){
                        winLoseStock.setTextFill(Color.RED);
                    }else{
                        winLoseStock.setTextFill(Color.WHITE);
                    }


                    accountMoney.setText(String.valueOf(this.decimalFormat.format(Double.parseDouble(accountMoney.getText()))));


                    // Value change of costStock

                    StringBuilder text = new StringBuilder();
                    text.append(this.stockName).append(":\n\n");


                    for (String line : this.stockProtocol.get(this.stockName)) {
                        String[] splitLine = line.split(";");
                        text.append(splitLine[0]).append(": ").append(splitLine[1]).append(" bought with ").append(splitLine[2]).append("\n");
                    }

                    protocol.setText(text.toString());
                    costStock.setText(this.stockPrice + " $");
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    @FXML
    private void onActionStock(ActionEvent event) {
        Button button = (Button) event.getSource();
        this.stockName = button.getText();


        try {

            Stock stock = YahooFinance.get(this.stockName);
            this.stockPrice = Double.parseDouble(decimalFormat.format(Objects.requireNonNull(stock).getQuote().getPrice()));

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Stock error");
            alert.setContentText("Stock is not possible not get the Price.");
            alert.show();
        }

        protocol.setText(this.stockName + ":\n\n");
        costStock.setText(this.stockPrice + " $");
    }


    @FXML
    private void onActionBuy() {

        try {
            if (Double.parseDouble(accountMoney.getText()) >= Double.parseDouble(input.getText())) {

                accountMoney.setText(String.valueOf(Double.parseDouble(accountMoney.getText()) - Double.parseDouble(input.getText())));

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                stockProtocol.get(this.stockName).add(simpleDateFormat.format(new Date()) + ";" + this.stockPrice + ";" + input.getText());
                this.input.clear();
                saveAccount(false);

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Buy Stock");
                alert.setContentText("You have not enough money to buy stock(s)!");
                alert.show();
            }
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Buy Stock");
            alert.setContentText("Please enter a value like integer or decimal number!");
            alert.show();
        }
    }


    @FXML
    private void onActionSell() {

        if (stockProtocol.get(this.stockName).size() > 0) {
            double boughtStockValue = 0;


            for (String line : this.stockProtocol.get(this.stockName)) {

                String[] splitLine = line.split(";");
                boughtStockValue += Double.parseDouble(splitLine[2]);
            }

            boughtStockValue = Double.parseDouble(decimalFormat.format(boughtStockValue));
            double account = Double.parseDouble(decimalFormat.format(Double.parseDouble(this.accountMoney.getText())));
            double winLoseMoney = Double.parseDouble(decimalFormat.format(Double.parseDouble(this.winLoseStock.getText())));


            this.accountMoney.setText(String.valueOf(account + boughtStockValue + winLoseMoney));


            this.accountMoney.setText(decimalFormat.format(Double.parseDouble(this.accountMoney.getText())));
            stockProtocol.get(this.stockName).clear();

            if (Double.parseDouble(this.accountMoney.getText()) < 0) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account close");
                alert.setContentText("You account is bankrupt. It will automatically close and sign out.");
                alert.show();

                signOut(true);
            } else {
                saveAccount(false);
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sell Stock");
            alert.setContentText("You have not stock(s) to sell!");
            alert.show();
        }
    }


    @FXML
    private void onActionSignOut() {
        signOut(false);
    }


    private void signOut(boolean close) {
        try {
            saveAccount(close);

            new SceneSwitch().sceneSwitch("Login.fxml", signOut);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sign out error");
            alert.setContentText("You account can not be sign out and also not save!");
            alert.show();
        }
    }


    /**
     * <h1>Account</h1>
     *
     * <p>There are methods where the account will be saved and loaded</p>
     */

    public void saveAccount(boolean close) {

        File file = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\StockFX\\" + this.name + ".txt");

        try {
            FileWriter writer = new FileWriter(file);

            writer.write(close + "\n");
            writer.write("Password:" + this.password + "\n");
            writer.write("Account:" + this.accountMoney.getText() + "\n");
            writer.flush();

            writeBoughtStock(writer, "AAPL");
            writeBoughtStock(writer, "AMD");
            writeBoughtStock(writer, "AMZN");
            writeBoughtStock(writer, "BBY");
            writeBoughtStock(writer, "DASH");
            writeBoughtStock(writer, "DIS");
            writeBoughtStock(writer, "DO");
            writeBoughtStock(writer, "ET");
            writeBoughtStock(writer, "FVRR");
            writeBoughtStock(writer, "GOOG");
            writeBoughtStock(writer, "INTC");
            writeBoughtStock(writer, "META");
            writeBoughtStock(writer, "MSFT");
            writeBoughtStock(writer, "NFLX");
            writeBoughtStock(writer, "ORCL");
            writeBoughtStock(writer, "PINS");
            writeBoughtStock(writer, "SNAP");
            writeBoughtStock(writer, "TSLA");
            writeBoughtStock(writer, "TWTR");
            writer.close();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Account save");
            alert.setContentText("You account can not be saved!");
            alert.show();
        }
    }


    private void writeBoughtStock(FileWriter writer, String stockName) throws IOException {

        writer.write(stockName + "\n");
        writer.flush();

        for (String line : this.stockProtocol.get(stockName)) {
            writer.write(line + "\n");
            writer.flush();
        }
    }


    public void loadAccount() {

        File file = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\StockFX\\" + this.name + ".txt");
        BufferedReader bufferedReader;
        String[] splitString;
        int index = 0;

        String[] stocks = new String[]{"AAPL", "AMD", "AMZN", "BBY",
                                        "DASH", "DIS", "DO", "ET",
                                        "FVRR", "GOOG", "INTC", "META",
                                        "MSFT", "NFLX", "ORCL", "PINS",
                                        "SNAP", "TSLA", "TWTR", ""};

        try {

            bufferedReader = new BufferedReader(new FileReader(file));
            String line;

            bufferedReader.readLine();
            bufferedReader.readLine();
            splitString = bufferedReader.readLine().split(":");
            accountMoney.setText(splitString[1]);

            while((line = bufferedReader.readLine()) != null){

                if (line.equals(stocks[index])){
                    index++;

                }else {
                    this.stockProtocol.get(stocks[index - 1]).add(line);
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Account load");
            alert.setContentText("You account can not be loaded!");
            alert.show();
        }
    }
}
