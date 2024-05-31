package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import dbpackage.Dbconnect;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerSceneGuildBuyPlants implements Initializable {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private Connection connection = Dbconnect.getConnection();

    private int currentDay; 
    private double money; 
    private int actionPointsRemaining; 
    private int actionPointsTotal; 
    private int deadline;
    
    @FXML
    private Label labelCurrentDay;
    @FXML
    private Label labelActionPoints;
    @FXML
    private Label labelMoney;
    @FXML
    private Label labelDeadline;
    
    @FXML
    private Spinner<Integer> buyBronzeSpinner;
    @FXML 
    private Spinner<Integer> buySilverSpinner;
    @FXML
    private Spinner<Integer> buyGoldSpinner;
    
    int calculatedTotalValue;
    @FXML
    private TextField buyTotal;
    
    
    public void getUser() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM profile WHERE user_id = 1");

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
        
        SpinnerValueFactory<Integer> bronzeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        bronzeValueFactory.setValue(0);
        buyBronzeSpinner.setValueFactory(bronzeValueFactory);
        
        SpinnerValueFactory<Integer> silverValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        silverValueFactory.setValue(0);
        buySilverSpinner.setValueFactory(silverValueFactory);
        
        SpinnerValueFactory<Integer> goldValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        goldValueFactory.setValue(0);
        buyGoldSpinner.setValueFactory(goldValueFactory);
        
        calculatedTotalValue = (buyBronzeSpinner.getValue() * 5) + 
        		(buySilverSpinner.getValue() * 10) + 
        		(buyGoldSpinner.getValue() * 15);
        buyTotal.setText(Integer.toString(calculatedTotalValue));
        
        buyBronzeSpinner.valueProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				
				calculatedTotalValue = (buyBronzeSpinner.getValue() * 5) + 
		        		(buySilverSpinner.getValue() * 10) + 
		        		(buyGoldSpinner.getValue() * 15);
				buyTotal.setText(Integer.toString(calculatedTotalValue));
			}
        	
        });
        
        buySilverSpinner.valueProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				
				calculatedTotalValue = (buyBronzeSpinner.getValue() * 5) + 
		        		(buySilverSpinner.getValue() * 10) + 
		        		(buyGoldSpinner.getValue() * 15);
				buyTotal.setText(Integer.toString(calculatedTotalValue));
			}
        	
        });
        
        buyGoldSpinner.valueProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				
				calculatedTotalValue = (buyBronzeSpinner.getValue() * 5) + 
		        		(buySilverSpinner.getValue() * 10) + 
		        		(buyGoldSpinner.getValue() * 15);
				buyTotal.setText(Integer.toString(calculatedTotalValue));
			}
        	
        });
        
    }

    public void setTopTexts() {
        labelCurrentDay.setText("Day: " + currentDay);
        labelActionPoints.setText("Actions: " + actionPointsRemaining);
        labelMoney.setText("Money: " + money);
        labelDeadline.setText("Deadline: " + deadline);
    }
	
	public void switchToSceneGuild(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneGuild.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
