package application;

import java.io.IOException;
import dbpackage.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ControllerSceneFarmNavigation implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    private Connection connection = Dbconnect.getConnection();

    private int currentDay; 
    private double money; 
    private int actionPointsRemaining; 
    private int actionPointsTotal = 5; 
    private int deadline;
    private boolean isOwnedTractor;
    

    @FXML
    private Label labelCurrentDay;
    @FXML
    private Label labelActionPoints;
    @FXML
    private Label labelMoney;
    @FXML
    private Label labelDeadline;
    @FXML
    private Label playerName;
    
    
    public void getUser() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM temporarystatsholder");

        if (rs.next()) { 
        	playerName.setText(rs.getString("username"));
            currentDay = rs.getInt("cur_day");
            money = rs.getDouble("cur_money");
            actionPointsRemaining = rs.getInt("cur_actions");
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
    }


    public void endDay(MouseEvent e) {
        try {
        	resetWateredColumn("tempgrowingbronze");
            resetWateredColumn("tempgrowingsilver");
            resetWateredColumn("tempgrowinggold");
            
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        currentDay++;
        actionPointsRemaining = actionPointsTotal;
        deadline -= 1;

        try {
        	
        	addToReadyToHarvest("tempgrowingbronze", currentDay);
            addToReadyToHarvest("tempgrowingsilver", currentDay);
            addToReadyToHarvest("tempgrowinggold", currentDay);
            removeUnwateredCrops("tempgrowingbronze", currentDay);
            removeUnwateredCrops("tempgrowingsilver", currentDay);
            removeUnwateredCrops("tempgrowinggold", currentDay);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        this.deadline();

        String updateSql = "UPDATE temporarystatsholder SET cur_day = ?, cur_deadline = ?, cur_money = ?, cur_actions = ? WHERE user_id = 1";

        try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
            pstmt.setInt(1, currentDay);
            pstmt.setInt(2, deadline);
            pstmt.setDouble(3, money);
            pstmt.setInt(4, actionPointsTotal); // Set cur_actions to 5
            pstmt.executeUpdate();

        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        this.setTopTexts();
    }

    private void addToReadyToHarvest(String tableName, int currentDay) throws SQLException {
        String getReadyCrops = "SELECT * FROM " + tableName + " WHERE grown_day <= ?";
        String updateReadyToHarvest = "UPDATE tempreadytoharvest SET ";

        if (tableName.equals("tempgrowingbronze")) {
            updateReadyToHarvest += "crop_bronze = crop_bronze + ?";
        }
        if (tableName.equals("tempgrowingsilver")) {
            updateReadyToHarvest += "crop_silver = crop_silver + ?";
        } 
        if (tableName.equals("tempgrowinggold")) {
            updateReadyToHarvest += "crop_gold = crop_gold + ?";
        }

        try (PreparedStatement pstmtReadyCrops = connection.prepareStatement(getReadyCrops);
             PreparedStatement pstmtUpdateReadyToHarvest = connection.prepareStatement(updateReadyToHarvest)) {

            pstmtReadyCrops.setInt(1, currentDay);

            try (ResultSet rsReadyCrops = pstmtReadyCrops.executeQuery()) {
                int totalCount = 0;

                while (rsReadyCrops.next()) {
                    int seedsPlanted = rsReadyCrops.getInt("seed_planted_num");
                    totalCount += seedsPlanted;
                }

                pstmtUpdateReadyToHarvest.setInt(1, totalCount);
                pstmtUpdateReadyToHarvest.executeUpdate();
            }
        }
    }

    public void setTopTexts() {
        labelCurrentDay.setText("Day: " + currentDay);
        labelActionPoints.setText("Actions: " + actionPointsRemaining);
        labelMoney.setText("Money: " + money);
        labelDeadline.setText("Deadline: " + deadline);
        
    }

    public void deadline() {
        if (deadline == 0) {
            money -= 50;
            deadline += 14;
        }
    }
    
    private void resetWateredColumn(String tableName) throws SQLException {
        String query = "UPDATE " + tableName + " SET watered = 0";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.executeUpdate();
        }
    }
    
    private void removeUnwateredCrops(String tableName, int currentDay) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE watered = 0 AND ? - day_watered > 1";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, currentDay); 
            pstmt.executeUpdate();
        }
    }

    public void switchToScenePauseMenu(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ScenePauseMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSceneFarmFields(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SceneFarmFields.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }

    public void switchToSceneFarmAssets(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SceneFarmAssets.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        
    }

    public void switchToSceneGuild(MouseEvent event) throws IOException {
    	
    	if (!isOwnedTractor) {
    		actionPointsRemaining--;
    		
    	}
    	
        root = FXMLLoader.load(getClass().getResource("SceneGuild.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        
    }
}
