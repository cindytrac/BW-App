package edu.wpi.cs3733.teamO.Controllers.Revamped;

import static edu.wpi.cs3733.teamO.GraphSystem.Graph.*;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.cs3733.teamO.Database.DataHandling;
import edu.wpi.cs3733.teamO.Database.UserHandling;
import edu.wpi.cs3733.teamO.GraphSystem.Graph;
import edu.wpi.cs3733.teamO.HelperClasses.Autocomplete;
import edu.wpi.cs3733.teamO.HelperClasses.DrawHelper;
import edu.wpi.cs3733.teamO.HelperClasses.PopupMaker;
import edu.wpi.cs3733.teamO.HelperClasses.SwitchScene;
import edu.wpi.cs3733.teamO.Model.Node;
import edu.wpi.cs3733.teamO.Opp;
import edu.wpi.cs3733.teamO.UserTypes.Settings;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;

public class NavController implements Initializable {

  /**
   * NEED THE FOLLOWING TEXTFIELDS nodeID xCoord yCoord floor building nodeType longName shortName
   * startNodeID endNodeID
   *
   * <p>button for submitting node changes (add and edit changes)
   *
   * <p>a toggle for visible nodes *
   */
  @FXML private AnchorPane anchorPane;

  @FXML private VBox menuVBox;
  @FXML private JFXDrawer drawer;
  @FXML private JFXHamburger hamburger;
  @FXML private Canvas mapCanvas;
  @FXML private ImageView imageView;
  @FXML private JFXButton profileBtn;
  @FXML private JFXButton homeBtn;
  @FXML private JFXButton navBtn;
  @FXML private JFXButton trackBtn;
  @FXML private JFXButton reqBtn;
  @FXML private JFXButton patientsBtn;
  @FXML private JFXButton employeesBtn;
  @FXML private JFXButton loginBtn;
  public StackPane nodeWarningPane;

  @FXML private JFXNodesList editingList = new JFXNodesList();
  @FXML private JFXNodesList parking = new JFXNodesList();
  @FXML private JFXNodesList directionsList = new JFXNodesList();
  @FXML private JFXNodesList help = new JFXNodesList();
  @FXML private JFXNodesList floorsList = new JFXNodesList();
  private JFXNodesList algoList = new JFXNodesList();

  // creating icons for buttons
  Image helpIcon = new Image(getClass().getResourceAsStream("/Icons/informationIcon.png"));
  ImageView helpIconView = new ImageView(helpIcon);
  Image parkingIcon =
      new Image(getClass().getResourceAsStream("/Icons/navPageIcons/parkingBox.png"));
  ImageView parkingIconView = new ImageView(parkingIcon);
  Image editIcon = new Image(getClass().getResourceAsStream("/Icons/navPageIcons/edit.png"));
  ImageView editIconView = new ImageView(editIcon);
  Image edgesIcon = new Image(getClass().getResourceAsStream("/Icons/navPageIcons/edges.png"));
  ImageView edgesIconView = new ImageView(edgesIcon);
  Image saveIcon = new Image(getClass().getResourceAsStream("/Icons/navPageIcons/download.png"));
  ImageView saveIconView = new ImageView(saveIcon);
  Image uploadIcon = new Image(getClass().getResourceAsStream("/Icons/navPageIcons/upload.png"));
  ImageView uploadIconView = new ImageView(uploadIcon);
  Image algoIcon = new Image(getClass().getResourceAsStream("/Icons/navPageIcons/algo.png"));
  ImageView algoIconView = new ImageView(algoIcon);
  Image navIcon = new Image(getClass().getResourceAsStream("/Icons/navPageIcons/directions.png"));
  ImageView navIconView = new ImageView(navIcon);
  Image floorsIcon = new Image(getClass().getResourceAsStream("/Icons/navPageIcons/floors.png"));
  ImageView floorsIconView = new ImageView(floorsIcon);

  private final JFXButton helpB = new JFXButton(null, helpIconView);
  private final JFXButton parkingB = new JFXButton(null, parkingIconView);
  private final JFXButton editB = new JFXButton(null, editIconView);
  private final JFXButton showEdgesB = new JFXButton(null, edgesIconView);
  private final JFXButton saveB = new JFXButton(null, saveIconView);
  private final JFXButton uploadB = new JFXButton(null, uploadIconView);
  private final JFXButton algoB = new JFXButton(null, algoIconView);
  private final JFXButton navB = new JFXButton(null, navIconView);
  private final JFXButton floorSelectionB = new JFXButton("G", floorsIconView);
  private final JFXButton floorGB = new JFXButton("G", null);
  private final JFXButton floor1B = new JFXButton("1", null);
  private final JFXButton floor2B = new JFXButton("2", null);
  private final JFXButton floor3B = new JFXButton("3", null);
  private final JFXButton floor4B = new JFXButton("4", null);
  private final JFXButton floor5B = new JFXButton("5", null);
  private final JFXComboBox<String> algoStratBox = new JFXComboBox<String>();

  VBox directionsBox = new VBox();
  HBox directionButtons = new HBox();
  JFXTextField startLoc = new JFXTextField();
  JFXTextField endLoc = new JFXTextField();
  private final JFXButton submitPath = new JFXButton("GO");
  private final JFXButton clearPath = new JFXButton("Clear");

  private String helpPageUrl;

  private GraphicsContext gc;
  private double percImageView = 1.0;
  private Rectangle2D currentViewport;
  String sFloor = "G";
  private String sideMenuUrl;
  private String pathFloors = "";

  ArrayList<String> alignList = new ArrayList<>();

  Node startNode = null;
  Node endNode = null;
  public Node selectedNode = null;
  Node selectedNodeB = null;

  String strategy = Settings.getInstance().getAlgoChoice();
  ObservableList<String> listOfStrats =
      FXCollections.observableArrayList("A*", "Djikstra", "DFS", "BFS");

  // booleans:
  private boolean editing = false;

  private boolean displayingRoute = false;

  private void setNavFalse() {
    displayingRoute = false;
    startNode = null;
    endNode = null;
  }

  // editing bools:
  private boolean selectingEditNode = false;
  private boolean addNodeMode = false;
  private boolean addNodeDBMode = false;
  private boolean editingEdge = false;
  private boolean deletingEdge = false;
  public boolean addingNode = false;
  // private boolean addingEdgeN1 = false;
  // private boolean addingEdgeN2 = false;
  private boolean showingEdges = false;
  private boolean selectingAlign = false;

  private boolean isDrawerDirections = false;

  /** Directions + NodeEditing Fields */
  @FXML JFXTextField longName;

  DrawerController drawerController;
  @FXML JFXTextField nodeType;
  @FXML JFXTextField xCoord;
  @FXML JFXTextField yCoord;
  @FXML JFXCheckBox visible;
  public JFXDrawer drawerBottomRight;

  /** end of variable declaration */
  private void setEditFalse() {
    // selectingEditNode = false;
    addNodeMode = false;
    addNodeDBMode = false;
    editingEdge = false;
    deletingEdge = false;
    showingEdges = false;

    selectedNode = null;
    selectedNodeB = null;
  }

  public AnchorPane getAnchorPane() {
    return anchorPane;
  }

  // creating context menu for add/edit/delete functions
  ContextMenu editMapContext = new ContextMenu();
  // create menu items
  MenuItem editNodeMenu = new MenuItem("Edit Node");
  MenuItem deleteNodeMenu = new MenuItem("Delete Node");
  MenuItem editingEdgeMenu = new MenuItem("Add/Delete Edge");
  MenuItem addNodeMenu = new MenuItem("Add Node");
  MenuItem alignHorizontally = new MenuItem("Align Nodes Horizontally");
  MenuItem alignVertically = new MenuItem("Align Nodes Vertically");

  // patient context menu for clearing
  ContextMenu pathfindContext = new ContextMenu();
  // create menu items
  MenuItem setStart = new MenuItem("Set as Start");
  MenuItem setEnd = new MenuItem("Set as Destination");
  MenuItem clearPathMenu = new MenuItem("Clear Path");

  /**
   * Create buttons for: directions editing help floors
   *
   * <p>make hamburger
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // Add Buttons to their respective list
    /** Add to the editing dropdown * */
    editingList.addAnimatedNode(editB);
    editingList.addAnimatedNode(showEdgesB);
    editingList.addAnimatedNode(saveB);
    editingList.addAnimatedNode(uploadB);
    editingList.addAnimatedNode(algoList);

    algoList.addAnimatedNode(algoB);
    algoStratBox.setItems(listOfStrats);
    algoList.addAnimatedNode(algoStratBox);

    // autocompletes start and end textfields
    ArrayList<String> longNameNodes = Autocomplete.autoNodeData("longName");
    Autocomplete.autoComplete(longNameNodes, startLoc);
    Autocomplete.autoComplete(longNameNodes, endLoc);

    /** Add to the floor selection* */
    floorsList.addAnimatedNode(floorSelectionB);
    floorsList.addAnimatedNode(floorGB);
    floorsList.addAnimatedNode(floor1B);
    floorsList.addAnimatedNode(floor2B);
    floorsList.addAnimatedNode(floor3B);
    floorsList.addAnimatedNode(floor4B);
    floorsList.addAnimatedNode(floor5B);

    /** hELP?* */
    help.addAnimatedNode(helpB);
    // parking
    parking.addAnimatedNode(parkingB);

    /** Navigation Button* */
    directionsList.addAnimatedNode(navB);

    // add stuff to vbox TODO(make horizontal)
    algoList.setRotate(90);
    directionsList.setRotate(0);
    floorsList.setRotate(270);

    directionsBox.getChildren().addAll(startLoc, endLoc);
    directionButtons.getChildren().addAll(clearPath, submitPath);
    directionsBox.getChildren().add(directionButtons);

    directionsList.addAnimatedNode(directionsBox);
    // TODO bind to flowpane

    // editing
    // TODO: add algo strat box
    //    algoStratCBox.setItems(listOfStrats);

    // mapCanvas.toFront();
    gc = mapCanvas.getGraphicsContext2D();

    imageView.setImage(campusMap);
    currentViewport = new Rectangle2D(0, 0, campusMap.getWidth(), campusMap.getHeight());
    imageView.setViewport(currentViewport);
    //    resizableWindow();
    // centers expanded image of ground level
    imageView.setTranslateX(0);
    imageView.setTranslateY(-250);

    GRAPH.setGraphicsContext(gc);

    editB.setVisible(false);

    if (UserHandling.getEmployee()) {
      System.out.println("EMPLOYEE");
      sideMenuUrl = "/Views/SideMenuStaff.fxml";
      helpPageUrl = "/RevampedViews/DesktopApp/NavPatientHelp.fxml";
      if (UserHandling.getAdmin()) {
        sideMenuUrl = "/Views/SideMenuAdmin.fxml";
        helpPageUrl = "/RevampedViews/DesktopApp/NavStaffHelp.fxml";
        System.out.println("ADMIN");
        editB.setVisible(true);
      }
    } else {
      sideMenuUrl = "/Views/SideMenu.fxml";
      helpPageUrl = "/RevampedViews/DesktopApp/NavPatientHelp.fxml";
    }

    try {
      VBox vbox =
          FXMLLoader.load(getClass().getResource("/RevampedViews/DesktopApp/NewSideMenu.fxml"));
      drawer.setSidePane(vbox);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // transition animation of Hamburger icon
    HamburgerBackArrowBasicTransition burgerTransition =
        new HamburgerBackArrowBasicTransition(hamburger);
    burgerTransition.setRate(-1);

    // click event - mouse click
    hamburger.addEventHandler(
        MouseEvent.MOUSE_PRESSED,
        (e) -> {
          burgerTransition.setRate(burgerTransition.getRate() * -1);
          burgerTransition.play();

          if (drawer.isOpened()) {
            drawer.close(); // this will close slide pane
            floorsList.setTranslateX(0);
            directionsList.setTranslateX(0);

          } else {
            drawer.open(); // this will open slide pane
            floorsList.setTranslateX(280);
            directionsList.setTranslateX(280);
          }
        });

    // transition animation of Hamburger icon
    HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
    transition.setRate(-1);

    resizeCanvas();
    // draws appropriately accordingly to combination of booleans
    draw(1);

    System.out.println("NavController Initialized");

    // set onaction events for all buttons
    generalButtons();
    setStyles();

    // add menu items to context menu
    editMapContext.getItems().add(editNodeMenu);
    editMapContext.getItems().add(deleteNodeMenu);
    editMapContext.getItems().add(editingEdgeMenu);
    editMapContext.getItems().add(addNodeMenu);

    pathfindContext.getItems().add(setStart);
    pathfindContext.getItems().add(setEnd);
    pathfindContext.getItems().add(clearPathMenu);

    /** Initialize the drawer @sadie */
    switchDrawer();

    //   drawerBottomRight.close();
  }

  public void switchDrawer() {
    if (isDrawerDirections) {
      String fuck = "/RevampedViews/DesktopApp/NodeEditing.fxml";
      try {
        // create connection between controllers, in order to display the drawer directions
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fuck));
        VBox drawerBox = fxmlLoader.load();
        drawerController = fxmlLoader.getController();
        drawerBottomRight.setSidePane(drawerBox);

      } catch (IOException e) {
        e.printStackTrace();
      }
      drawerBottomRight.open();
    } else {
      String fuck = "/RevampedViews/DesktopApp/DirectionsDisplay.fxml";
      try {
        // create connection between controllers, in order to display the drawer directions
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fuck));
        VBox drawerBox = fxmlLoader.load();
        drawerController = fxmlLoader.getController();
        drawerBottomRight.setSidePane(drawerBox);

      } catch (IOException e) {
        e.printStackTrace();
      }
      drawerBottomRight.close();
    }

    drawerController.setNavController(this);
    isDrawerDirections = !isDrawerDirections;
  }

  public void setStyles() {

    helpIconView.setFitWidth(35);
    helpIconView.setFitHeight(35);
    parkingIconView.setFitWidth(35);
    parkingIconView.setFitHeight(35);
    editIconView.setFitWidth(35);
    editIconView.setFitHeight(35);
    edgesIconView.setFitWidth(35);
    edgesIconView.setFitHeight(35);
    saveIconView.setFitWidth(35);
    saveIconView.setFitHeight(35);
    uploadIconView.setFitWidth(35);
    uploadIconView.setFitHeight(35);
    algoIconView.setFitWidth(35);
    algoIconView.setFitHeight(35);
    navIconView.setFitWidth(35);
    navIconView.setFitHeight(35);
    floorsIconView.setFitWidth(20);
    floorsIconView.setFitHeight(20);

    editingList.setSpacing(10);
    help.setSpacing(10);
    parking.setSpacing(10);
    directionsList.setSpacing(10);
    floorsList.setSpacing(10);
    algoList.setSpacing(60);

    drawer.toFront();
    menuVBox.toFront();
    editingList.toFront();
    help.toFront();
    parking.toFront();
    directionsList.toFront();
    floorsList.toFront();
    algoList.toFront();

    algoStratBox.setPadding(new Insets(5, 10, 5, 10));
    algoStratBox.getStyleClass().addAll("combo-box");
    menuVBox.getHeight();

    directionsList.setAlignment(Pos.TOP_LEFT);
    directionButtons.setAlignment(Pos.CENTER);
    directionsBox.setPadding(new Insets(20, 20, 20, 20));
    directionsBox.getStyleClass().addAll("directions-box");
    startLoc.setPromptText("Starting from...");
    endLoc.setPromptText("Navigate to...");
    startLoc.setPrefSize(300, 50);
    endLoc.setPrefSize(300, 50);
    submitPath.setPrefSize(100, 50);
    clearPath.setPrefSize(100, 50);

    helpB.getStyleClass().addAll("buttons");
    helpB.setButtonType(JFXButton.ButtonType.RAISED);
    parkingB.getStyleClass().addAll("buttons");
    parkingB.setButtonType(JFXButton.ButtonType.RAISED);
    editB.getStyleClass().addAll("buttons");
    editB.setButtonType(JFXButton.ButtonType.RAISED);
    showEdgesB.getStyleClass().addAll("buttons");
    showEdgesB.setButtonType(JFXButton.ButtonType.RAISED);
    saveB.getStyleClass().addAll("buttons");
    saveB.setButtonType(JFXButton.ButtonType.RAISED);
    uploadB.getStyleClass().addAll("buttons");
    uploadB.setButtonType(JFXButton.ButtonType.RAISED);
    algoB.getStyleClass().addAll("buttons");
    algoB.setButtonType(JFXButton.ButtonType.RAISED);
    navB.getStyleClass().addAll("buttons");
    navB.setButtonType(JFXButton.ButtonType.RAISED);
    floorSelectionB.getStyleClass().addAll("buttons");
    floorSelectionB.setButtonType(JFXButton.ButtonType.RAISED);
    floorGB.getStyleClass().addAll("buttons");
    floorGB.setButtonType(JFXButton.ButtonType.RAISED);
    floor1B.getStyleClass().addAll("buttons");
    floor1B.setButtonType(JFXButton.ButtonType.RAISED);
    floor2B.getStyleClass().addAll("buttons");
    floor2B.setButtonType(JFXButton.ButtonType.RAISED);
    floor3B.getStyleClass().addAll("buttons");
    floor3B.setButtonType(JFXButton.ButtonType.RAISED);
    floor4B.getStyleClass().addAll("buttons");
    floor4B.setButtonType(JFXButton.ButtonType.RAISED);
    floor5B.getStyleClass().addAll("buttons");
    floor5B.setButtonType(JFXButton.ButtonType.RAISED);
  }

  public void generalButtons() {

    parkingB.setOnAction(
        e -> {
          // change the first location field to the saved parking spot
        });

    helpB.setOnAction(
        e -> {
          // show tutorial/help
          SwitchScene.goToParent(helpPageUrl);
        });

    submitPath.setOnAction(
        e -> {
          // send path to do pathfinding actions
          doPathfind();
        });

    clearPath.setOnAction(
        e -> {
          // clear start and end locations
          drawerController.removeDirectionChildren();
          drawerBottomRight.close();
          clearSelection();
        });

    /** Floor onactions* */
    floorGB.setOnAction(
        e -> {
          setFloor("G");
          draw();
        });
    floor1B.setOnAction(
        e -> {
          setFloor("1");
          draw();
        });
    floor2B.setOnAction(
        e -> {
          setFloor("2");
          draw();
        });
    floor3B.setOnAction(
        e -> {
          setFloor("3");
          draw();
        });
    floor4B.setOnAction(
        e -> {
          setFloor("4");
          draw();
        });
    floor5B.setOnAction(
        e -> {
          setFloor("5");
          draw();
        });

    /** Edit onactions* */
    editB.setOnAction(
        e -> {
          // toggle edit mode
          editing = !editing;
          if (editing) {
            editMode();
          } else {
            setEditFalse();
          }
          resetFloorButtons();
          switchDrawer();
          draw();
        });
    uploadB.setOnAction(
        e -> {
          DataHandling.importExcelData();
        });
    saveB.setOnAction(
        e -> {
          DataHandling.save();
        });
    showEdgesB.setOnAction(
        e -> {
          // show all edges
          showingEdges = !showingEdges;
          draw();
        });

    algoStratBox.setOnAction(
        e -> {
          strategy = (String) algoStratBox.getValue();
          Settings.getInstance().setAlgoChoice(strategy);
        });
  }

  /**
   * Creates a resizable GridPane with map image, menu buttons, etc.
   *
   * @return GridPane
   */
  public AnchorPane resizableWindow() {
    imageView.setPreserveRatio(true);
    // imageView.fitWidthProperty().bind(Opp.getPrimaryStage().getScene().widthProperty());
    //     resizeCanvas();

    return anchorPane;
    // 350 / 1920
  }

  /** Resizes Canvas to be the current size of the Image */
  public void resizeCanvas() {
    mapCanvas.heightProperty().setValue(1000);
    mapCanvas.widthProperty().setValue(1000);

    mapCanvas.heightProperty().setValue(imageView.getBoundsInParent().getHeight());
    mapCanvas.widthProperty().setValue(imageView.getBoundsInParent().getWidth());
  }

  /** switches between editing and pathfinding for admin users */
  public void editMode() {
    if (!GRAPH.allConnected()) {
      editing = true;

      System.out.println("Incomplete map.");
      //      PopupMaker.unconnectedPopup(nodeWarningPane);
      return;
    }

    if (editing) {
      setNavFalse();
      selectingEditNode = true;
    } else {
      setEditFalse();
    }
    draw();
  }

  /**
   * fills in a nodes info when it is clicked
   *
   * @param clickedNode
   */
  public void autocompleteEditMap(Node clickedNode) {
    // xCoord.setText(Integer.toString(clickedNode.getXCoord()));
    // yCoord.setText(Integer.toString(clickedNode.getYCoord()));
    // nodeType.setText(clickedNode.getNodeType());
    // longName.setText(clickedNode.getLongName());
    // visible.setSelected(clickedNode.isVisible());
  }

  /** resets path and creates a new path depending on start and end nodes */
  public void doPathfind() {
    if (startNode != null && endNode != null) {
      GRAPH.resetPath();
      GRAPH.findPath(Settings.getInstance().getAlgoChoice(), startNode, endNode);
      displayingRoute = true;
    } else if (startLoc.getText() != null && endLoc.getText() != null) {
      startNode = GRAPH.getNodeByLongName(startLoc.getText());
      endNode = GRAPH.getNodeByLongName(endLoc.getText());
      GRAPH.resetPath();
      GRAPH.findPath(Settings.getInstance().getAlgoChoice(), startNode, endNode);
      displayingRoute = true;
    } else {
      // TODO: add stackpane for all warnings
      //      PopupMaker.invalidPathfind(nodeWarningPane);
    }

    setFloor(startNode.getFloor());
    draw();
    pathFloors = "";
    for (Node n : GRAPH.getPath()) {
      if (!pathFloors.contains(n.getFloor())) {
        pathFloors += n.getFloor();
      }
    }
    disableFloorButtons();

    /** DIRECTIONS @sadie */

    // for each direction, add to the directions box.
    drawerController.addDirectionChildren(Graph.findTextDirection());

    // open the directions box at the bottom right
    drawerBottomRight.open();
  }

  /**
   * determines closest node to mouse click on canvas, used for both navigating and editing the map
   *
   * @param mouseEvent
   */
  public void canvasClick(MouseEvent mouseEvent) {
    // close the directions drawer
    Node clickedNode =
        GRAPH.closestNode(sFloor, mouseEvent.getX(), mouseEvent.getY(), editing, imageView);

    // block for SHIFT CLICK --> aligning nodes
    if (editing && mouseEvent.isShiftDown() && mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
      // hide context menu
      editMapContext.setAutoHide(true);
      if (editMapContext.isShowing()) {
        editMapContext.hide();
      }
      pathfindContext.setAutoHide(true);
      if (pathfindContext.isShowing()) {
        pathfindContext.hide();
      }

      if ((selectingEditNode && selectedNode == null)) {
        selectedNode = clickedNode;
        drawerBottomRight.open();
        editNodeMenuSelect(selectedNode);

      } else if (selectingEditNode && selectedNode != null && selectedNode != clickedNode) {
        alignList.add(clickedNode.getID());
        if (!editMapContext.getItems().contains(alignHorizontally)) {
          editMapContext.getItems().add(alignHorizontally);
          editMapContext.getItems().add(alignVertically);
        }
      }
      selectedNodeB = null;
    }
    // ----------------------
    // block for LEFT CLICK --> regular clicking
    else if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
      // hide context menu
      editMapContext.setAutoHide(true);
      if (editMapContext.isShowing()) {
        editMapContext.hide();
      }
      pathfindContext.setAutoHide(true);
      if (pathfindContext.isShowing()) {
        pathfindContext.hide();
      }

      clearAlignList();

      boolean temp = showingEdges;
      setEditFalse();
      showingEdges = temp;
      if (editing) {
        drawerBottomRight.close();
      }
    }
    // ----------------------
    // block for RIGHT CLICK
    else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {

      System.out.println("You right clicked!");

      if (editing) { // editing mode
        editMapContext.setAutoHide(true);
        if (editMapContext.isShowing()) {
          editMapContext.hide();
        }
        mapCanvas.setOnContextMenuRequested(
            f -> {
              editMapContext.show(mapCanvas, f.getScreenX(), f.getScreenY());
            });

        // if align list is empty, do normal stuff
        if (alignList.size() == 0) {
          // if not adding edge, do normal stuff
          if (!editingEdge) {
            selectedNode = clickedNode;
            drawerBottomRight.open();
            editNodeMenuSelect(selectedNode);
            contextMenuOnActions(selectedNode, mouseEvent);
          }
          // if adding edge, keep first node
          else {
            selectedNodeB = clickedNode;
            contextMenuOnActions(selectedNodeB, mouseEvent);
          }
        }
        // otherwise, option
        else {
          contextMenuOnActions(selectedNode, mouseEvent);
        }
      }
      // pathfinding mode
      else {
        pathfindContext.setAutoHide(true);
        if (pathfindContext.isShowing()) {
          pathfindContext.hide();
        }
        mapCanvas.setOnContextMenuRequested(
            f -> {
              pathfindContext.show(mapCanvas, f.getScreenX(), f.getScreenY());
            });

        contextMenuOnActions(clickedNode, mouseEvent);
      }

    } else if (true) {
      // TODO add dragging functionality
    }

    // autocompleteEditMap(clickedNode);
    draw();
  }

  public void contextMenuOnActions(Node node, MouseEvent mouseEvent) {
    // TODO: add functionality to these
    editNodeMenu.setOnAction(
        action -> {
          System.out.println("editing node");
          // editNodeMenuSelect(node);
          editingEdge = false;
        });

    deleteNodeMenu.setOnAction(
        action -> {
          // deleting node
          try {
            editingEdge = false;
            deleteNode(node);
            draw();
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });

    editingEdgeMenu.setOnAction(
        action -> {
          System.out.println("adding edge");
          if (!editingEdge) {
            editingEdge = true;
          }
          // if adding edge already
          else {
            String id1 = selectedNode.getID();
            String id2 = selectedNodeB.getID();
            // if edge doesn't exist, add
            if (!GRAPH.edgeAlreadyExists(id1, id2)) {
              try {
                GRAPH.addEdge(id1, id2);
                // selectedNode = selectedNodeB;
                selectedNodeB = null;
              } catch (SQLException throwables) {
                PopupMaker.edgeAlreadyExists(nodeWarningPane);
              }
            }
            // else, delete
            else {
              try {
                GRAPH.deleteEdge(id1, id2);
                // selectedNode = selectedNodeB;
                selectedNodeB = null;
              } catch (SQLException throwables) {
                PopupMaker.edgeDoesntExists(nodeWarningPane);
              }
            }

            draw();
          }
        });

    addNodeMenu.setOnAction(
        action -> {
          selectedNode = null;
          draw();
          selectedNode = getRealXY(mouseEvent);
          // TODO: isMobile t/f?
          DrawHelper.drawSingleNode(gc, selectedNode, Color.BLUE, imageView, false);

          drawerBottomRight.open();
          editNodeMenuSelect(selectedNode);

          addingNode = true;
        });

    alignHorizontally.setOnAction(
        action -> {
          alignHorizontally();
        });

    addNodeMenu.setOnAction(
        action -> {
          selectedNode = null;
          draw();
          selectedNode = getRealXY(mouseEvent);
          // TODO: isMobile t/f?
          DrawHelper.drawSingleNode(gc, selectedNode, Color.BLUE, imageView, false);

          drawerBottomRight.open();
          editNodeMenuSelect(selectedNode);

          addingNode = true;
        });

    alignHorizontally.setOnAction(
        action -> {
          alignHorizontally();
        });

    alignVertically.setOnAction(
        action -> {
          alignVertically();
        });

    // NAVIGATING:

    clearPathMenu.setOnAction(
        action -> {
          editingEdge = false;
          clearSelection();
          draw();
        });

    setStart.setOnAction(
        action -> {
          startNode = node;
          startLoc.setText(startNode.getLongName());
          if (startNode != null && endNode != null) {
            doPathfind();
          }
          draw();
        });

    setEnd.setOnAction(
        action -> {
          endNode = node;
          endLoc.setText(endNode.getLongName());
          if (startNode != null && endNode != null) {
            doPathfind();
          }
          draw();
        });
  }

  private void clearAlignList() {
    alignList = new ArrayList<>();
    if (editMapContext.getItems().contains(alignHorizontally)) {
      editMapContext.getItems().remove(alignHorizontally);
      editMapContext.getItems().remove(alignVertically);
    }
  }

  private void editNodeMenuSelect(Node selectedNode) {
    // nodeID.setText(selectedNode.getID());
    drawerController.xCoord.setText(Integer.toString(selectedNode.getXCoord()));
    drawerController.yCoord.setText(Integer.toString(selectedNode.getYCoord()));
    // floor.setText(selectedNode.getFloor());
    drawerController.building.setText(selectedNode.getBuilding());
    drawerController.nodeType.setText(selectedNode.getNodeType());
    drawerController.longName.setText(selectedNode.getLongName());
    drawerController.shortName.setText(selectedNode.getShortName());
    drawerController.visible.setSelected(selectedNode.isVisible());
  }

  /**
   * sets the mode for adding a new node
   *
   * @param actionEvent
   */
  private void addNode(ActionEvent actionEvent) {
    addNodeMode = true;
    addNodeDBMode = true;
    selectingEditNode = false;
    editingEdge = false;
  }

  /** will delete the node that is currently selected */
  private void deleteNode(Node selectedNode) throws SQLException {
    GRAPH.deleteNode(selectedNode.getID());
    selectedNode = null;
    draw();
  }

  /**
   * Drag the node to change its coords
   *
   * @param closest, the node you want to drag
   * @param xEvent, the x property of mouse event and y event VV
   * @param yEvent
   */
  public void nodeDrag(Node closest, int xEvent, int yEvent) {
    Node draggedNode = closest;

    int x = (int) (getImgX(xEvent));
    int y = (int) (getImgY(yEvent));

    draggedNode.setXCoord(x);
    draggedNode.setYCoord(y);
    draw();
  }

  /**
   * gets the xy coordinates of the mouse and scales it to the image
   *
   * @param mouseEvent
   * @return
   */
  private Node getRealXY(MouseEvent mouseEvent) {
    Node n = new Node();
    n.setXCoord((int) getImgX(mouseEvent.getX()));
    n.setYCoord((int) getImgY(mouseEvent.getY()));
    return n;
  }

  public void onCanvasScroll(ScrollEvent scrollEvent) {
    double scrollDeltaY = scrollEvent.getDeltaY();
    // if scroll is at least a certain amount, then zoom (idk, maybe change this??)
    if (Math.abs(scrollDeltaY) > 10) {
      // if positive, then scrolling up (zooming in)
      if (scrollDeltaY > 0) {
        if (percImageView <= 0.4) {
          return;
        } else {
          percImageView -= 0.05;
        }

      }
      // else, scrolling down (zooming out)
      else {
        if (percImageView >= 1.0) {
          return;
        } else {
          percImageView += 0.05;
        }
      }
    }

    double a = getImgX(scrollEvent.getX());
    double b = getImgY(scrollEvent.getY());
    double vX = percImageView * imageView.getImage().getWidth();
    double vY = percImageView * imageView.getImage().getHeight();
    // zoom option A:
    currentViewport =
        new Rectangle2D(
            (a * (1 - percImageView) + imageView.getImage().getWidth() * 0.5 * percImageView)
                - vX / 2,
            (b * (1 - percImageView) + imageView.getImage().getHeight() * 0.5 * percImageView)
                - vY / 2,
            vX,
            vY);

    imageView.setViewport(currentViewport);
    draw();
  }

  public double getImgX(double canvasX) {
    double percCanvasX = canvasX / mapCanvas.getWidth();
    return (currentViewport.getMinX() + (percCanvasX * currentViewport.getWidth()));
  }

  public double getImgY(double canvasY) {
    double percCanvasY = canvasY / mapCanvas.getHeight();
    return (currentViewport.getMinY() + ((percCanvasY * currentViewport.getHeight())));
  }

  // TODO: all sharing function!

  public void share() {
    System.out.println("The share button was pressed my guy");
  }

  public void clearSelection() {
    startNode = null;
    endNode = null;
    startLoc.clear();
    endLoc.clear();
    setNavFalse();

    GRAPH.resetPath();
    pathFloors = "";
    resetFloorButtons();

    resizeCanvas();
    draw();
    //    directionvbox.getChildren().clear();
  }

  /** disables and hides floor buttons for floors that aren't in the current path */
  public void disableFloorButtons() {
    if (!pathFloors.contains("G")) {
      floorGB.setDisable(true);
      floorGB.setVisible(false);
    }
    if (!pathFloors.contains("1")) {
      floor1B.setDisable(true);
      floor1B.setVisible(false);
    }
    if (!pathFloors.contains("2")) {
      floor2B.setDisable(true);
      floor2B.setVisible(false);
    }
    if (!pathFloors.contains("3")) {
      floor3B.setDisable(true);
      floor3B.setVisible(false);
    }
    if (!pathFloors.contains("4")) {
      floor4B.setDisable(true);
      floor4B.setVisible(false);
    }
    if (!pathFloors.contains("5")) {
      floor5B.setDisable(true);
      floor5B.setVisible(false);
    }
  }

  /** resets (enables and un-hides) all floor buttons */
  public void resetFloorButtons() {
    floorGB.setDisable(false);
    floorGB.setVisible(true);

    floor1B.setDisable(false);
    floor1B.setVisible(true);

    floor2B.setDisable(false);
    floor2B.setVisible(true);

    floor3B.setDisable(false);
    floor3B.setVisible(true);

    floor4B.setDisable(false);
    floor4B.setVisible(true);

    floor5B.setDisable(false);
    floor5B.setVisible(true);
  }

  /** can draw the path, nodes, and edges based on booleans */
  void draw() {
    resizeCanvas();

    Group group = new Group(anchorPane);
    Stage stage = Opp.getPrimaryStage();
    stage.getScene().setRoot(group);
    stage.show();

    resizeCanvas();
    gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

    // i know these can be simplified but i don't care -- this is more organized imo

    if (!editing && !displayingRoute) {
      // draw the visible Node (navigating) on sFloor + highlight start and end (if selected)
      // GRAPH.drawVisibleNodes(sFloor, startNode, endNode, imageView, false);
      if (startNode != null) {
        DrawHelper.drawSingleNode(gc, startNode, Color.BLUE, imageView, false);
      }
      if (endNode != null) {
        DrawHelper.drawSingleNode(gc, endNode, Color.RED, imageView, false);
      }
    } else if (!editing && displayingRoute) {
      directionsList.animateList(true);

      // draw the portion on sFloor + highlight start and end
      Polyline path = GRAPH.drawCurrentPath(sFloor, startNode, endNode, imageView, false);

      group = new Group(anchorPane, path);
      stage = Opp.getPrimaryStage();
      stage.getScene().setRoot(group);
      stage.show();

    } else if (editing) {
      // draw ALL the nodes (editing) + highlight selected node (if selected)
      GRAPH.drawAllNodes(sFloor, selectedNode, selectedNodeB, selectingEditNode, imageView, false);
      // and if "show edges" is selected, draw them as well
      if (showingEdges) {
        GRAPH.drawAllEdges(sFloor, selectedNode, selectedNodeB, imageView, false);
      }
    }

    if (!alignList.isEmpty()) {
      for (String s : alignList) {
        Node n = GRAPH.getNodeByID(s);
        DrawHelper.drawSingleNode(gc, n, Color.BLUE, imageView, false);
      }
    }
  }

  // ignore this, BUT DON'T DELETE IT!!!!! ...i have my reasons
  private void draw(int i) {
    // i know these can be simplified but i don't care -- this is more organized
    if (!editing && !displayingRoute) {
      // GRAPH.drawVisibleNodes(sFloor, startNode, endNode, imageView, false);
      if (startNode != null) {
        DrawHelper.drawSingleNode(gc, startNode, Color.BLUE, imageView, false);
      }
      if (endNode != null) {
        DrawHelper.drawSingleNode(gc, endNode, Color.RED, imageView, false);
      }
    } else if (!editing && displayingRoute) {
      GRAPH.drawCurrentPath(sFloor, startNode, endNode, imageView, false);
    } else if (editing) {
      GRAPH.drawAllNodes(sFloor, selectedNode, selectedNodeB, selectingEditNode, imageView, false);
      if (showingEdges) {
        GRAPH.drawAllEdges(sFloor, selectedNode, selectedNodeB, imageView, false);
      }
    }
  }

  //  private void addTextToDirectionBox(String text) {
  //
  //    Text newText = new Text(text + "\n");
  //    newText.setFont(Font.font("leelawadee ui", 16.0));
  //    directionvbox.getChildren().add(newText);
  //  }

  public void alignVertically() {
    for (String s : alignList) {
      Node n = GRAPH.getNodeByID(s);
      n.setYCoord(selectedNode.getYCoord());
    }

    clearAlignList();
    draw();
  }

  public void alignHorizontally() {
    for (String s : alignList) {
      Node n = GRAPH.getNodeByID(s);
      n.setXCoord(selectedNode.getXCoord());
    }

    clearAlignList();
    draw();
  }

  public void setFloor(String floor) {
    if (sFloor.equals(floor)) {
      return;
    }
    switch (floor) {
      case "G":
        imageView.setImage(campusMap);
        break;
      case "1":
        imageView.setImage(floor1Map);
        break;
      case "2":
        imageView.setImage(floor2Map);
        break;
      case "3":
        imageView.setImage(floor3Map);
        break;
      case "4":
        imageView.setImage(floor4Map);
        break;
      case "5":
        imageView.setImage(floor5Map);
        break;
    }

    setMapViewDraw(floor);
  }

  public void setMapViewDraw(String floorSelected) {
    if (sFloor.equals(floorSelected)) {
      return;
    }
    if (floorSelected.equals("G")) {
      imageView.setTranslateX(0);
      imageView.setTranslateY(-250);
    } else {
      imageView.setTranslateX(0);
      imageView.setTranslateY(-50);
    }

    sFloor = floorSelected;
    floorSelectionB.setText(floorSelected);

    currentViewport =
        new Rectangle2D(0, 0, imageView.getImage().getWidth(), imageView.getImage().getHeight());
    imageView.setViewport(currentViewport);
    percImageView = 1.0;

    resizeCanvas();
    draw();
  }

  // TODO better dragging functionality @sadie
  public void nodeDragEnter(MouseDragEvent mouseDragEvent) {}

  public void nodeDragRelease(MouseDragEvent mouseDragEvent) {}

  public void toProfile(ActionEvent actionEvent) {
    SwitchScene.goToParent("/RevampedViews/DesktopApp/ProfilePage.fxml");
  }

  public void toHome(ActionEvent actionEvent) {
    SwitchScene.goToParent("/RevampedViews/DesktopApp/MainStaffScreen.fxml");
  }

  public void toNav(ActionEvent actionEvent) {
    SwitchScene.goToParent("/RevampedViews/DesktopApp/Navigation.fxml");
  }

  public void toTrack(ActionEvent actionEvent) {
    SwitchScene.goToParent("/RevampedViews/DesktopApp/MainPatientScreen.fxml");
  }

  public void toReq(ActionEvent actionEvent) {
    SwitchScene.goToParent("/RevampedViews/DesktopApp/MainStaffScreen.fxml");
  }

  public void toPatients(ActionEvent actionEvent) {
    SwitchScene.goToParent("/RevampedViews/DesktopApp/MainStaffScreen.fxml");
  }

  public void toEmployees(ActionEvent actionEvent) {
    SwitchScene.goToParent("/RevampedViews/DesktopApp/MainStaffScreen.fxml");
  }

  public void toLogin(ActionEvent actionEvent) {
    SwitchScene.goToParent("/RevampedViews/DesktopApp/SignInPage.fxml");
  }
}
