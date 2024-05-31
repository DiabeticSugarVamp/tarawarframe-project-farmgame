package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerSaveCreate {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	Label btnGoBack;
	@FXML
	Label saveSlot1;
	@FXML 
	Label saveSlot2;
	@FXML 
	Label saveSlot3;
	
	public void switchToScenePauseMenu(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("ScenePauseMenu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
