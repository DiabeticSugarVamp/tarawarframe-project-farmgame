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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    
    private int ownedBronzeSeeds;
    private int ownedSilverSeeds;
    private int ownedGoldSeeds;
    
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
    private Spinner<Integer> buyBronzeSpinner;
    @FXML
    private Spinner<Integer> buySilverSpinner;
    @FXML
    private Spinner<Integer> buyGoldSpinner;
    
    @FXML
    private Label lblOwnSeedBronze;
    @FXML
    private Label lblOwnSeedSilver;
    @FXML
    private Label lblOwnSeedGold;

    @FXML
    private Label btnBuyItems;

    int calculatedTotalValue;
    @FXML
    private TextField buyTotal;
    
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
            setOwnedSeeds();
            updateActionBarsImage(actionPointsRemaining);
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

        updateTotalValue();

        buyBronzeSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
                updateTotalValue();
                
            }
        });

        buySilverSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
                updateTotalValue();
                
            }
        });

        buyGoldSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
                updateTotalValue();
                
            }
        });

        btnBuyItems.setOnMouseClicked(this::buyTotalItems);
    }

    private void updateTotalValue() {
        calculatedTotalValue = (buyBronzeSpinner.getValue() * 5) +
                (buySilverSpinner.getValue() * 10) +
                (buyGoldSpinner.getValue() * 15);
        buyTotal.setText(Integer.toString(calculatedTotalValue));
        
    }
    
    //For the action bars
    public void updateActionBarsImage(int actionPointsRemaining) {
        if (actionPointsRemaining >= 0 && actionPointsRemaining < actionBarImages.length) {
            String imageUrl = actionBarImages[actionPointsRemaining];
            Image image = new Image(getClass().getResource(imageUrl).toExternalForm());
            actionBars.setImage(image);
        }
    }
    
    public void setOwnedSeeds() {
    	String querySeeds = "SELECT * FROM tempitems WHERE temp_id = 1";
    	try (Statement stmtSeeds = connection.createStatement();
    	     ResultSet rsSeeds = stmtSeeds.executeQuery(querySeeds)) {

    	    if (rsSeeds.next()) {
    	        ownedBronzeSeeds = rsSeeds.getInt("seed_bronze");
    	        ownedSilverSeeds = rsSeeds.getInt("seed_silver");
    	        ownedGoldSeeds = rsSeeds.getInt("seed_gold");
    	        
    	    }
    	    
    	    lblOwnSeedBronze.setText("Bronze seeds owned: " + ownedBronzeSeeds);
    	    lblOwnSeedSilver.setText("Silver seeds owned: " + ownedSilverSeeds);
    	    lblOwnSeedGold.setText("Gold seeds owned: " + ownedGoldSeeds);

    	} catch (SQLException e) {
    	    e.printStackTrace();
    	    
    	}
    	
    }

    public void buyTotalItems(MouseEvent event) {
        if (money >= calculatedTotalValue) {
            money -= calculatedTotalValue;
            updateUserMoney();
            updateTempItems();
            initialize(null, null);
           
            try {
                switchToSceneGuild(event);
                
            } catch (IOException e) {
                e.printStackTrace();
                
            }
        } else {
            showAlert("Insufficient Funds", "You do not have enough money to make this purchase.");
            
        }
    }

    private void updateUserMoney() {
        String updateSql = "UPDATE temporarystatsholder SET cur_money = ? WHERE user_id = 1";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
            pstmt.setDouble(1, money);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        setTopTexts();
        
    }

    private void updateTempItems() {
        String updateSql = "UPDATE tempitems SET seed_bronze = seed_bronze + ?, seed_silver = seed_silver + ?, seed_gold = seed_gold + ? WHERE temp_id = 1";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
            pstmt.setInt(1, buyBronzeSpinner.getValue());
            pstmt.setInt(2, buySilverSpinner.getValue());
            pstmt.setInt(3, buyGoldSpinner.getValue());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
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

    public void setTopTexts() {
        labelCurrentDay.setText(" " + currentDay);
        //labelActionPoints.setText(" " + actionPointsRemaining);
        labelMoney.setText(" " + money);
        labelDeadline.setText(" " + deadline);
        
    }

    public void switchToSceneGuild(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SceneGuild.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }
}
