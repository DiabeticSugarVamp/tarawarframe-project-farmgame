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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    
    private int harvestBronze;
    private int harvestSilver;
    private int harvestGold;
    
    private boolean isOwnedGloves;
    private boolean isOwnedTractor;
    private boolean isOwnedCan;
    
    //For the actionBars
    private String[] actionBarImages = {
    		"/assets/stamina bars/stamina (blue)/stamina0 (blue).png",
    	    "/assets/stamina bars/stamina (blue)/stamina1 (blue).png",
    	    "/assets/stamina bars/stamina (blue)/stamina2 (blue).png",
    	    "/assets/stamina bars/stamina (blue)/stamina3 (blue).png",
    	    "/assets/stamina bars/stamina (blue)/stamina4 (blue).png",
    	    "/assets/stamina bars/stamina (blue)/stamina5 (blue).png"
    	};

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
    private Label lblHarvestBronze;
    @FXML
    private Label lblHarvestSilver;
    @FXML
    private Label lblHarvestGold;
    
    @FXML
    private Label btnGoToInventories;
    
    @FXML
    private ImageView actionBars;

    public void getUser() throws SQLException {
        String userQuery = "SELECT * FROM temporarystatsholder WHERE user_id = 1";
        String itemsQuery = "SELECT * FROM tempitems WHERE temp_id = 1";
        String growingBronze = "SELECT * FROM tempgrowingbronze WHERE day_planted >= ?";
        String growingSilver = "SELECT * FROM tempgrowingsilver WHERE day_planted >= ?";
        String growingGold = "SELECT * FROM tempgrowinggold WHERE day_planted >= ?";
        String queryHarvest = "SELECT * FROM tempreadytoharvest WHERE temp_id = 1";

        totalBronzeGrowing = 0;
        totalSilverGrowing = 0;
        totalGoldGrowing = 0;

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

            try (Statement stmtHarvest = connection.createStatement();
                 ResultSet rsHarvest = stmtHarvest.executeQuery(queryHarvest)) {
                if (rsHarvest.next()) {
                    harvestBronze = rsHarvest.getInt("crop_bronze");
                    harvestSilver = rsHarvest.getInt("crop_silver");
                    harvestGold = rsHarvest.getInt("crop_gold");
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
            resetWateredColumn("tempgrowingbronze");
            resetWateredColumn("tempgrowingsilver");
            resetWateredColumn("tempgrowinggold");
            readyToHarvest();

           
            setTopTexts();
            
            //For the actionBars , turn to normal later 
            //updateActionBarsImage(actionPointsRemaining);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deadline() throws IOException {
	    	if(deadline == 7) {
	    		switchRentNotifDay7();
	    		
	    	}else if(deadline == 2) {
	    		switchRentNotifDay2();
	    		
	    	}else if (deadline == 0){
	            if (money > 0) {
	            	switchRentNotif();
	            	money -= 50;
		            deadline += 14;
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
	            }   
            
	    	}
	    	
    }
    //For the action bars
    public void updateActionBarsImage(int actionPointsRemaining) {
        if (actionPointsRemaining >= 0 && actionPointsRemaining < actionBarImages.length) {
            String imageUrl = actionBarImages[actionPointsRemaining];
            Image image = new Image(getClass().getResource(imageUrl).toExternalForm());
            actionBars.setImage(image);
        }
    }
    
    private void switchRentNotif() throws IOException {
        root = FXMLLoader.load(getClass().getResource("SceneRentNotif.fxml"));
        stage = (Stage) btnGoToInventories.getScene().getWindow(); 
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        	
        	
    }
        
    private void switchRentNotifDay2() throws IOException {
    	root = FXMLLoader.load(getClass().getResource("SceneRentNotifDay2.fxml"));
        stage = (Stage) btnGoToInventories.getScene().getWindow(); 
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        	
        	
     }
        
        private void switchRentNotifDay7() throws IOException {
        	root = FXMLLoader.load(getClass().getResource("SceneRentNotifDay7.fxml"));
            stage = (Stage) btnGoToInventories.getScene().getWindow(); 
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        	
        	
        }
    public void setTopTexts() {
        labelCurrentDay.setText(" " + currentDay);
        labelActionPoints.setText(" " + actionPointsRemaining);
        labelMoney.setText(" " + money);
        labelDeadline.setText(" " + deadline);
        
        lblSeedsBronze.setText("Bronze seeds: " + seedBronze);
        lblSeedsSilver.setText("Silver seeds: " + seedSilver);
        lblSeedsGold.setText("Gold seeds: " + seedGold);

        lblGrowingBronze.setText("Bronze crops growing: " + totalBronzeGrowing);
        lblGrowingSilver.setText("Silver crops growing: " + totalSilverGrowing);
        lblGrowingGold.setText("Gold crops growing: " + totalGoldGrowing);
        
        lblHarvestBronze.setText("Bronze crops ready to harvest: " + harvestBronze);
        lblHarvestSilver.setText("Silver crops ready to harvest: " + harvestSilver);
        lblHarvestGold.setText("Gold crops ready to harvest: " + harvestGold);
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
                	seedBronze = rsItems.getInt("seed_bronze");
                	seedSilver = rsItems.getInt("seed_silver");
                	seedGold = rsItems.getInt("seed_gold");

                    actionPointsRemaining = rsStats.getInt("cur_actions");
                    currentDay = rsStats.getInt("cur_day");
                    deadline = rsStats.getInt("cur_deadline");

                    boolean canPlant = false;

                    if (seedBronze > 0) {
                    	seedBronze -= 1;
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

                    if (seedSilver > 0) {
                    	seedSilver -= 1;
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

                    if (seedGold > 0) {
                    	seedGold -= 1;
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
                        if (actionPointsRemaining > 0) {
                        	actionPointsRemaining--;
                            
                            
                        }

                        if (actionPointsRemaining == 0) {
                            currentDay++;
                            deadline--;
                            deadline();

                            removeUnwateredCrops("tempgrowingbronze");
                            removeUnwateredCrops("tempgrowingsilver");
                            removeUnwateredCrops("tempgrowinggold");
                            resetWateredColumn("tempgrowingbronze");
                            resetWateredColumn("tempgrowingsilver");
                            resetWateredColumn("tempgrowinggold");

                            actionPointsRemaining = actionPointsTotal;
                            
                            
                        }

                        String updateItems = "UPDATE tempitems SET seed_bronze = ?, seed_silver = ?, seed_gold = ? WHERE temp_id = 1";
                        String updateStats = "UPDATE temporarystatsholder SET cur_actions = ?, cur_day = ?, cur_deadline = ? WHERE user_id = 1";

                        try {
                            try (PreparedStatement pstmtStats = connection.prepareStatement(updateStats)) {
                                pstmtStats.setInt(1, actionPointsRemaining);
                                pstmtStats.setInt(2, currentDay);
                                pstmtStats.setInt(3, deadline);
                                pstmtStats.executeUpdate();
                                
                                
                            }

                            try (PreparedStatement pstmtItems = connection.prepareStatement(updateItems)){
                                pstmtItems.setInt(1, seedBronze);
                                pstmtItems.setInt(2, seedSilver);
                                pstmtItems.setInt(3, seedGold);
                                pstmtItems.executeUpdate();
                                
                                
                                
                            }



                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            
                            
                        }
                        initialize(null, null);
                        
                        
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("No Seeds");
                        alert.setHeaderText("Insufficient Seeds");
                        alert.setContentText("You do not have enough seeds to plant.");
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
                
                
            }
            
            
        }
        
        
    }
    
    public void harvestCrops(MouseEvent e) throws IOException, SQLException {
        String getHarvestedCropsQuery = "SELECT * FROM tempreadytoharvest WHERE temp_id = 1";
        String getStats = "SELECT * FROM temporarystatsholder WHERE user_id = 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(getHarvestedCropsQuery);
             Statement stmtStats = connection.createStatement();
             ResultSet rsStats = stmtStats.executeQuery(getStats)) {

            if (rs.next() && rsStats.next()) {
               
                int harvestedBronze = rs.getInt("crop_bronze");
                int harvestedSilver = rs.getInt("crop_silver");
                int harvestedGold = rs.getInt("crop_gold");
                
                if(harvestedBronze == 0 && harvestedSilver == 0 && harvestedGold == 0) {
                	Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Crops to Harvest");
                    alert.setHeaderText(null);
                    alert.setContentText("There are no crops ready to harvest.");
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
                    
                	return;
                	
                }

                
                int currentActions = rsStats.getInt("cur_actions");
                currentDay = rsStats.getInt("cur_day");
                deadline = rsStats.getInt("cur_deadline");

                
                if (currentActions > 0) {
                    currentActions--;
                }

                if (currentActions == 0) {
                    currentDay++;
                    deadline--;
                    deadline();
                    
                    currentActions = actionPointsTotal;

                    
                    removeUnwateredCrops("tempgrowingbronze");
                    removeUnwateredCrops("tempgrowingsilver");
                    removeUnwateredCrops("tempgrowinggold");
                    resetWateredColumn("tempgrowingbronze");
                    resetWateredColumn("tempgrowingsilver");
                    resetWateredColumn("tempgrowinggold");
                    
                }

                
                String updateStats = "UPDATE temporarystatsholder SET cur_actions = ?, cur_day = ?, cur_deadline = ? WHERE user_id = 1";
                try (PreparedStatement pstmtStats = connection.prepareStatement(updateStats)) {
                    pstmtStats.setInt(1, currentActions);
                    pstmtStats.setInt(2, currentDay);
                    pstmtStats.setInt(3, deadline);
                    pstmtStats.executeUpdate();
                }

                
                String updateItemsQuery = "UPDATE tempitems SET crop_bronze = crop_bronze + ?, crop_silver = crop_silver + ?, crop_gold = crop_gold + ? WHERE temp_id = 1";
                try (PreparedStatement pstmt = connection.prepareStatement(updateItemsQuery)) {
                    pstmt.setInt(1, harvestedBronze);
                    pstmt.setInt(2, harvestedSilver);
                    pstmt.setInt(3, harvestedGold);
                    pstmt.executeUpdate();
                }

                
                String resetHarvestedCropsQuery = "UPDATE tempreadytoharvest SET crop_bronze = 0, crop_silver = 0, crop_gold = 0 WHERE temp_id = 1";
                try (PreparedStatement resetPstmt = connection.prepareStatement(resetHarvestedCropsQuery)) {
                    resetPstmt.executeUpdate();
                }

                
                harvestBronze = 0;
                harvestSilver = 0;
                harvestGold = 0;
                initialize(null, null);

            } else {
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Crops to Harvest");
                alert.setHeaderText(null);
                alert.setContentText("There are no crops ready to harvest.");
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
        } catch (SQLException ex) {
            ex.printStackTrace();
            
            throw new IOException("Failed to harvest crops", ex);
        }
    }

    
    public void readyToHarvest() throws SQLException {
        String updateReadyToHarvest = "UPDATE tempreadytoharvest SET crop_bronze = crop_bronze + ?, crop_silver = crop_silver + ?, crop_gold = crop_gold + ? WHERE temp_id = 1";
        String getReadyBronze = "SELECT COALESCE(SUM(seed_planted_num), 0) AS total FROM tempgrowingbronze WHERE grown_day <= ?";
        String getReadySilver = "SELECT COALESCE(SUM(seed_planted_num), 0) AS total FROM tempgrowingsilver WHERE grown_day <= ?";
        String getReadyGold = "SELECT COALESCE(SUM(seed_planted_num), 0) AS total FROM tempgrowinggold WHERE grown_day <= ?";
        String deleteReadyBronze = "DELETE FROM tempgrowingbronze WHERE grown_day <= ?";
        String deleteReadySilver = "DELETE FROM tempgrowingsilver WHERE grown_day <= ?";
        String deleteReadyGold = "DELETE FROM tempgrowinggold WHERE grown_day <= ?";

        int readyBronze = 0;
        int readySilver = 0;
        int readyGold = 0;

        try (PreparedStatement pstmtReadyBronze = connection.prepareStatement(getReadyBronze);
             PreparedStatement pstmtReadySilver = connection.prepareStatement(getReadySilver);
             PreparedStatement pstmtReadyGold = connection.prepareStatement(getReadyGold);
             PreparedStatement pstmtDeleteBronze = connection.prepareStatement(deleteReadyBronze);
             PreparedStatement pstmtDeleteSilver = connection.prepareStatement(deleteReadySilver);
             PreparedStatement pstmtDeleteGold = connection.prepareStatement(deleteReadyGold);
             PreparedStatement pstmtUpdateHarvest = connection.prepareStatement(updateReadyToHarvest)) {

            pstmtReadyBronze.setInt(1, currentDay);
            pstmtReadySilver.setInt(1, currentDay);
            pstmtReadyGold.setInt(1, currentDay);

            try (ResultSet rsReadyBronze = pstmtReadyBronze.executeQuery()) {
                if (rsReadyBronze.next()) {
                    readyBronze = rsReadyBronze.getInt("total");
                }
            }

            try (ResultSet rsReadySilver = pstmtReadySilver.executeQuery()) {
                if (rsReadySilver.next()) {
                    readySilver = rsReadySilver.getInt("total");
                }
            }

            try (ResultSet rsReadyGold = pstmtReadyGold.executeQuery()) {
                if (rsReadyGold.next()) {
                    readyGold = rsReadyGold.getInt("total");
                }
            }

            
            pstmtUpdateHarvest.setInt(1, readyBronze);
            pstmtUpdateHarvest.setInt(2, readySilver);
            pstmtUpdateHarvest.setInt(3, readyGold);
            pstmtUpdateHarvest.executeUpdate();

          
            pstmtDeleteBronze.setInt(1, currentDay);
            pstmtDeleteBronze.executeUpdate();

            pstmtDeleteSilver.setInt(1, currentDay);
            pstmtDeleteSilver.executeUpdate();

            pstmtDeleteGold.setInt(1, currentDay);
            pstmtDeleteGold.executeUpdate();

            
            totalBronzeGrowing -= readyBronze;
            totalSilverGrowing -= readySilver;
            totalGoldGrowing -= readyGold;

    
            harvestBronze += readyBronze;
            harvestSilver += readySilver;
            harvestGold += readyGold;
            
            lblGrowingBronze.setText("Bronze crops growing: " + totalBronzeGrowing);
            lblGrowingSilver.setText("Silver crops growing: " + totalSilverGrowing);
            lblGrowingGold.setText("Gold crops growing: " + totalGoldGrowing);
            
            lblHarvestBronze.setText("Bronze crops ready to harvest: " + harvestBronze);
            lblHarvestSilver.setText("Silver crops ready to harvest: " + harvestSilver);
            lblHarvestGold.setText("Gold crops ready to harvest: " + harvestGold);

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }



    
    public void waterCrops(MouseEvent e) throws IOException {
        if (!isOwnedCan) {
            try {
               
            	
            	if (actionPointsRemaining > 0) {
            		actionPointsRemaining--;
                    
                    
                }

                if (actionPointsRemaining == 0) {
                    currentDay++;
                    deadline--;
                    actionPointsRemaining = actionPointsTotal;
                    
                    
                }

                resetWateredColumn("tempgrowingbronze");
                resetWateredColumn("tempgrowingsilver");
                resetWateredColumn("tempgrowinggold");

                waterCurrentDayCrops("tempgrowingbronze");
                waterCurrentDayCrops("tempgrowingsilver");
                waterCurrentDayCrops("tempgrowinggold");
                
                String updateStats = "UPDATE temporarystatsholder SET cur_actions = ?, cur_day = ?, cur_deadline = ? WHERE user_id = 1";

              
                    try (PreparedStatement pstmtStats = connection.prepareStatement(updateStats)) {
                        pstmtStats.setInt(1, actionPointsRemaining);
                        pstmtStats.setInt(2, currentDay);
                        pstmtStats.setInt(3, deadline);
                        pstmtStats.executeUpdate();
                        
                        
                    }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Crops Watered");
                alert.setHeaderText(null);
                alert.setContentText("All crops have been watered for today.");
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

                
                initialize(null, null);
            } catch (SQLException ex) {
                ex.printStackTrace();
                
            }
        }
    }

    private void removeUnwateredCrops(String tableName) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE day_watered < ? AND watered = 0";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, currentDay - 1);
            pstmt.executeUpdate();
        }
        
       
    }

    private void resetWateredColumn(String tableName) throws SQLException {
        String query = "UPDATE " + tableName + " SET watered = 0 WHERE day_watered < " + currentDay;
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
