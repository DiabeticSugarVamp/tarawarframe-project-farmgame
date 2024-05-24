package application;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class SceneController {
	@FXML
	private MediaView mainMenuMusic;
	
	@FXML
	private Button exitPromptButton;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void switchToSceneMainMenu(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneMainMenu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		//this.initializeMainMenuMusic();
		stage.show();
	}
	
	public void switchToSceneMainMenuSaves(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneMainMenuSaves.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToSceneMainMenuCredits(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneMainMenuCredits.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToSceneMainMenuAboutUs(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneMainmenuAboutUs.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToSceneMainMenuTutorial(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneMainMenuTutorial.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void exitPrompt(MouseEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("exit");
		alert.setHeaderText("You are about to exit!");
		alert.setContentText("Are you sure you want to exit the game?");
		
		if (alert.showAndWait().get() == ButtonType.OK) {
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
		}
	}
	
	public void switchToScenePrologue(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("ScenePrologue.fxml"));
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
	
	public void switchToSceneFarmFields(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneFarmFields.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToSceneFarmAssets(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneFarmAssets.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToSceneGuild(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneGuild.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
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
	
	/*
	 * public void switchToScenePauseMenu(KeyEvent event) throws IOException {
	 * Parent root = FXMLLoader.load(getClass().getResource("ScenePauseMenu.fxml"));
	 * Stage stage = null;
	 * 
	 * // Check if the source is a Node if (event.getSource() instanceof Node) {
	 * stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); } // Check
	 * if the source is a Scene else if (event.getSource() instanceof Scene) { stage
	 * = (Stage) ((Scene) event.getSource()).getWindow(); }
	 * 
	 * if (stage != null) { Scene scene = new Scene(root); stage.setScene(scene);
	 * stage.show(); } else { // Handle error: stage is null
	 * System.err.println("Unable to determine the stage from the event source."); }
	 * }
	 */
	
	/*
	 * public void initializeMainMenuMusic() { String mediaPath =
	 * "/assets/Effervescence.mp3"; Media media = new
	 * Media(getClass().getResource(mediaPath).toString()); mediaPlayer = new
	 * MediaPlayer(media); mediaPlayer.play(); }
	 */
	
	
}
