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

public class ControllerSceneFarmInventories implements Initializable {
	private Stage stage;
    private Scene scene;
    private Parent root;
    
    private Connection connection = Dbconnect.getConnection();
    
    private int seedBronze;
    private int seedSilver;
    private int seedGold;
    private int cropBronze;
    private int cropSilver;
    private int cropGold;
    
    @FXML
    private Label lblOwnSeedBronze;
    @FXML
    private Label lblOwnSeedSilver;
    @FXML
    private Label lblOwnSeedGold;
    @FXML
    private Label lblOwnCropsBronze;
    @FXML
    private Label lblOwnCropsSilver;
    @FXML
    private Label lblOwnCropsGold;
    
    
    public void getItems() throws SQLException {
    	Statement stmt = connection.createStatement();
    	ResultSet rs = stmt.executeQuery("SELECT * FROM tempitems WHERE temp_id = 1");
    	
    	if(rs.next()) {
    		seedBronze = rs.getInt("seed_bronze");
    	    seedSilver = rs.getInt("seed_silver");
    	    seedGold = rs.getInt("seed_gold");
    	    cropBronze = rs.getInt("crop_bronze");
    	    cropSilver = rs.getInt("crop_silver");
    	    cropGold = rs.getInt("crop_gold");
    		
    	}
    	
    	
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
        	getItems();
        	
        }catch(SQLException e) {
        	e.printStackTrace();
        	
        }
        setItemsLabels();
    	
    }
    
    public void setItemsLabels() {
    	
    	
    	lblOwnSeedBronze.setText("Bronze seeds owned: " + seedBronze);
        lblOwnSeedSilver.setText("Silver seeds owned: " + seedSilver);
        lblOwnSeedGold.setText("Gold seeds owned: " + seedGold);
        lblOwnCropsBronze.setText("Bronze crops owned: " + cropBronze);
        lblOwnCropsSilver.setText("Silver crops owned: " + cropSilver);
        lblOwnCropsGold.setText("Gold crops owned: " + cropGold);
    	
    	
    }
    
    public void switchToSceneFarmFields(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SceneFarmFields.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
