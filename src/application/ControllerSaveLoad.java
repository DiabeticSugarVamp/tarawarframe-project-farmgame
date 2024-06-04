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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerSaveLoad implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Connection connection = Dbconnect.getConnection();

    @FXML
    Label saveSlot1;
    @FXML
    Label saveSlot2;
    @FXML
    Label saveSlot3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadSlotLabels();
    }

    public void loadSlot1(MouseEvent event) throws IOException, SQLException {
        loadSlot(event, 1);
    }

    public void loadSlot2(MouseEvent event) throws IOException, SQLException {
        loadSlot(event, 2);
    }

    public void loadSlot3(MouseEvent event) throws IOException, SQLException {
        loadSlot(event, 3);
    }

    public void loadSlot(MouseEvent event, int slot) throws IOException, SQLException {
        String selectStatsQuery = "SELECT * FROM savedstats WHERE save_slots = ?";
        String selectItemsQuery = "SELECT * FROM saveditems WHERE save_slots = ?";
        String selectGrowingBronzeQuery = "SELECT * FROM savedgrowingbronze WHERE save_slots = ?";
        String selectGrowingSilverQuery = "SELECT * FROM savedgrowingsilver WHERE save_slots = ?";
        String selectGrowingGoldQuery = "SELECT * FROM savedgrowinggold WHERE save_slots = ?";

        String updateStatsQuery = "UPDATE temporarystatsholder SET username = ?, cur_day = ?, cur_actions = ?, cur_money = ?, cur_deadline = ? WHERE user_id = 1";
        String updateItemsQuery = "UPDATE tempitems SET seed_bronze = ?, seed_silver = ?, seed_gold = ?, crop_bronze = ?, crop_silver = ?, crop_gold = ? WHERE temp_id = 1";
        String insertGrowingBronzeQuery = "INSERT INTO tempgrowingbronze (day_planted, grown_day, seed_planted_num, watered, day_watered) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE grown_day = VALUES(grown_day), seed_planted_num = VALUES(seed_planted_num), watered = VALUES(watered), day_watered = VALUES(day_watered)";
        String insertGrowingSilverQuery = "INSERT INTO tempgrowingsilver (day_planted, grown_day, seed_planted_num, watered, day_watered) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE grown_day = VALUES(grown_day), seed_planted_num = VALUES(seed_planted_num), watered = VALUES(watered), day_watered = VALUES(day_watered)";
        String insertGrowingGoldQuery = "INSERT INTO tempgrowinggold (day_planted, grown_day, seed_planted_num, watered, day_watered) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE grown_day = VALUES(grown_day), seed_planted_num = VALUES(seed_planted_num), watered = VALUES(watered), day_watered = VALUES(day_watered)";
        String deleteGrowingBronzeQuery = "DELETE FROM tempgrowingbronze";
        String deleteGrowingSilverQuery = "DELETE FROM tempgrowingsilver";
        String deleteGrowingGoldQuery = "DELETE FROM tempgrowinggold";
        
        String selectHarvest = "SELECT crop_bronze, crop_silver, crop_gold FROM savedreadytoharvest WHERE save_slots = ?";
        PreparedStatement pstmtSelectHarvest = connection.prepareStatement(selectHarvest);
        pstmtSelectHarvest.setInt(1, slot); // Set the value of the slot
        ResultSet rsHarvest = pstmtSelectHarvest.executeQuery();

       
        if (rsHarvest.next()) {
            
            int cropBronze = rsHarvest.getInt("crop_bronze");
            int cropSilver = rsHarvest.getInt("crop_silver");
            int cropGold = rsHarvest.getInt("crop_gold");

           
            String updateHarvest = "INSERT INTO tempreadytoharvest (crop_bronze, crop_silver, crop_gold) VALUES (?, ?, ?) " +
                                   "ON DUPLICATE KEY UPDATE crop_bronze = VALUES(crop_bronze), crop_silver = VALUES(crop_silver), crop_gold = VALUES(crop_gold)";
            PreparedStatement pstmtUpdateHarvest = connection.prepareStatement(updateHarvest);
            pstmtUpdateHarvest.setInt(1, cropBronze);
            pstmtUpdateHarvest.setInt(2, cropSilver);
            pstmtUpdateHarvest.setInt(3, cropGold);
            pstmtUpdateHarvest.executeUpdate();
        }

        
        rsHarvest.close();
        pstmtSelectHarvest.close();
        

        try (
            PreparedStatement pstmtSelectStats = connection.prepareStatement(selectStatsQuery);
            PreparedStatement pstmtSelectItems = connection.prepareStatement(selectItemsQuery);
            PreparedStatement pstmtSelectGrowingBronze = connection.prepareStatement(selectGrowingBronzeQuery);
            PreparedStatement pstmtSelectGrowingSilver = connection.prepareStatement(selectGrowingSilverQuery);
            PreparedStatement pstmtSelectGrowingGold = connection.prepareStatement(selectGrowingGoldQuery);
        		
            PreparedStatement pstmtUpdateStats = connection.prepareStatement(updateStatsQuery);
            PreparedStatement pstmtUpdateItems = connection.prepareStatement(updateItemsQuery);
            PreparedStatement pstmtInsertGrowingBronze = connection.prepareStatement(insertGrowingBronzeQuery);
            PreparedStatement pstmtInsertGrowingSilver = connection.prepareStatement(insertGrowingSilverQuery);
            PreparedStatement pstmtInsertGrowingGold = connection.prepareStatement(insertGrowingGoldQuery);
            PreparedStatement pstmtDeleteGrowingBronze = connection.prepareStatement(deleteGrowingBronzeQuery);
            PreparedStatement pstmtDeleteGrowingSilver = connection.prepareStatement(deleteGrowingSilverQuery);
            PreparedStatement pstmtDeleteGrowingGold = connection.prepareStatement(deleteGrowingGoldQuery)) {
        	
        	
            
        	pstmtDeleteGrowingBronze.executeUpdate();
            pstmtDeleteGrowingSilver.executeUpdate();
            pstmtDeleteGrowingGold.executeUpdate();

            
            pstmtSelectStats.setInt(1, slot);
            ResultSet rsStats = pstmtSelectStats.executeQuery();
            if (rsStats.next()) {
                
                pstmtUpdateStats.setString(1, rsStats.getString("username"));
                pstmtUpdateStats.setInt(2, rsStats.getInt("cur_day"));
                pstmtUpdateStats.setInt(3, rsStats.getInt("cur_actions"));
                pstmtUpdateStats.setDouble(4, rsStats.getDouble("cur_money"));
                pstmtUpdateStats.setInt(5, rsStats.getInt("cur_deadline"));
                pstmtUpdateStats.executeUpdate();
                
            }
            
            
            rsStats.close();

            
            pstmtSelectItems.setInt(1, slot);
            ResultSet rsItems = pstmtSelectItems.executeQuery();
            if (rsItems.next()) {
               
                pstmtUpdateItems.setInt(1, rsItems.getInt("seed_bronze"));
                pstmtUpdateItems.setInt(2, rsItems.getInt("seed_silver"));
                pstmtUpdateItems.setInt(3, rsItems.getInt("seed_gold"));
                pstmtUpdateItems.setInt(4, rsItems.getInt("crop_bronze"));
                pstmtUpdateItems.setInt(5, rsItems.getInt("crop_silver"));
                pstmtUpdateItems.setInt(6, rsItems.getInt("crop_gold"));
                pstmtUpdateItems.executeUpdate();
                
            }
            
            rsItems.close();

            
            pstmtSelectGrowingBronze.setInt(1, slot);
            ResultSet rsGrowingBronze = pstmtSelectGrowingBronze.executeQuery();
            while (rsGrowingBronze.next()) {
                pstmtInsertGrowingBronze.setInt(1, rsGrowingBronze.getInt("day_planted"));
                pstmtInsertGrowingBronze.setInt(2, rsGrowingBronze.getInt("grown_day"));
                pstmtInsertGrowingBronze.setInt(3, rsGrowingBronze.getInt("seed_planted_num"));
                pstmtInsertGrowingBronze.setInt(4, rsGrowingBronze.getInt("watered"));
                pstmtInsertGrowingBronze.setInt(5, rsGrowingBronze.getInt("day_watered"));
                pstmtInsertGrowingBronze.executeUpdate();
                
            }
            
            
            rsGrowingBronze.close();

           
            pstmtSelectGrowingSilver.setInt(1, slot);
            ResultSet rsGrowingSilver = pstmtSelectGrowingSilver.executeQuery();
            while (rsGrowingSilver.next()) {
                pstmtInsertGrowingSilver.setInt(1, rsGrowingSilver.getInt("day_planted"));
                pstmtInsertGrowingSilver.setInt(2, rsGrowingSilver.getInt("grown_day"));
                pstmtInsertGrowingSilver.setInt(3, rsGrowingSilver.getInt("seed_planted_num"));
                pstmtInsertGrowingSilver.setInt(4, rsGrowingSilver.getInt("watered"));
                pstmtInsertGrowingSilver.setInt(5, rsGrowingSilver.getInt("day_watered"));
                pstmtInsertGrowingSilver.executeUpdate();
                
            }
            
            rsGrowingSilver.close();

            
            pstmtSelectGrowingGold.setInt(1, slot);
            ResultSet rsGrowingGold = pstmtSelectGrowingGold.executeQuery();
            while (rsGrowingGold.next()) {
                pstmtInsertGrowingGold.setInt(1, rsGrowingGold.getInt("day_planted"));
                pstmtInsertGrowingGold.setInt(2, rsGrowingGold.getInt("grown_day"));
                pstmtInsertGrowingGold.setInt(3, rsGrowingGold.getInt("seed_planted_num"));
                pstmtInsertGrowingGold.setInt(4, rsGrowingGold.getInt("watered"));
                pstmtInsertGrowingGold.setInt(5, rsGrowingGold.getInt("day_watered"));
                pstmtInsertGrowingGold.executeUpdate();
                
                
            }
            
            rsGrowingGold.close();

           
            root = FXMLLoader.load(getClass().getResource("SceneFarmNavigation.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
            
        } catch (SQLException e) {
            e.printStackTrace();
            
            
        }
        
    }

    public void loadSlotLabels() {
        String query = "SELECT * FROM savedstats WHERE save_slots IN (1, 2, 3)";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int slot = rs.getInt("save_slots");
                String username = rs.getString("username");
                int curDay = rs.getInt("cur_day");
                int curDeadline = rs.getInt("cur_deadline");

                String labelText = "User: " + username + ", Days: " + curDay + ", Deadline: " + curDeadline;
                switch (slot) {
                    case 1:
                        saveSlot1.setText(labelText);
                        break;
                        
                    case 2:
                        saveSlot2.setText(labelText);
                        break;
                        
                    case 3:
                        saveSlot3.setText(labelText);
                        break;
                        
                    default:
                        System.out.println("Invalid slot: " + slot);
                        break;
                        
                }
                
                
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
            
            
        }
        
    }

    public void switchToSceneMainMenu(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SceneMainMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }
}
