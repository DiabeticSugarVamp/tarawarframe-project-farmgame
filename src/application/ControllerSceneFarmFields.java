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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerSceneFarmFields implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private Connection connection = Dbconnect.getConnection();

    private int currentDay; 
    private double money; 
    private int actionPointsRemaining; 
    private int actionPointsTotal; 
    private int deadline;

    private int seedBronze;
    private int seedSilver;
    private int seedGold;
    
    private int bronzeDuration = 2;
    private int silverDuration = 4;
    private int goldDuration = 7;
    
    private int totalBronzeGrowing;
    private int totalSilverGrowing;
    private int totalGoldGrowing;
    
    private int bronzeGrownDay;
    private int silverGrownDay;
    private int goldGrownDay;
    
    private boolean isOwnedGloves;
    private boolean isOwnedTractor;
    private boolean isOwnedCan;

    @FXML
    private Label labelCurrentDay;
    @FXML
    private Label labelActionPoints;
    @FXML
    private Label labelMoney;
    @FXML
    private Label labelDeadline;
    
    @FXML
    private Label lblSeedsBronze;
    @FXML
    private Label lblSeedsSilver;
    @FXML
    private Label lblSeedsGold;
    
    @FXML
    private Label lblGrowingBronze;
    @FXML
    private Label lblGrowingSilver;
    @FXML
    private Label lblGrowingGold;
    
    @FXML
    private Label btnGoToInventories;

    public void getUser() throws SQLException {
        String userQuery = "SELECT * FROM temporarystatsholder WHERE user_id = 1";
        String itemsQuery = "SELECT * FROM tempitems WHERE temp_id = 1";
        String growingBronze = "SELECT * FROM tempgrowingbronze WHERE day_planted >= ?";
        String growingSilver = "SELECT * FROM tempgrowingsilver WHERE day_planted >= ?";
        String growingGold = "SELECT * FROM tempgrowinggold WHERE day_planted >= ?";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(userQuery);
             Statement stmtItems = connection.createStatement();
             ResultSet rsItems = stmtItems.executeQuery(itemsQuery);
             PreparedStatement pstmtGrowingBronze = connection.prepareStatement(growingBronze);
             PreparedStatement pstmtGrowingSilver = connection.prepareStatement(growingSilver);
             PreparedStatement pstmtGrowingGold = connection.prepareStatement(growingGold)) {

            if (rs.next()) { 
                currentDay = rs.getInt("cur_day");
                money = rs.getDouble("cur_money");
                actionPointsRemaining = rs.getInt("cur_actions");
                actionPointsTotal = 5; 
                deadline = rs.getInt("cur_deadline");
            }
            
            if (rsItems.next()) {
                seedBronze = rsItems.getInt("seed_bronze");
                seedSilver = rsItems.getInt("seed_silver");
                seedGold = rsItems.getInt("seed_gold");
            }
            
            pstmtGrowingBronze.setInt(1, currentDay - bronzeDuration); 
            pstmtGrowingSilver.setInt(1, currentDay - silverDuration); 
            pstmtGrowingGold.setInt(1, currentDay - goldDuration); 
            
            try (ResultSet rsGrowingBronze = pstmtGrowingBronze.executeQuery()) {
                while (rsGrowingBronze.next()) {
                    int seedsPlanted = rsGrowingBronze.getInt("seed_planted_num"); 
                    totalBronzeGrowing += seedsPlanted;
                }
            }
            
            try (ResultSet rsGrowingSilver = pstmtGrowingSilver.executeQuery()) {
                while (rsGrowingSilver.next()) {
                    int seedsPlanted = rsGrowingSilver.getInt("seed_planted_num"); 
                    totalSilverGrowing += seedsPlanted;
                }
            }
            
            try (ResultSet rsGrowingGold = pstmtGrowingGold.executeQuery()) {
                while (rsGrowingGold.next()) {
                    int seedsPlanted = rsGrowingGold.getInt("seed_planted_num"); 
                    totalGoldGrowing += seedsPlanted;
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            getUser();
            removeUnwateredCrops("tempgrowingbronze");
            removeUnwateredCrops("tempgrowingsilver");
            removeUnwateredCrops("tempgrowinggold");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setTopTexts();
    }

    public void setTopTexts() {
        labelCurrentDay.setText("Day: " + currentDay);
        labelActionPoints.setText("Actions: " + actionPointsRemaining);
        labelMoney.setText("Money: " + money);
        labelDeadline.setText("Deadline: " + deadline);
        
        lblSeedsBronze.setText("Bronze seeds: " + seedBronze);
        lblSeedsSilver.setText("Silver seeds: " + seedSilver);
        lblSeedsGold.setText("Gold seeds: " + seedGold);

        lblGrowingBronze.setText("Bronze crops growing: " + totalBronzeGrowing);
        lblGrowingSilver.setText("Silver crops growing: " + totalSilverGrowing);
        lblGrowingGold.setText("Gold crops growing: " + totalGoldGrowing);
        
    }
    
    public void plantSeeds(MouseEvent e) throws IOException, SQLException {
        if (!isOwnedGloves) {
            String getItems = "SELECT * FROM tempitems WHERE temp_id = 1";
            String getStats = "SELECT * FROM temporarystatsholder WHERE user_id = 1";

            try (Statement stmtItems = connection.createStatement();
                 ResultSet rsItems = stmtItems.executeQuery(getItems);
                 Statement stmtStats = connection.createStatement();
                 ResultSet rsStats = stmtStats.executeQuery(getStats)) {

                if (rsItems.next() && rsStats.next()) {
                    int bronzeSeeds = rsItems.getInt("seed_bronze");
                    int silverSeeds = rsItems.getInt("seed_silver");
                    int goldSeeds = rsItems.getInt("seed_gold");

                    int currentActions = rsStats.getInt("cur_actions");
                    currentDay = rsStats.getInt("cur_day");
                    deadline = rsStats.getInt("cur_deadline");

                    boolean canPlant = false;

                    if (bronzeSeeds > 0) {
                        bronzeSeeds -= 1;
                        canPlant = true;

                        String updateSeedBronze = "INSERT INTO tempgrowingbronze (grown_day, seed_planted_num, day_planted, day_watered, watered) " +
                                "VALUES (?, 1, ?, ?, 1) " + 
                                "ON DUPLICATE KEY UPDATE seed_planted_num = seed_planted_num + 1";

                        bronzeGrownDay = currentDay + bronzeDuration;

                        try (PreparedStatement pstBronzeGrowing = connection.prepareStatement(updateSeedBronze)) {
                            pstBronzeGrowing.setInt(1, bronzeGrownDay);
                            pstBronzeGrowing.setInt(2, currentDay);
                            pstBronzeGrowing.setInt(3, currentDay);
                            pstBronzeGrowing.executeUpdate();
                            
                            
                        }

                        totalBronzeGrowing++; 
                    } 

                    if (silverSeeds > 0) {
                        silverSeeds -= 1;
                        canPlant = true;
                        

                        String updateSeedSilver = "INSERT INTO tempgrowingsilver (grown_day, seed_planted_num, day_planted, day_watered, watered) " +
                                "VALUES (?, 1, ?, ?, 1) " + 
                                "ON DUPLICATE KEY UPDATE seed_planted_num = seed_planted_num + 1";

                        silverGrownDay = currentDay + silverDuration;

                        try (PreparedStatement pstSilverGrowing = connection.prepareStatement(updateSeedSilver)) {
                            pstSilverGrowing.setInt(1, silverGrownDay);
                            pstSilverGrowing.setInt(2, currentDay);
                            pstSilverGrowing.setInt(3, currentDay);
                            pstSilverGrowing.executeUpdate();
                            
                            
                        }

                        totalSilverGrowing++; 
                    }

                    if (goldSeeds > 0) {
                        goldSeeds -= 1;
                        canPlant = true;

                        String updateSeedGold = "INSERT INTO tempgrowinggold (grown_day, seed_planted_num, day_planted, day_watered, watered) " +
                                "VALUES (?, 1, ?, ?, 1) " + 
                                "ON DUPLICATE KEY UPDATE seed_planted_num = seed_planted_num + 1";

                        goldGrownDay = currentDay + goldDuration;

                        try (PreparedStatement pstGoldGrowing = connection.prepareStatement(updateSeedGold)) {
                            pstGoldGrowing.setInt(1, goldGrownDay);
                            pstGoldGrowing.setInt(2, currentDay);
                            pstGoldGrowing.setInt(3, currentDay);
                            pstGoldGrowing.executeUpdate();
                            
                            
                        }

                        totalGoldGrowing++; 
                    }

                    if (canPlant) {
                        if (currentActions > 0) {
                            currentActions--;
                            
                            
                        }

                        if (currentActions == 0) {
                            currentDay++;
                            deadline--;

                            resetWateredColumn("tempgrowingbronze");
                            resetWateredColumn("tempgrowingsilver");
                            resetWateredColumn("tempgrowinggold");

                            currentActions = actionPointsTotal;
                            
                            
                        }

                        String updateItems = "UPDATE tempitems SET seed_bronze = ?, seed_silver = ?, seed_gold = ? WHERE temp_id = 1";
                        String updateStats = "UPDATE temporarystatsholder SET cur_actions = ?, cur_day = ?, cur_deadline = ? WHERE user_id = 1";

                        try {
                            try (PreparedStatement pstmtStats = connection.prepareStatement(updateStats)) {
                                pstmtStats.setInt(1, currentActions);
                                pstmtStats.setInt(2, currentDay);
                                pstmtStats.setInt(3, deadline);
                                pstmtStats.executeUpdate();
                                
                                
                            }

                            try (PreparedStatement pstmtItems = connection.prepareStatement(updateItems)){
                                pstmtItems.setInt(1, bronzeSeeds);
                                pstmtItems.setInt(2, silverSeeds);
                                pstmtItems.setInt(3, goldSeeds);
                                pstmtItems.executeUpdate();
                                
                                
                                
                            }

                            lblSeedsBronze.setText("Bronze seeds: " + bronzeSeeds);
                            lblSeedsSilver.setText("Silver seeds: " + silverSeeds);
                            lblSeedsGold.setText("Gold seeds: " + goldSeeds);
                            labelActionPoints.setText("Actions: " + currentActions);
                            labelCurrentDay.setText("Day: " + currentDay);
                            labelDeadline.setText("Deadline: " + deadline);

                           
                            lblGrowingBronze.setText("Bronze crops growing: " + totalBronzeGrowing);
                            lblGrowingSilver.setText("Silver crops growing: " + totalSilverGrowing);
                            lblGrowingGold.setText("Gold crops growing: " + totalGoldGrowing);
                            
                            

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            
                            
                        }
                        
                        
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("No Seeds");
                        alert.setHeaderText("Insufficient Seeds");
                        alert.setContentText("You do not have enough seeds to plant.");
                        alert.showAndWait();
                        
                        
                    }
                    
                    
                }
                
                
            }
            
        }
        
        
    }
    
    public void harvestCrops(MouseEvent e) throws IOException {
        if (!isOwnedTractor) {
            
        }
    }
    
    public void waterCrops(MouseEvent e) throws IOException {
        if (!isOwnedCan) {
            try {
                // Decrement action points
                actionPointsRemaining--;

                resetWateredColumn("tempgrowingbronze");
                resetWateredColumn("tempgrowingsilver");
                resetWateredColumn("tempgrowinggold");

                waterCurrentDayCrops("tempgrowingbronze");
                waterCurrentDayCrops("tempgrowingsilver");
                waterCurrentDayCrops("tempgrowinggold");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Crops Watered");
                alert.setHeaderText(null);
                alert.setContentText("All crops have been watered for today.");
                alert.showAndWait();

                // Update the action points label
                labelActionPoints.setText("Actions: " + actionPointsRemaining);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void removeUnwateredCrops(String tableName) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE ? - day_watered > 2";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, currentDay);
            pstmt.executeUpdate();
            
            
        }
    }

    private void resetWateredColumn(String tableName) throws SQLException {
        String query = "UPDATE " + tableName + " SET watered = 0";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.executeUpdate();
            
            
        }
    }

    private void waterCurrentDayCrops(String tableName) throws SQLException {
        String query = "UPDATE " + tableName + " SET watered = 1, day_watered = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, currentDay);
            pstmt.executeUpdate();
                    
            
        }
    }

    public void switchToSceneFarmNavigation(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SceneFarmNavigation.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        
    }
    
    public void switchToSceneFarmInventories(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SceneFarmInventories.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        
    }
}
