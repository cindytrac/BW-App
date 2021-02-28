package edu.wpi.cs3733.teamO.Controllers.Popups;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.teamO.Database.UserHandling;
import edu.wpi.cs3733.teamO.Opp;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class PatientSignInPopupController {

  @FXML private JFXTextField username;
  @FXML private JFXPasswordField password;

  @FXML
  public void signIn(ActionEvent actionEvent) {
    UserHandling.login(username.getText(), password.getText());

    try {
      GridPane root = FXMLLoader.load(getClass().getResource("/Views/MainPage.fxml"));
      Opp.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void close(ActionEvent actionEvent) {
    System.out.println("EASY");
  }

  public void createAccount(ActionEvent actionEvent) {
    try {
      GridPane root = FXMLLoader.load(getClass().getResource("/Views/CreateAccount.fxml"));
      Opp.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
