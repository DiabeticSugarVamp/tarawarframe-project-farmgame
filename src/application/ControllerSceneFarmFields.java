package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
    private Label btnGoToInvetories;

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
    }

    public void setTopTexts() {
        labelCurrentDay.setText("Day: " + currentDay);
        labelActionPoints.setText("Actions: " + actionPointsRemaining);
        labelMoney.setText("Money: " + money);
        labelDeadline.setText("Deadline: " + deadline);
    }
    
    public void plantSeeds(MouseEvent e) throws IOException {
    	if (!isOwnedGloves) {
    		
    	}
    }
    
    public void harvestCrops(MouseEvent e) throws IOException {
    	if (!isOwnedTractor) {
    		
    	}
    }
    
    public void waterCrops(MouseEvent e) throws IOException {
    	if (!isOwnedCan) {
    		
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
