package application;

import java.io.IOException;
import java.io.File;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class GameController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	// Prologue stuff
	private int prologueCounter = 0;
	
	@FXML
	ImageView prologueImageView;
	@FXML
	Label prologueButton;
	@FXML
	Label prologueText; 
	
	Image prologueImage2 = new Image(getClass().getResourceAsStream("/assets/imagePrologue2.jpg"));
	Image prologueImage3 = new Image(getClass().getResourceAsStream("/assets/imagePrologue3.jpg"));
	Image prologueImage4 = new Image(getClass().getResourceAsStream("/assets/imagePrologue4.jpg"));
	
	
	// Game stuff
	private boolean initializeStartingValues;
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
	
	public void initializeSceneFarmNavigation() {
		if (initializeStartingValues) {
			currentDay = 1;
			actionPointsTotal = 3;
			actionPointsRemaining = actionPointsTotal;
			money = 100;
			deadline = 14;
			this.setTopTexts();
		}
		
		if (!initializeStartingValues) {
			this.setTopTexts();
		}
		
		initializeStartingValues = false;
	}
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
	
	// Switching scenes
	public void switchToSceneFarmNavigation(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SceneFarmNavigation.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		this.setTopTexts();
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
	
	// Prologue control
	public void displayImage(MouseEvent event) throws IOException {
		if (prologueCounter == 0) {
			prologueImageView.setImage(prologueImage2);
			prologueText.setText("your landlord, a man of stern countenance "
					+ "and a penchant for punctuality, demands his due every "
					+ "fortnight. Like clockwork, he arrives at your doorstep, "
					+ "his gaze as sharp as the edge of a freshly sharpened plow. "
					+ "With each visit, you feel the weight of his expectations "
					+ "pressing down upon you. In this bucolic haven, survival is "
					+ "measured not in gold but in the sweat of your brow and the yield of your fields.");
		}
		
		if (prologueCounter == 1) {
			prologueImageView.setImage(prologueImage3);
			prologueText.setText("As you toil under his watchful eye, "
					+ "you dream of the day you’ll plow a different furrow, "
					+ "leading to ownership of the land you so diligently cultivate—"
					+ "a plowing of passions that ignites the flames of independence. "
					+ "You yearn to plant your seeds in straight rows, "
					+ "caress tender shoots as they emerge, and nurture them until they blossom into ripe, "
					+ "succulent fruits. When harvest time comes, you gather the fruits of your "
					+ "labor with hands as gentle as a lover’s touch, savoring each moment like "
					+ "the first kiss of spring.");
		}
		
		if (prologueCounter == 2) {
			prologueImageView.setImage(prologueImage4);
			prologueText.setText("With every stroke of the scythe and thrust of the pitchfork, "
					+ "you dance with nature's rhythm, surrendering to the primal ecstasy of creation. "
					+ "As the sun sets, you retire to your humble abode, weary but content, "
					+ "knowing that you have sown the seeds of your destiny and will soon reap the "
					+ "rewards of a life truly lived.");
		}
		
		if (prologueCounter == 3) {
			root = FXMLLoader.load(getClass().getResource("SceneFarmNavigation.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		
		prologueCounter++;
	}
}
