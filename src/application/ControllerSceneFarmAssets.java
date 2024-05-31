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

public class ControllerSceneFarmAssets {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private boolean isOwnedCan;
	private boolean isOwnedTruck;
	private boolean isOwnedTractor;
	private boolean isOwnedGloves;
	
	@FXML
	private Label labelIsOwnedCan;
	@FXML
	private Label labelIsOwnedTruck;
	@FXML
	private Label labelIsOwnedTractor;
	@FXML
	private Label labelIsOwnedGloves;
	
	
	public void initialize() {
		if (isOwnedCan) {
			labelIsOwnedCan.setText("Owned");
		}
		
		if (isOwnedTruck) {
			labelIsOwnedTruck.setText("Owned");
		}
		
		if (isOwnedTractor) {
			labelIsOwnedTractor.setText("Owned");
		}
		
		if (isOwnedGloves) {
			labelIsOwnedGloves.setText("Owned");
		}
	}
	
	public void switchToSceneFarmNavigation(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneFarmNavigation.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
