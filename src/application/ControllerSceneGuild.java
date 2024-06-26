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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerSceneGuild implements Initializable {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private Connection connection = Dbconnect.getConnection();

    private int currentDay; 
    private double money; 
    private int actionPointsRemaining; 
    private int actionPointsTotal; 
    private int deadline;
    
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
    //@FXML
    //private Label labelActionPoints;
    @FXML
    private Label labelMoney;
    @FXML
    private Label labelDeadline;
    
    @FXML
    private ImageView actionBars;

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
            updateActionBarsImage(actionPointsRemaining);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setTopTexts();
    }

    public void setTopTexts() {
        labelCurrentDay.setText(" " + currentDay);
        //labelActionPoints.setText(" " + actionPointsRemaining);
        labelMoney.setText(" " + money);
        labelDeadline.setText(" " + deadline);
    }
    
    //For the action bars
    public void updateActionBarsImage(int actionPointsRemaining) {
        if (actionPointsRemaining >= 0 && actionPointsRemaining < actionBarImages.length) {
            String imageUrl = actionBarImages[actionPointsRemaining];
            Image image = new Image(getClass().getResource(imageUrl).toExternalForm());
            actionBars.setImage(image);
        }
    }
	
	public void switchToSceneGuildBuyPlants(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneGuildBuyPlants.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToSceneGuildSellPlants(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneGuildSellPlants.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToSceneGuildUpgrades(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneGuildUpgrades.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToSceneFarmNavigation(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneFarmNavigation.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
