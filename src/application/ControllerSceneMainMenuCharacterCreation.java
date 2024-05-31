package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import dbpackage.Dbconnect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerSceneMainMenuCharacterCreation {
	
	private Connection connection = Dbconnect.getConnection();
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private TextField charNameInputBox;
	
	public void switchToSceneMainMenu(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneMainMenu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		//this.initializeMainMenuMusic();
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
			/*
			String insertUser = "INSERT INTO profile (username) VALUES (?)";
			
			try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
	            pstmt.setString(1, charNameInputBox.getText());  
	            pstmt.executeUpdate();
	            
	        } catch (SQLException e1) {
	            e1.printStackTrace();
	            
	        }
			
			*/ //Turn this to normal after testing
			
			
			root = FXMLLoader.load(getClass().getResource("ScenePrologue.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			
		}
		
		
	
	}
	

	
}
