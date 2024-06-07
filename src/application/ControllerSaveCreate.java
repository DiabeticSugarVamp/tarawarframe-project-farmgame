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
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
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
            
            String deleteBronzeQuery = "DELETE FROM savedgrowingbronze WHERE save_slots = ?";
            PreparedStatement deleteBronzeStmt = connection.prepareStatement(deleteBronzeQuery);
            deleteBronzeStmt.setInt(1, slot);
            deleteBronzeStmt.executeUpdate();

            String deleteSilverQuery = "DELETE FROM savedgrowingsilver WHERE save_slots = ?";
            PreparedStatement deleteSilverStmt = connection.prepareStatement(deleteSilverQuery);
            deleteSilverStmt.setInt(1, slot);
            deleteSilverStmt.executeUpdate();

            String deleteGoldQuery = "DELETE FROM savedgrowinggold WHERE save_slots = ?";
            PreparedStatement deleteGoldStmt = connection.prepareStatement(deleteGoldQuery);
            deleteGoldStmt.setInt(1, slot);
            deleteGoldStmt.executeUpdate();
            
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
            
            String insertReadyToHarvestQuery = "INSERT INTO savedreadytoharvest (crop_bronze, crop_silver, crop_gold, save_slots) " +
                    "SELECT crop_bronze, crop_silver, crop_gold, ? FROM tempreadytoharvest " +
                    "ON DUPLICATE KEY UPDATE crop_bronze=VALUES(crop_bronze), crop_silver=VALUES(crop_silver), crop_gold=VALUES(crop_gold), save_slots=VALUES(save_slots)";
            PreparedStatement insertReadyToHarvestStmt = connection.prepareStatement(insertReadyToHarvestQuery);
            insertReadyToHarvestStmt.setInt(1, slot);
            insertReadyToHarvestStmt.executeUpdate();

            if (rs.next() && rsItems.next()) {
                int userId = rs.getInt("user_id");
                String avatar = rs.getString("avatar");
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
                    
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                    dialogPane.getStyleClass().add("alert");
                    
                    ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/assets/icon.jpg")));
                    icon.setFitHeight(48);
                    icon.setFitWidth(48); 
                    alert.setGraphic(icon); 

                    ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
                    ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(okButtonType, cancelButtonType);

                    Button okButton = (Button) alert.getDialogPane().lookupButton(okButtonType);
                    okButton.getStyleClass().add("button-ok");
                    Button cancelButton = (Button) alert.getDialogPane().lookupButton(cancelButtonType);
                    cancelButton.getStyleClass().add("button-cancel");
                    
                    alert.showAndWait();
                    

                    if (alert.getResult() == ButtonType.NO) {
                        return;
                        
                        
                    }
                    
                    
                }

                String saveQuery = "INSERT INTO savedstats (avatar, user_id, username, cur_day, cur_actions, cur_money, cur_deadline, save_slots) " +
                        "VALUES (?,?,?,?,?,?,?,?) " +
                        "ON DUPLICATE KEY UPDATE avatar = VALUES(avatar), username = VALUES(username), cur_day = VALUES(cur_day), cur_actions = VALUES(cur_actions), cur_money = VALUES(cur_money), cur_deadline = VALUES(cur_deadline)";
                
                String saveQueryItems = "INSERT INTO saveditems (seed_bronze, seed_silver, seed_gold, crop_bronze, crop_silver, crop_gold, save_slots) " +
                        "VALUES (?,?,?,?,?,?,?) " +
                        "ON DUPLICATE KEY UPDATE seed_bronze = VALUES(seed_bronze), seed_silver = VALUES(seed_silver), seed_gold = VALUES(seed_gold), crop_bronze = VALUES(crop_bronze), crop_silver = VALUES(crop_silver), crop_gold = VALUES(crop_gold)";
                
                PreparedStatement saveStmt = connection.prepareStatement(saveQuery);
                PreparedStatement saveStmtItems = connection.prepareStatement(saveQueryItems);
                
                saveStmt.setString(1, avatar);
                saveStmt.setInt(2, userId);
                saveStmt.setString(3, username);
                saveStmt.setInt(4, curDay);
                saveStmt.setInt(5, curActions);
                saveStmt.setInt(6, curMoney);
                saveStmt.setInt(7, curDeadline);
                saveStmt.setInt(8, slot);
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
