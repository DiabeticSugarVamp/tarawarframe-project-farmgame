package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import dbpackage.Dbconnect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerSaveCreate implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Connection connection = Dbconnect.getConnection();
    
    @FXML
    private Label btnGoBack;
    @FXML
    private Label saveSlot1;
    @FXML 
    private Label saveSlot2;
    @FXML 
    private Label saveSlot3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateSlotLabels();
    }

    public void saveCurrentSlot1(MouseEvent event) throws IOException {
        saveCurrent(event, 1);
    }

    public void saveCurrentSlot2(MouseEvent event) throws IOException {
        saveCurrent(event, 2);
    }

    public void saveCurrentSlot3(MouseEvent event) throws IOException {
        saveCurrent(event, 3);
    }

    public void saveCurrent(MouseEvent event, int slot) throws IOException {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM temporarystatsholder WHERE user_id = 1");
            
            Statement stmtItems = connection.createStatement();
            ResultSet rsItems = stmtItems.executeQuery("SELECT * FROM tempitems WHERE temp_id = 1");
            
            String insertBronzeQuery = "INSERT INTO savedgrowingbronze (grown_day, seed_planted_num, day_planted, day_watered, watered, save_slots) " +
                                        "SELECT grown_day, seed_planted_num, day_planted, day_watered, watered, ? FROM tempgrowingbronze " +
                                        "ON DUPLICATE KEY UPDATE grown_day=VALUES(grown_day), seed_planted_num=VALUES(seed_planted_num), day_watered=VALUES(day_watered), watered=VALUES(watered), save_slots=VALUES(save_slots)";
            PreparedStatement insertBronzeStmt = connection.prepareStatement(insertBronzeQuery);
            insertBronzeStmt.setInt(1, slot);
            insertBronzeStmt.executeUpdate();

            String insertSilverQuery = "INSERT INTO savedgrowingsilver (grown_day, seed_planted_num, day_planted, day_watered, watered, save_slots) " +
                                        "SELECT grown_day, seed_planted_num, day_planted, day_watered, watered, ? FROM tempgrowingsilver " +
                                        "ON DUPLICATE KEY UPDATE grown_day=VALUES(grown_day), seed_planted_num=VALUES(seed_planted_num), day_watered=VALUES(day_watered), watered=VALUES(watered), save_slots=VALUES(save_slots)";
            PreparedStatement insertSilverStmt = connection.prepareStatement(insertSilverQuery);
            insertSilverStmt.setInt(1, slot);
            insertSilverStmt.executeUpdate();

            String insertGoldQuery = "INSERT INTO savedgrowinggold (grown_day, seed_planted_num, day_planted, day_watered, watered, save_slots) " +
                                        "SELECT grown_day, seed_planted_num, day_planted, day_watered, watered, ? FROM tempgrowinggold " +
                                        "ON DUPLICATE KEY UPDATE grown_day=VALUES(grown_day), seed_planted_num=VALUES(seed_planted_num), day_watered=VALUES(day_watered), watered=VALUES(watered), save_slots=VALUES(save_slots)";
            PreparedStatement insertGoldStmt = connection.prepareStatement(insertGoldQuery);
            insertGoldStmt.setInt(1, slot);
            insertGoldStmt.executeUpdate();

            if (rs.next() && rsItems.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                int curDay = rs.getInt("cur_day");
                int curActions = rs.getInt("cur_actions");
                int curMoney = rs.getInt("cur_money");
                int curDeadline = rs.getInt("cur_deadline");
                
                int seedBronze = rsItems.getInt("seed_bronze");
                int seedSilver = rsItems.getInt("seed_silver");
                int seedGold = rsItems.getInt("seed_gold");
                int cropBronze = rsItems.getInt("crop_bronze");
                int cropSilver = rsItems.getInt("crop_silver");
                int cropGold = rsItems.getInt("crop_gold");

                String checkSlotQuery = "SELECT * FROM savedstats WHERE save_slots = ?";
                PreparedStatement checkSlotStmt = connection.prepareStatement(checkSlotQuery);
                checkSlotStmt.setInt(1, slot);
                ResultSet checkSlotRs = checkSlotStmt.executeQuery();

                if (checkSlotRs.next()) {
                    Alert alert = new Alert(AlertType.CONFIRMATION, "Slot " + slot + " is already occupied. Do you want to overwrite it?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.NO) {
                        return;
                    }
                }

                String saveQuery = "INSERT INTO savedstats (user_id, username, cur_day, cur_actions, cur_money, cur_deadline, save_slots) " +
                        "VALUES (?,?,?,?,?,?,?) " +
                        "ON DUPLICATE KEY UPDATE username = VALUES(username), cur_day = VALUES(cur_day), cur_actions = VALUES(cur_actions), cur_money = VALUES(cur_money), cur_deadline = VALUES(cur_deadline)";
                
                String saveQueryItems = "INSERT INTO saveditems (seed_bronze, seed_silver, seed_gold, crop_bronze, crop_silver, crop_gold, save_slots) " +
                        "VALUES (?,?,?,?,?,?,?) " +
                        "ON DUPLICATE KEY UPDATE seed_bronze = VALUES(seed_bronze), seed_silver = VALUES(seed_silver), seed_gold = VALUES(seed_gold), crop_bronze = VALUES(crop_bronze), crop_silver = VALUES(crop_silver), crop_gold = VALUES(crop_gold)";
                
                PreparedStatement saveStmt = connection.prepareStatement(saveQuery);
                PreparedStatement saveStmtItems = connection.prepareStatement(saveQueryItems);
                
                saveStmt.setInt(1, userId);
                saveStmt.setString(2, username);
                saveStmt.setInt(3, curDay);
                saveStmt.setInt(4, curActions);
                saveStmt.setInt(5, curMoney);
                saveStmt.setInt(6, curDeadline);
                saveStmt.setInt(7, slot);
                saveStmt.executeUpdate();
                
                saveStmtItems.setInt(1, seedBronze);
                saveStmtItems.setInt(2, seedSilver);
                saveStmtItems.setInt(3, seedGold);
                saveStmtItems.setInt(4, cropBronze);
                saveStmtItems.setInt(5, cropSilver);
                saveStmtItems.setInt(6, cropGold);
                saveStmtItems.setInt(7, slot);
                saveStmtItems.executeUpdate();
                
                saveStmtItems.close();
                saveStmt.close();
                checkSlotRs.close();
                checkSlotStmt.close();
                
            } else {
                System.out.println("No data found for user_id = 1 in temporarystatsholder");
            }

            rs.close();
            stmt.close();
            stmtItems.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateSlotLabels();
        switchToScenePauseMenu(event);
    }

    public void updateSlotLabels() {
        try {
            String query = "SELECT * FROM savedstats";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

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

            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void switchToScenePauseMenu(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ScenePauseMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
