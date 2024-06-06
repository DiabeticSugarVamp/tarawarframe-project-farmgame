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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    
    @FXML
    private ImageView charAvatar;
    
    
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
            setAvatar();
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        
        setTopTexts(); 
    }
    
    public void setAvatar() throws SQLException {
    	String queryAvatar = "SELECT * FROM temporarystatsholder WHERE user_id = 1";
    	Statement stmtAvatar = connection.prepareStatement(queryAvatar);
    	ResultSet rsAvatar = stmtAvatar.executeQuery(queryAvatar);
    	
    	if(rsAvatar.next()) {
    		String getAvatar = rsAvatar.getString("avatar");
    		Image theAvatar = new Image(getClass().getResourceAsStream(getAvatar));
    		charAvatar.setImage(theAvatar);
    		
    	}
    	
    	
    }


    public void endDay(MouseEvent e) {
        

        currentDay++;
        actionPointsRemaining = actionPointsTotal;
        deadline -= 1;
        
        try {
        	removeUnwateredCrops("tempgrowingbronze");
            removeUnwateredCrops("tempgrowingsilver");
            removeUnwateredCrops("tempgrowinggold");
            resetWateredColumn("tempgrowingbronze");
            resetWateredColumn("tempgrowingsilver");
            resetWateredColumn("tempgrowinggold");
            
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        // Removed the code that updates tempreadytoharvest and removes growing seeds in tempgrowing tables

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

    public void setTopTexts() {
        labelCurrentDay.setText(" " + currentDay);
        labelActionPoints.setText(" " + actionPointsRemaining);
        labelMoney.setText(" " + money);
        labelDeadline.setText(" " + deadline);
        
    }

    public void deadline() {
        if (deadline == 0) {
            if (money > 0) {
                money -= 50;
                deadline += 14;
            } else {
                try {
                    switchToEndLoseScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void switchToEndLoseScene() throws IOException {
        root = FXMLLoader.load(getClass().getResource("SceneEndLose.fxml"));
        stage = (Stage) charAvatar.getScene().getWindow(); // Use any node in the current scene to get the window
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    private void resetWateredColumn(String tableName) throws SQLException {
        String query = "UPDATE " + tableName + " SET watered = 0";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.executeUpdate();
        }
    }
    
    private void removeUnwateredCrops(String tableName) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE day_watered < ? AND watered = 0";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Set the parameter for the current day minus 1
            pstmt.setInt(1, currentDay - 1);
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
