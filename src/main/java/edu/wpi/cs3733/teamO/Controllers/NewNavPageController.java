package edu.wpi.cs3733.teamO.Controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.teamO.GraphSystem.Graph;
import edu.wpi.cs3733.teamO.Opp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class NewNavPageController implements Initializable {

  @FXML private HBox hboxRef;
  @FXML private VBox vboxRef;
  @FXML private RowConstraints row0;
  @FXML private RowConstraints row1;
  @FXML private ColumnConstraints col1;
  @FXML private GridPane gridPane;
  @FXML private JFXDrawer drawerSM1;
  @FXML private JFXHamburger hamburgerMainBtn1;
  @FXML private AnchorPane anchorPane;
  @FXML private Canvas canvas;
  @FXML private VBox topMenu;
  @FXML private JFXDrawer drawerSM;
  @FXML private JFXHamburger hamburgerMainBtn;
  @FXML private BorderPane borderPane;
  @FXML private JFXToggleButton editToggle;
  @FXML private ImageView imageView;
  @FXML private JFXComboBox<String> floorSelectionBtn;
  @FXML private JFXButton startLocBtn;
  @FXML private JFXButton endLocBtn;
  @FXML private JFXButton pathfindBtn;

  ObservableList<String> listOfFloors =
      FXCollections.observableArrayList(
          "Campus", "Floor 1", "Floor 2", "Floor 3", "Floor 4", "Floor 5");

  public static Image campusMap = new Image("FaulknerCampus_Updated.png");
  public static Image floor1Map = new Image("Faulkner1_Updated.png");
  public static Image floor2Map = new Image("Faulkner2_Updated.png");
  public static Image floor3Map = new Image("Faulkner3_Updated.png");
  public static Image floor4Map = new Image("Faulkner4_Updated.png");
  public static Image floor5Map = new Image("Faulkner5_Updated.png");

  private Graph graph;

  public NewNavPageController() {}

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    floorSelectionBtn.setItems(listOfFloors);
    resizableWindow();
    canvas.toFront();
  }

  /**
   * Creates a resizable GridPane with map image, menu buttons, etc.
   *
   * @return GridPane
   */
  public GridPane resizableWindow() {
    imageView.setPreserveRatio(true);
    imageView
        .fitHeightProperty()
        .bind(Opp.getPrimaryStage().getScene().heightProperty().subtract(vboxRef.heightProperty()));
    imageView.fitWidthProperty().bind(hboxRef.widthProperty());

    canvas.heightProperty().bind(imageView.fitHeightProperty());
    canvas.widthProperty().bind(imageView.fitWidthProperty());

    return gridPane;
  }

  public void editMode(ActionEvent actionEvent) {}

  public void goToMain(ActionEvent actionEvent) {
    try {
      BorderPane root = FXMLLoader.load(getClass().getResource("/Views/MainPage.fxml"));
      Opp.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void floorSelection(ActionEvent actionEvent) {
    String floorSelected = floorSelectionBtn.getValue();
    // System.out.println(floorSelected);

    switch (floorSelected) {
      case "Campus":
        imageView.setImage(campusMap);
        break;
      case "Floor 1":
        imageView.setImage(floor1Map);
        break;
      case "Floor 2":
        imageView.setImage(floor2Map);
        break;
      case "Floor 3":
        imageView.setImage(floor3Map);
        break;
      case "Floor 4":
        imageView.setImage(floor4Map);
        break;
      case "Floor 5":
        imageView.setImage(floor5Map);
        break;
    }
  }

  public void endLocSelection(ActionEvent actionEvent) {}

  public void doPathfind(ActionEvent actionEvent) {}

  public void goToSideMenu(MouseEvent mouseEvent) {}

  public void canvasClick(MouseEvent mouseEvent) {
    System.out.println("Click");
  }

  public void startLocSelection(ActionEvent actionEvent) {}
}
