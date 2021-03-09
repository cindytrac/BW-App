package edu.wpi.cs3733.teamO.Controllers.Mobile;

import static edu.wpi.cs3733.teamO.GraphSystem.Graph.GRAPH;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.teamO.Database.UserHandling;
import edu.wpi.cs3733.teamO.HelperClasses.Autocomplete;
import edu.wpi.cs3733.teamO.HelperClasses.SwitchScene;
import edu.wpi.cs3733.teamO.Model.Node;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class SaveParkingController implements Initializable {

  public Label currentSpot;
  public JFXTextField input;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    currentSpot.setText(UserHandling.getParkingSpot());

    // setting up auto complete for parking nodes
    ArrayList<String> nodeTypeNodes = Autocomplete.autoNodeData("nodeType");    // getting all nodes types
    ArrayList<String> parkingNodes = new ArrayList<>();

    for (Node n : GRAPH.getNodeByNodeType("PARK")) {
      parkingNodes.add(n.getLongName());
    }
    // actually autocompleting with the parking nodes (displays the long name)
    Autocomplete.autoComplete(parkingNodes, input);
  }

  /**
   * returns the previous map page based on booleans
   *
   * @param actionEvent
   */
  public void goBack(ActionEvent actionEvent) {
    SwitchScene.goToParentMobile("/Views/MobileApp/MobileHospitalNav.fxml", actionEvent);
  }

  // TODO make the textfield autocomplete things?
  public void saveSpot(ActionEvent actionEvent) {
    String spot = input.getText().substring(6, 8);
    UserHandling.setParkingSpot(spot);
    System.out.println(spot);
    currentSpot.setText(spot);
  }
}
