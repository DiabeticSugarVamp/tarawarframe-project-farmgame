package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import dbpackage.Dbconnect;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerSceneGuildSellPlants implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private Connection connection = Dbconnect.getConnection();

    private int currentDay;
    private double money;
    private int actionPointsRemaining;
    private int actionPointsTotal;
    private int deadline;

    @FXML
    private Label labelCurrentDay;
    @FXML
    private Label labelActionPoints;
    @FXML
    private Label labelMoney;
    @FXML
    private Label labelDeadline;

    @FXML
    private Spinner<Integer> sellBronzeSpinner;
    @FXML
    private Spinner<Integer> sellSilverSpinner;
    @FXML
    private Spinner<Integer> sellGoldSpinner;

    @FXML
    private Label btnSellItems;

    int calculatedTotalValue;
    @FXML
    private TextField sellTotal;

    public void getUser() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM temporarystatsholder WHERE user_id = 1");

        if (rs.next()) {
            currentDay = rs.getInt("cur_day");
            money = rs.getDouble("cur_money");
            actionPointsRemaining = rs.getInt("cur_actions");
            actionPointsTotal = rs.getInt("cur_actions");
            deadline = rs.getInt("cur_deadline");
            
            
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            getUser();
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        setTopTexts();

        SpinnerValueFactory<Integer> bronzeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        bronzeValueFactory.setValue(0);
        sellBronzeSpinner.setValueFactory(bronzeValueFactory);

        SpinnerValueFactory<Integer> silverValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        silverValueFactory.setValue(0);
        sellSilverSpinner.setValueFactory(silverValueFactory);

        SpinnerValueFactory<Integer> goldValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        goldValueFactory.setValue(0);
        sellGoldSpinner.setValueFactory(goldValueFactory);

        calculatedTotalValue = (sellBronzeSpinner.getValue() * 8) +
                (sellSilverSpinner.getValue() * 14) +
                (sellGoldSpinner.getValue() * 20);
        sellTotal.setText(Integer.toString(calculatedTotalValue));

        sellBronzeSpinner.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {

                calculatedTotalValue = (sellBronzeSpinner.getValue() * 8) +
                        (sellSilverSpinner.getValue() * 14) +
                        (sellGoldSpinner.getValue() * 20);
                sellTotal.setText(Integer.toString(calculatedTotalValue));
                
                
            }

        });

        sellSilverSpinner.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {

                calculatedTotalValue = (sellBronzeSpinner.getValue() * 8) +
                        (sellSilverSpinner.getValue() * 14) +
                        (sellGoldSpinner.getValue() * 20);
                sellTotal.setText(Integer.toString(calculatedTotalValue));
                
                
            }

        });

        sellGoldSpinner.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {

                calculatedTotalValue = (sellBronzeSpinner.getValue() * 8) +
                        (sellSilverSpinner.getValue() * 14) +
                        (sellGoldSpinner.getValue() * 20);
                sellTotal.setText(Integer.toString(calculatedTotalValue));
                
            }

        });
    }

    public void sellTotalItems() {
        int bronzeSold = sellBronzeSpinner.getValue();
        int silverSold = sellSilverSpinner.getValue();
        int goldSold = sellGoldSpinner.getValue();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT crop_bronze, crop_silver, crop_gold FROM tempitems WHERE temp_id = 1");

            if (rs.next()) {
                int availableBronze = rs.getInt("crop_bronze");
                int availableSilver = rs.getInt("crop_silver");
                int availableGold = rs.getInt("crop_gold");

                // Check if the character has enough crops to sell
                if (bronzeSold > availableBronze || silverSold > availableSilver || goldSold > availableGold) {
                    // Show an alert indicating insufficient crops
                    System.out.println("You do not have enough crops to sell.");
                    return;
                    
                }

                // Update tempitems table
                String updateTempItemsQuery = "UPDATE tempitems SET crop_bronze = ?, crop_silver = ?, crop_gold = ? WHERE temp_id = 1";
                PreparedStatement pstmt = connection.prepareStatement(updateTempItemsQuery);
                pstmt.setInt(1, availableBronze - bronzeSold);
                pstmt.setInt(2, availableSilver - silverSold);
                pstmt.setInt(3, availableGold - goldSold);
                pstmt.executeUpdate();

                // Update money in temporarystatsholder
                double totalSale = calculatedTotalValue;
                money += totalSale;
                String updateMoneyQuery = "UPDATE temporarystatsholder SET cur_money = ? WHERE user_id = 1";
                PreparedStatement pstmtMoney = connection.prepareStatement(updateMoneyQuery);
                pstmtMoney.setDouble(1, money);
                pstmtMoney.executeUpdate();

                // Update UI
                setTopTexts();

                // Reset spinners
                sellBronzeSpinner.getValueFactory().setValue(0);
                sellSilverSpinner.getValueFactory().setValue(0);
                sellGoldSpinner.getValueFactory().setValue(0);

            } else {
                // Show an alert indicating no crops available
                System.out.println("You do not have any crops to sell.");
                
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            
        }
    }


    public void setTopTexts() {
        labelCurrentDay.setText(" " + currentDay);
        labelActionPoints.setText(" " + actionPointsRemaining);
        labelMoney.setText(" " + money);
        labelDeadline.setText(" " + deadline);
        
    }

    public void switchToSceneGuild(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SceneGuild.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        
    }
}
