package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("SceneIntro.fxml"));
			Scene scene = new Scene(root,1024,768);
			
			String css = this.getClass().getResource("application.css").toExternalForm();
			
			String iconPath = "/assets/icon.jpg";
			Image icon = new Image(getClass().getResource(iconPath).toString());
			stage.getIcons().add(icon);
			
			stage.setTitle("Plow to Profit");
			stage.setResizable(false);
			scene.getStylesheets().add(css);
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
}
