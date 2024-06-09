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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerSceneGuildUpgrades implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    private Connection connection = Dbconnect.getConnection();
    
    private int money;
    private boolean isOwnedCan;
    private boolean isOwnedTruck;
    private boolean isOwnedTractor;
    private boolean isOwnedGloves;
    
    @FXML
    private Label totalMoney;
    
    public void switchToSceneGuild(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/application/SceneGuild.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String queryMoney = "SELECT * FROM temporarystatsholder WHERE user_id = 1";
        try {
            Statement stmtMoney = connection.createStatement();
            ResultSet rsMoney = stmtMoney.executeQuery(queryMoney);
            
            if(rsMoney.next()) {
                money = rsMoney.getInt("cur_money");
            }
            totalMoney.setText(" " + money);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void buyCan(MouseEvent event) throws IOException {
        if (!isOwnedCan && money >= 200) {
            money -= 200;
            updateMoneyInDatabase(money);
            isOwnedCan = true;
        } else {
            showAlert("Not enough money to buy the watering can.");
        }
    }
    
    public void buyTruck(MouseEvent event) throws IOException {
        if (!isOwnedTruck && money >= 200) {
            money -= 200;
            updateMoneyInDatabase(money);
            isOwnedTruck = true;
        } else {
            showAlert("Not enough money to buy the truck.");
        }
    }

    public void buyTractor(MouseEvent event) throws IOException {
        if (!isOwnedTractor && money >= 200) {
            money -= 200;
            updateMoneyInDatabase(money);
            isOwnedTractor = true;
        } else {
            showAlert("Not enough money to buy the tractor.");
        }
    }

    public void buyGloves(MouseEvent event) throws IOException {
        if (!isOwnedGloves && money >= 200) {
            money -= 200;
            updateMoneyInDatabase(money);
            isOwnedGloves = true;
        } else {
            showAlert("Not enough money to buy the gloves.");
        }
    }
    
    public void buyMansion(MouseEvent event) throws IOException {
        if (money >= 300) {
            money -= 300;
            updateMoneyInDatabase(money);
            root = FXMLLoader.load(getClass().getResource("SceneEndWin.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            showAlert("Not enough money to buy the mansion.");
        }
    }
    
    private void updateMoneyInDatabase(int newMoney) {
        String updateMoneyQuery = "UPDATE temporarystatsholder SET cur_money = ? WHERE user_id = 1";
        try {
            PreparedStatement pstmt = connection.prepareStatement(updateMoneyQuery);
            pstmt.setInt(1, newMoney);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Insufficient Funds");
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/assets/icon.jpg")));
        icon.setFitHeight(48);
        icon.setFitWidth(48); 
        alert.setGraphic(icon); 

        //ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Ok", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(cancelButtonType);

        
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(cancelButtonType);
        cancelButton.getStyleClass().add("button-cancel");
        
        alert.showAndWait();
    }
}
