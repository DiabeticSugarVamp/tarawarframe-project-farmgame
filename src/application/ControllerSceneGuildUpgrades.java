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
	
	private int money;
	private boolean isOwnedCan;
	private boolean isOwnedTruck;
	private boolean isOwnedTractor;
	private boolean isOwnedGloves;
	
	public void switchToSceneGuild(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneGuild.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void initialize() {
		
	}
	
	public void buyCan(MouseEvent event) throws IOException {
		if (!isOwnedCan && money >= 200) {
			money -= 200;
			isOwnedCan = true;
			
		}
	}
	
	public void buyTruck(MouseEvent event) throws IOException {
		if (!isOwnedTruck && money >= 200) {
			money -= 200;
			isOwnedTruck = true;
		}
	}

	public void buyTractor(MouseEvent event) throws IOException {
		if (!isOwnedTractor && money >= 200) {
			money -= 200;
			isOwnedTractor = true;
		}
	}

	public void buyGloves(MouseEvent event) throws IOException {
		if (!isOwnedGloves && money >= 200) {
			money -= 200;
			isOwnedGloves = true;
		}
	}
	
	public void buyMansion(MouseEvent event) throws IOException {
		if (money >= 300) {
			root = FXMLLoader.load(getClass().getResource("SceneYouWin.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
}
