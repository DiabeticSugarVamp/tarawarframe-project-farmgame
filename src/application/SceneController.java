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
	
	private File file;
	private Media media;
	private MediaPlayer mediaPlayer;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void switchToSceneMainMenu(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneMainMenu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		this.initializeMainMenuMusic();
		stage.show();
	}
	
	public void switchToSceneCredits(MouseEvent event) throws IOException {
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
	
	public void toggleFullscreen(MouseEvent event) {
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		
		if (!stage.isFullScreen()) {
			stage.setFullScreen(true);
		} else {
			stage.setFullScreen(false);
		}
		
	}
	
	public void initializeMainMenuMusic() {
		String mediaPath = "/assets/Effervescence.mp3";
		Media media = new Media(getClass().getResource(mediaPath).toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}
	
	
}
