package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerSceneGuildUpgrades {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void switchToSceneGuild(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneGuild.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
