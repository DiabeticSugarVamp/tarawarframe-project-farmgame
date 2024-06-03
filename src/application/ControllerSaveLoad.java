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

    public void loadSlot1(MouseEvent event) throws IOException {
        loadSlot(event, 1);
    }

    public void loadSlot2(MouseEvent event) throws IOException {
        loadSlot(event, 2);
    }

    public void loadSlot3(MouseEvent event) throws IOException {
        loadSlot(event, 3);
    }

    public void loadSlot(MouseEvent event, int slot) throws IOException {
        String query = "SELECT * FROM savedstats WHERE save_slots = ?";
        String insertQuery = "UPDATE temporarystatsholder SET username = ?, cur_day = ?, cur_actions = ?, cur_money = ?, cur_deadline = ? WHERE user_id = 1";
        String query2 = "SELECT * FROM saveditems WHERE save_slots = ?";
        String insertQueryItems = "UPDATE tempitems SET seed_bronze = ?, seed_silver = ?, seed_gold = ?, crop_bronze = ?, crop_silver = ?, crop_gold = ? WHERE temp_id = 1";

        try (
            PreparedStatement pstmtSelect = connection.prepareStatement(query);
            PreparedStatement pstmtUpdate = connection.prepareStatement(insertQuery);
            PreparedStatement pstmtSelectItems = connection.prepareStatement(query2);
            PreparedStatement pstmtUpdateItems = connection.prepareStatement(insertQueryItems)
            		
        ) {
            pstmtSelect.setInt(1, slot);
            ResultSet rs = pstmtSelect.executeQuery();

            if (rs.next()) {
                pstmtUpdate.setString(1, rs.getString("username"));
                pstmtUpdate.setInt(2, rs.getInt("cur_day"));
                pstmtUpdate.setInt(3, rs.getInt("cur_actions"));
                pstmtUpdate.setInt(4, rs.getInt("cur_money"));
                pstmtUpdate.setInt(5, rs.getInt("cur_deadline"));
                pstmtUpdate.executeUpdate();
                
                
            }
            rs.close();

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
