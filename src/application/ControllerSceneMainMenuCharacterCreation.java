package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import dbpackage.Dbconnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerSceneMainMenuCharacterCreation implements Initializable {

    private Connection connection = Dbconnect.getConnection();

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField charNameInputBox;

    @FXML 
    private ChoiceBox<String> avatarChoiceBox;

    @FXML
    private ImageView avatar;

    private Map<String, String> avatarMap;
    
    private String selectedAvatar;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        avatarMap = new HashMap<>();
        avatarMap.put("avatarAndre", "/assets/imageDevAndre.png");
        avatarMap.put("avatarCarl", "/assets/imageDevCarl.png");
        avatarMap.put("avatarIsrael", "/assets/imageDevIsrael.png");
        avatarMap.put("avatarKen", "/assets/imageDevKen.png");
        avatarMap.put("avatarNat", "/assets/imageDevNat.png");
        avatarMap.put("avatarGirl0", "/assets/avatars/avatarGirl0.png");
        avatarMap.put("avatarGirl3", "/assets/avatars/avatarGirl3.png");
        avatarMap.put("avatarGirl4", "/assets/avatars/avatarGirl4.png");
        avatarMap.put("avatarGirl5", "/assets/avatars/avatarGirl5.png");
        avatarMap.put("avatarGirl6", "/assets/avatars/avatarGirl6.png");
        avatarMap.put("avatarGirlMature0", "/assets/avatars/avatarGirlMature0.png");
        avatarMap.put("avatarGirlMature1", "/assets/avatars/avatarGirlMature1.png");
        avatarMap.put("avatarGirlMature2", "/assets/avatars/avatarGirlMature2.png");
        avatarMap.put("avatarGirlMature3", "/assets/avatars/avatarGirlMature3.png");
        avatarMap.put("avatarGirlMature4", "/assets/avatars/avatarGirlMature4.png");
        avatarMap.put("avatarGirlMature5", "/assets/avatars/avatarGirlMature5.png");

        
        avatarChoiceBox.getItems().addAll(avatarMap.keySet());
        avatarChoiceBox.getSelectionModel().selectFirst();
        avatarChoiceBox.setOnAction(this::setAvatar);
        setAvatar(null); 
    }

    public void setAvatar(ActionEvent event) {
        selectedAvatar = avatarChoiceBox.getValue();
        String avatarPath = avatarMap.get(selectedAvatar);
        Image avatarImage = new Image(getClass().getResourceAsStream(avatarPath));
        avatar.setImage(avatarImage);
        
    }
    
    

    private void deleteTempGrowingData() throws SQLException {
        String deleteGrowingBronzeQuery = "DELETE FROM tempgrowingbronze";
        String deleteGrowingSilverQuery = "DELETE FROM tempgrowingsilver";
        String deleteGrowingGoldQuery = "DELETE FROM tempgrowinggold";
        String resetReadyToHarvest = "UPDATE tempreadytoharvest SET crop_bronze = 0, crop_silver = 0, crop_gold = 0 WHERE temp_id = 1";
        String resetItemCrops = "UPDATE tempitems SET crop_bronze = 0, crop_silver = 0, crop_gold = 0 WHERE temp_id = 1";

        try (PreparedStatement pstmtDeleteGrowingBronze = connection.prepareStatement(deleteGrowingBronzeQuery);
             PreparedStatement pstmtDeleteGrowingSilver = connection.prepareStatement(deleteGrowingSilverQuery);
             PreparedStatement pstmtDeleteGrowingGold = connection.prepareStatement(deleteGrowingGoldQuery);
        	 PreparedStatement pstmtResetReadyToHarvest = connection.prepareStatement(resetReadyToHarvest);
        	 PreparedStatement pstmtResetItemCrops = connection.prepareStatement(resetItemCrops)) {

            pstmtDeleteGrowingBronze.executeUpdate();
            pstmtDeleteGrowingSilver.executeUpdate();
            pstmtDeleteGrowingGold.executeUpdate();
            pstmtResetReadyToHarvest.executeUpdate();
            pstmtResetItemCrops.executeUpdate();

        } catch (SQLException e) {
            throw e;
            
        }
    }

    public void switchToSceneMainMenu(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SceneMainMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScenePrologue(MouseEvent event) throws IOException {
        if (charNameInputBox.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input Required");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Please enter your username!");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");

            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/assets/icon.jpg")));
            icon.setFitHeight(48);
            icon.setFitWidth(48);
            alert.setGraphic(icon);

            ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);

            alert.getButtonTypes().setAll(okButtonType);

            Button okButton = (Button) alert.getDialogPane().lookupButton(okButtonType);
            okButton.getStyleClass().add("button-ok");

            alert.showAndWait();

        } else {
            String insertUser = "UPDATE temporarystatsholder SET username = ?, cur_day = ?, cur_actions = ?, cur_money = ?, cur_deadline = ?, avatar = ? WHERE user_id = 1";
            String insertDefItems = "UPDATE tempitems SET seed_bronze = ?, seed_silver = ?, seed_gold = ? WHERE temp_id = 1";

            try {
                deleteTempGrowingData();

                try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
                    pstmt.setString(1, charNameInputBox.getText());
                    pstmt.setInt(2, 1);
                    pstmt.setInt(3, 5);
                    pstmt.setInt(4, 100);
                    pstmt.setInt(5, 14);
                    pstmt.setString(6, avatarMap.get(selectedAvatar));
                    pstmt.executeUpdate();

                } catch (SQLException e1) {
                    e1.printStackTrace();
                    
                }

                try (PreparedStatement pstmt = connection.prepareStatement(insertDefItems)) {
                    pstmt.setInt(1, 5);
                    pstmt.setInt(2, 3);
                    pstmt.setInt(3, 1);
                    pstmt.executeUpdate();

                } catch (SQLException e2) {
                    e2.printStackTrace();
                }

                root = FXMLLoader.load(getClass().getResource("ScenePrologue.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (SQLException ex) {
                ex.printStackTrace();
                
            }
        }
    }
}
