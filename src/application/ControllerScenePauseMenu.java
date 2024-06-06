package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dbpackage.Dbconnect;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerScenePauseMenu {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private Connection connection = Dbconnect.getConnection();
	
	public void switchToSceneFarmNavigation(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneFarmNavigation.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}
	
	public void switchToSceneMainMenuSaves(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneMainMenuSaves.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}
	
	public void switchToSceneMainMenu(MouseEvent event) throws IOException {
		
		
		root = FXMLLoader.load(getClass().getResource("SceneMainMenu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
