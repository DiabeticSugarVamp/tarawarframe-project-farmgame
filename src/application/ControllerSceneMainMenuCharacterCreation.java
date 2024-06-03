package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import dbpackage.Dbconnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerSceneMainMenuCharacterCreation implements Initializable{
	
	private Connection connection = Dbconnect.getConnection();
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private TextField charNameInputBox;
	
	@FXML 
	private ChoiceBox<String> avatarChoiceBox;
	
	@FXML
	private ImageView avatar;
	
	Image avatarAndre = new Image(getClass().getResourceAsStream("/assets/imageDevAndre.png"));
	Image avatarCarl = new Image(getClass().getResourceAsStream("/assets/imageDevCarl.png"));
	Image avatarIsrael = new Image(getClass().getResourceAsStream("/assets/imageDevIsrael.png"));
	Image avatarKen = new Image(getClass().getResourceAsStream("/assets/imageDevKen.png"));
	Image avatarNat = new Image(getClass().getResourceAsStream("/assets/imageDevNat.png"));
	
	private String[] avatarsStr = {"avatarAndre", "avatarCarl", "avatarIsrael", "avatarKen", "avatarNat"};
	
	private Image[] avatars = {avatarAndre,
								avatarCarl,
								avatarIsrael,
								avatarKen,
								avatarNat};
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		avatarChoiceBox.getItems().addAll(avatarsStr);
		avatarChoiceBox.getSelectionModel().selectFirst();
		avatarChoiceBox.setOnAction(this::setAvatar);
	}
	
	public void setAvatar(ActionEvent event) {
		String myAvatar = avatarChoiceBox.getValue();
		int avatarCounter = 0;
		
		while (true) {
			String avatarName = avatarsStr[avatarCounter];
			Image avatarToSet = avatars[avatarCounter];
			
			if (avatarName.equals(myAvatar)) {
				avatar.setImage(avatarToSet);
				break;
				
			}
			avatarCounter++;
		}
		
	}
	
	private void deleteTempGrowingData() throws SQLException {
        String deleteGrowingBronzeQuery = "DELETE FROM tempgrowingbronze";
        String deleteGrowingSilverQuery = "DELETE FROM tempgrowingsilver";
        String deleteGrowingGoldQuery = "DELETE FROM tempgrowinggold";
        
        try (PreparedStatement pstmtDeleteGrowingBronze = connection.prepareStatement(deleteGrowingBronzeQuery);
             PreparedStatement pstmtDeleteGrowingSilver = connection.prepareStatement(deleteGrowingSilverQuery);
             PreparedStatement pstmtDeleteGrowingGold = connection.prepareStatement(deleteGrowingGoldQuery)) {
            
            pstmtDeleteGrowingBronze.executeUpdate();
            pstmtDeleteGrowingSilver.executeUpdate();
            pstmtDeleteGrowingGold.executeUpdate();
            
        } catch (SQLException e) {
            throw e;
        }
    }
	
	public void switchToSceneMainMenu(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneMainMenu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToScenePrologue(MouseEvent event) throws IOException {
		
		if(charNameInputBox.getText().isBlank()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Idiot!");
	        alert.setHeaderText("Idiocy");
	        alert.setContentText("Input your username pls! ohmahgahd!");

	        DialogPane dialogPane = alert.getDialogPane();
	        dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	        dialogPane.getStyleClass().add("alert");
	        
	        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/assets/icon.jpg")));
	        icon.setFitHeight(48);
	        icon.setFitWidth(48); 
	        alert.setGraphic(icon); 

	        ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
	      
	        alert.getButtonTypes().setAll(okButtonType);

	        Button okButton = (Button) alert.getDialogPane().lookupButton(okButtonType);
	        okButton.getStyleClass().add("button-ok");

	        alert.showAndWait();

			
		}else {
			
			String insertUser = "UPDATE temporarystatsholder SET username = ?, cur_day = ?, cur_actions = ?, cur_money = ?, cur_deadline = ? WHERE user_id = 1";
			String insertDefItems = "UPDATE tempitems SET seed_bronze = ?, seed_silver = ?, seed_gold = ? WHERE temp_id = 1";
			
			try {
                // Delete tempgrowing data
                deleteTempGrowingData();
                
                // Insert new user data
                try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
                    pstmt.setString(1, charNameInputBox.getText());
                    pstmt.setInt(2, 1);
                    pstmt.setInt(3, 5);
                    pstmt.setInt(4, 100);
                    pstmt.setInt(5, 14);
                    pstmt.executeUpdate();
                    
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                
                // Insert default items data
                try (PreparedStatement pstmt = connection.prepareStatement(insertDefItems)) {
                    pstmt.setInt(1, 5);
                    pstmt.setInt(2, 3);
                    pstmt.setInt(3, 1);
                    pstmt.executeUpdate();
                    
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
                
                // Switch to ScenePrologue
                root = FXMLLoader.load(getClass().getResource("ScenePrologue.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        
			
		}
		
		
	
	}

	
	

	
}
