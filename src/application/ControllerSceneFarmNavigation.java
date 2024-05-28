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

public class ControllerSceneFarmNavigation {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private int currentDay;
	private int money;
	private int actionPointsRemaining;
	private int actionPointsTotal;
	private int deadline;
	
	@FXML
	Label labelCurrentDay;
	@FXML
	Label labelActionPoints;
	@FXML
	Label labelMoney;
	@FXML
	Label labelDeadline;
	
	public void endDay(MouseEvent e) {
		currentDay++;
		actionPointsRemaining = actionPointsTotal;
		deadline -= 1;
		this.deadline();
		this.setTopTexts();
	}
	
	public void setTopTexts() {
		labelCurrentDay.setText("Day: " + currentDay);
		labelActionPoints.setText("Actions: " + actionPointsRemaining);
		labelMoney.setText("Money: " + money);
		labelDeadline.setText("Deadline: " + deadline);
	}
	
	public void deadline() {
		if (deadline == 0) {
			money -= 50;
			deadline += 14;
		}
	}
	
	public void switchToScenePauseMenu(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("ScenePauseMenu.fxml"));
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
}
