package edu.wpi.cs3733.teamO.Controllers.Mobile;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MobileHospitalNavController implements Initializable {
  @FXML private JFXNodesList buttonsList;

  // creating icons for buttons
  Image addIcon = new Image(getClass().getResourceAsStream("/Icons/addBlack.png"));
  ImageView addIconView = new ImageView(addIcon);
  Image hospitalIcon = new Image(getClass().getResourceAsStream("/Icons/hospitalBlack.png"));
  ImageView hospitalIconView = new ImageView(hospitalIcon);
  Image parkingIcon = new Image(getClass().getResourceAsStream("/Icons/parkingBlack.png"));
  ImageView parkingIconView = new ImageView(parkingIcon);
  Image exitIcon = new Image(getClass().getResourceAsStream("/Icons/exitBlack.png"));
  ImageView exitIconView = new ImageView(exitIcon);

  // adding icons to buttons
  private final JFXButton addBtn = new JFXButton(null, addIconView);
  private final JFXButton parkingBtn = new JFXButton("Save Parking Spot", parkingIconView);
  private final JFXButton hospitalBtn = new JFXButton("Hospital Navigation", hospitalIconView);
  private final JFXButton exitBtn = new JFXButton("Exit Navigation", exitIconView);

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // set the icons sizes
    addIconView.setFitWidth(15);
    addIconView.setFitHeight(15);
    hospitalIconView.setFitWidth(25);
    hospitalIconView.setFitHeight(25);
    parkingIconView.setFitWidth(25);
    parkingIconView.setFitHeight(25);
    exitIconView.setFitWidth(20);
    exitIconView.setFitHeight(20);

    // style the buttons
    addBtn.getStyleClass().addAll("nav-menu-button");
    addBtn.setButtonType(JFXButton.ButtonType.RAISED);
    parkingBtn.getStyleClass().addAll("nav-buttons");
    parkingBtn.setButtonType(JFXButton.ButtonType.RAISED);
    hospitalBtn.getStyleClass().addAll("nav-buttons");
    hospitalBtn.setButtonType(JFXButton.ButtonType.RAISED);
    exitBtn.getStyleClass().addAll("nav-buttons");
    exitBtn.setButtonType(JFXButton.ButtonType.RAISED);

    // add them to be in an animated node list
    buttonsList.addAnimatedNode(addBtn);
    buttonsList.addAnimatedNode(parkingBtn);
    buttonsList.addAnimatedNode(hospitalBtn);
    buttonsList.addAnimatedNode(exitBtn);
    buttonsList.setSpacing(20);
    buttonsList.setRotate(180);
    buttonsList.setAlignment(Pos.CENTER_RIGHT);
    buttonFunction();
  }

  private void buttonFunction() {

    parkingBtn.setOnAction(
        actionEvent -> {
          // TODO: save parking spot
        });

    hospitalBtn.setOnAction(
        actionEvent -> {
          // TODO: navigate hospital campus, maybe another nodes list with drop downs?
        });
  }
}
