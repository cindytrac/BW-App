package edu.wpi.cs3733.teamO.Controllers.Revamped;

import static edu.wpi.cs3733.teamO.GraphSystem.Graph.*;
import static edu.wpi.cs3733.teamO.GraphSystem.Graph.floor5Map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.teamO.Database.UserHandling;
import edu.wpi.cs3733.teamO.GraphSystem.Graph;
import edu.wpi.cs3733.teamO.HelperClasses.DrawHelper;
import edu.wpi.cs3733.teamO.HelperClasses.PopupMaker;
import edu.wpi.cs3733.teamO.Model.Node;
import edu.wpi.cs3733.teamO.Opp;
import edu.wpi.cs3733.teamO.UserTypes.Settings;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class NavController implements Initializable {

  public AnchorPane anchorPane;
  public Canvas mapCanvas;
  public FlowPane hamburger;
  public ImageView imageView;
  @FXML private JFXNodesList parking;
  @FXML private FlowPane bottomRightInfo;
  @FXML private JFXNodesList directionsList;
  @FXML private JFXNodesList editingList;
  @FXML private JFXNodesList help;
  @FXML private JFXNodesList floorsList;
  private final JFXButton helpB = new JFXButton("H", null);
  private final JFXButton parkingB = new JFXButton("P", null);
  private final JFXButton editB = new JFXButton("E", null);
  private final JFXButton showEdgesB = new JFXButton("sho", null);
  private final JFXButton saveB = new JFXButton("S", null);
  private final JFXButton uploadB = new JFXButton("U", null);
  private final JFXButton navB = new JFXButton("N", null);
  private final JFXButton floorSelectionB = new JFXButton("F", null);
  private final JFXButton floorGB = new JFXButton("G", null);
  private final JFXButton floor1B = new JFXButton("1", null);
  private final JFXButton floor2B = new JFXButton("2", null);
  private final JFXButton floor3B = new JFXButton("3", null);
  private final JFXButton floor4B = new JFXButton("4", null);
  private final JFXButton floor5B = new JFXButton("5", null);
  VBox directionsBox = new VBox();
  HBox directionButtons = new HBox();
  JFXTextField startLoc = new JFXTextField();
  JFXTextField endLoc = new JFXTextField();
  private final JFXButton submitPath = new JFXButton("GO");
  private final JFXButton clearPath = new JFXButton("Clear");

  private GraphicsContext gc;
  private double percImageView = 1.0;
  private Rectangle2D currentViewport;
  private String sFloor = "G";
  private String sideMenuUrl;
  private String pathFloors = "";

  ArrayList<String> alignList = new ArrayList<>();

  Node startNode = null;
  Node endNode = null;
  Node selectedNode = null;
  Node selectedNodeB = null;

  String strategy = Settings.getInstance().getAlgoChoice();
  ObservableList<String> listOfStrats =
      FXCollections.observableArrayList("A*", "Djikstra", "DFS", "BFS");

  // booleans:
  private boolean editing = false;

  // navigating bools:
  private boolean selectingStart = false;
  private boolean selectingEnd = false;
  private boolean displayingRoute = false;

  private void setNavFalse() {
    selectingStart = false;
    selectingEnd = false;
    displayingRoute = false;
    startNode = null;
    endNode = null;
  }

  // editing bools:
  private boolean selectingEditNode = false;
  private boolean addNodeMode = false;
  private boolean addNodeDBMode = false;
  private boolean addingEdgeBD = false;
  // private boolean addingEdgeN1 = false;
  // private boolean addingEdgeN2 = false;
  private boolean showingEdges = false;
  private boolean selectingAlign = false;

  private void setEditFalse() {
    selectingEditNode = false;
    addNodeMode = false;
    addNodeDBMode = false;
    addingEdgeBD = false;
    showingEdges = false;
    selectedNode = null;
  }

  public AnchorPane getAnchorPane() {
    return anchorPane;
  }

  // creating context menu for add/edit/delete functions
  ContextMenu contextMenu = new ContextMenu();
  // create menu items
  MenuItem editNodeMenu = new MenuItem("edit");
  MenuItem deleteNodeMenu = new MenuItem("delete");
  MenuItem addEdgeMenu = new MenuItem("add edge");

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

    mapCanvas.toFront();
    gc = mapCanvas.getGraphicsContext2D();

    imageView.setImage(campusMap);
    currentViewport = new Rectangle2D(0, 0, campusMap.getWidth(), campusMap.getHeight());
    imageView.setViewport(currentViewport);
    //    resizableWindow();

    GRAPH.setGraphicsContext(gc);

    // TODO: temp for testing should be false
    editB.setVisible(true);

    if (UserHandling.getEmployee()) {
      System.out.println("EMPLOYEE");
      sideMenuUrl = "/Views/SideMenuStaff.fxml";
      if (UserHandling.getAdmin()) {
        sideMenuUrl = "/Views/SideMenuAdmin.fxml";
        System.out.println("ADMIN");
        editB.setVisible(true);
      }
    } else {
      sideMenuUrl = "/Views/SideMenu.fxml";
    }

    // draws appropriately accordingly to combination of booleans
    draw(1);

    System.out.println("NavController Initialized");

    // set onaction events for all buttons
    generalButtons();
    setStyles();

    // add menu items to context menu
    contextMenu.getItems().add(editNodeMenu);
    contextMenu.getItems().add(deleteNodeMenu);
    contextMenu.getItems().add(addEdgeMenu);

    selectingStart = true;
    selectingEnd = false;
  }

  public void setStyles() {
    editingList.setSpacing(10);
    help.setSpacing(10);
    parking.setSpacing(10);
    directionsList.setSpacing(10);
    floorsList.setSpacing(10);

    editingList.toFront();
    help.toFront();
    parking.toFront();
    directionsList.toFront();
    floorsList.toFront();

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
        });

    submitPath.setOnAction(
        e -> {
          // send path to do pathfinding actions
          doPathfind();
        });

    clearPath.setOnAction(
        e -> {
          // clear start and end locations
          clearSelection();
        });

    /** Floor onactions* */
    floorGB.setOnAction(
        e -> {
          imageView.setImage(campusMap);
          setMapViewDraw("G");
        });
    floor1B.setOnAction(
        e -> {
          imageView.setImage(floor1Map);
          setMapViewDraw("1");
        });
    floor2B.setOnAction(
        e -> {
          imageView.setImage(floor2Map);
          setMapViewDraw("2");
        });
    floor3B.setOnAction(
        e -> {
          imageView.setImage(floor3Map);
          setMapViewDraw("3");
        });
    floor4B.setOnAction(
        e -> {
          imageView.setImage(floor4Map);
          setMapViewDraw("4");
        });
    floor5B.setOnAction(
        e -> {
          imageView.setImage(floor5Map);
          setMapViewDraw("5");
        });

    /** Edit onactions* */
    editB.setOnAction(
        e -> {
          // toggle edit mode
          editing = !editing;
          editMode();
        });
    uploadB.setOnAction(
        e -> {
          // upload csv to DB
          // TODO:figure out whether its a node or edge upload

          //          //uploading node
          //          DataHandling.importExcelData(true);
          //          // TODO: re-initialize Graph after uploading excel file?
          //
          //          //uploading edge
          //          DataHandling.importExcelData(false);
          //          // TODO: re-initialize Graph after uploading excel file?
        });
    saveB.setOnAction(
        e -> {
          // save csv to computer
          // TODO:figure out whether its a node or edge save

          //          // saving node csv
          //          DataHandling.save(true);
          //
          //          // saving edge csv
          //          DataHandling.save(false);
        });
    showEdgesB.setOnAction(
        e -> {
          // show all edges
          showingEdges = !showingEdges;
          draw();
        });
  }

  /**
   * Creates a resizable GridPane with map image, menu buttons, etc.
   *
   * @return GridPane
   */
  public AnchorPane resizableWindow() {
    imageView.setPreserveRatio(true);
    //    imageView.fitHeightProperty().bind(Opp.getPrimaryStage().getScene().heightProperty());
    imageView.fitWidthProperty().bind(Opp.getPrimaryStage().getScene().widthProperty());

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

  /** resets path and creates a new path depending on start and end nodes */
  public void doPathfind() {
    if (startNode != null && endNode != null) {
      GRAPH.resetPath();
      GRAPH.findPath(Settings.getInstance().getAlgoChoice(), startNode, endNode);
      displayingRoute = true;
      selectingStart = false;
      selectingEnd = false;
    } else if (!startLoc.getText().isEmpty() && !endLoc.getText().isEmpty()) {
      startNode = GRAPH.getNodeByLongName(startLoc.getText());
      endNode = GRAPH.getNodeByLongName(endLoc.getText());
      GRAPH.resetPath();
      GRAPH.findPath("A*", startNode, endNode);
      displayingRoute = true;
      selectingStart = false;
      selectingEnd = false;
    } else {
      // TODO: add stackpane for all warnings
      //      PopupMaker.invalidPathfind(nodeWarningPane);
    }

    draw();
    pathFloors = "";
    for (Node n : GRAPH.getPath()) {
      if (!pathFloors.contains(n.getFloor())) pathFloors += n.getFloor();
    }
    for (String d : Graph.findTextDirection()) {
      //      addTextToDirectionBox(d);
    }
  }

  /**
   * determines closest node to mouse click on canvas, used for both navigating and editing the map
   *
   * @param mouseEvent
   */
  public void canvasClick(MouseEvent mouseEvent) {
    selectingStart = !selectingStart;
    selectingEnd = !selectingEnd;
    Node clickedNode =
        GRAPH.closestNode(sFloor, mouseEvent.getX(), mouseEvent.getY(), editing, imageView);

    // block for SHIFT CLICK
    if (editing && mouseEvent.isShiftDown() && mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
      if ((selectingEditNode && selectedNode == null)) {
        selectedNode = clickedNode;

      } else if (selectingEditNode && selectedNode != null && selectedNode != clickedNode) {
        alignList.add(clickedNode.getID());
      }
      selectedNodeB = null;
    }
    // ----------------------
    // block for LEFT CLICK
    else if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
      alignList = new ArrayList<>();

      selectedNodeB = null;

      // if navigating
      if (!editing) {
        if (selectingStart) {
          startNode = clickedNode;
          startLoc.setText(startNode.getLongName());
        } else if (selectingEnd) {
          endNode = clickedNode;
          endLoc.setText(startNode.getLongName());
          doPathfind();
        }
      }

    }
    // ----------------------
    // block for RIGHT CLICK
    else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {

      System.out.println("You right clicked!");
      selectedNode = clickedNode;

      contextMenu.setAutoHide(true);
      if (contextMenu.isShowing()) {
        contextMenu.hide();
      }
      mapCanvas.setOnContextMenuRequested(
          f -> {
            contextMenu.show(mapCanvas, f.getScreenX(), f.getScreenY());
          });
      contextMenuOnActions(selectedNode);

    } else if (true) {
      // TODO add dragging functionality
    }

    draw();
  }

  public void contextMenuOnActions(Node selectedNode) {
    // TODO: add functionality to these
    editNodeMenu.setOnAction(
        action -> {
          System.out.println("editing node");
        });

    deleteNodeMenu.setOnAction(
        action -> {
          //deleting node
          try {
            deleteNode(selectedNode);
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });

    addEdgeMenu.setOnAction(
        action -> {
          System.out.println("adding edge");
        });
  }

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
    // zoom option B:
    /*double percCanvasA = scrollEvent.getX() / mapCanvas.getWidth();
    double percCanvasB = scrollEvent.getY() / mapCanvas.getHeight();
    currentViewport = new Rectangle2D(a - (percCanvasA * vX), b - (percCanvasB * vY), vX, vY);*/

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

  //  /**
  //   * creates an output file
  //   *
  //   * @param fileName
  //   * @return file
  //   */
  //  public File createOutputFile(String fileName) {
  //    String home = System.getProperty("user.home");
  //    File outputFile = new File(home + "/Downloads/" + fileName);
  //    return outputFile;
  //  }
  //
  //  /**
  //   * grabs an image from a floor and gives it an output file
  //   *
  //   * @param image
  //   * @param floor
  //   * @param outputFile
  //   * @return WritableImage
  //   * @throws IOException
  //   */
  //  public WritableImage grabImage(Image image, String floor, File outputFile) throws IOException
  // {
  //
  //    imageView.setImage(image);
  //    sFloor = floor;
  //    resizeCanvas();
  //    draw();
  //    WritableImage map = innerGrid.snapshot(new SnapshotParameters(), null);
  //    ImageIO.write(SwingFXUtils.fromFXImage(map, null), "png", outputFile);
  //    return map;
  //  }
  //
  //  /**
  //   * takes pictures of every floor to email and navigates to email page
  //   *
  //   * @param actionEvent
  //   * @throws IOException
  //   */
  //  public void toSharePage(ActionEvent actionEvent) throws IOException, UnirestException {
  //
  //    GraphicsContext gc = mapCanvas.getGraphicsContext2D();
  //    mapCanvas.getGraphicsContext2D();
  //    // TODO: Insert method call that write qr.png to download folder
  //    //    SharingFunctionality.createQRCode(
  //    //        "mapimg1.png", "mapimg2.png", "mapimg3.png", "mapimg4.png", "mapimg5.png",
  //    // "mapimg6.png");
  //
  //    WritableImage map1 = grabImage(campusMap, "G", createOutputFile("mapimg1.png"));
  //    WritableImage map2 = grabImage(floor1Map, "1", createOutputFile("mapimg2.png"));
  //    WritableImage map3 = grabImage(floor2Map, "2", createOutputFile("mapimg3.png"));
  //    WritableImage map4 = grabImage(floor3Map, "3", createOutputFile("mapimg4.png"));
  //    WritableImage map5 = grabImage(floor4Map, "4", createOutputFile("mapimg5.png"));
  //    WritableImage map6 = grabImage(floor5Map, "5", createOutputFile("mapimg6.png"));
  //    LinkedList<WritableImage> listOfImages = new LinkedList<>();
  //    if (pathFloors.contains("G")) {
  //      listOfImages.add(map1);
  //    }
  //    if (pathFloors.contains("1")) {
  //      listOfImages.add(map2);
  //    }
  //    if (pathFloors.contains("2")) {
  //      listOfImages.add(map3);
  //    }
  //    if (pathFloors.contains("3")) {
  //      listOfImages.add(map4);
  //    }
  //    if (pathFloors.contains("4")) {
  //      listOfImages.add(map5);
  //    }
  //    if (pathFloors.contains("5")) {
  //      listOfImages.add(map6);
  //    }
  //    if (listOfImages.isEmpty()) {
  //      // TODO: Throw a error "you did not do pathfind"
  //    }
  //    EmailPageController.setScreenShot(map1, map2, map3, map4, map5, map6);
  //    // EmailPageController.setScreenShot(listOfImages);
  //    SwitchScene.goToParent("/Views/EmailPage.fxml");
  //  }

  public void clearSelection() {
    setNavFalse();
    selectingStart = true;

    GRAPH.resetPath();

    resizeCanvas();
    draw();
    //    directionvbox.getChildren().clear();
  }

  /** can draw the path, nodes, and edges based on booleans */
  private void draw() {
    resizeCanvas();

    // i know these can be simplified but i don't care -- this is more organized imo

    if (!editing && !displayingRoute) {
      // draw the visible Node (navigating) on sFloor + highlight start and end (if selected)
      GRAPH.drawVisibleNodes(sFloor, startNode, endNode, imageView, false);
    } else if (!editing && displayingRoute) {
      // draw the portion on sFloor + highlight start and end
      GRAPH.drawCurrentPath(sFloor, startNode, endNode, imageView, false);
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
      GRAPH.drawVisibleNodes(sFloor, startNode, endNode, imageView, false);
    } else if (!editing && displayingRoute) {
      GRAPH.drawCurrentPath(sFloor, startNode, endNode, imageView, false);
    } else if (editing) {
      GRAPH.drawAllNodes(sFloor, selectedNode, selectedNodeB, selectingEditNode, imageView, false);
      if (showingEdges) {
        GRAPH.drawAllEdges(sFloor, selectedNode, selectedNodeB, imageView, false);
      }
    }
  }

  //  /**
  //   * chooses the pathfinding algorithm used
  //   *
  //   * @param actionEvent
  //   */
  //  public void chooseStrat(ActionEvent actionEvent) {
  //    strategy = (String) algoStratCBox.getValue();
  //    Settings.getInstance().setAlgoChoice(strategy);
  //  }

  //  private void addTextToDirectionBox(String text) {
  //
  //    Text newText = new Text(text + "\n");
  //    newText.setFont(Font.font("leelawadee ui", 16.0));
  //    directionvbox.getChildren().add(newText);
  //  }

  public void alignVertically(ActionEvent actionEvent) {
    for (String s : alignList) {
      Node n = GRAPH.getNodeByID(s);
      n.setYCoord(selectedNode.getYCoord());
    }

    alignList = new ArrayList<>();
    draw();
  }

  public void alignHorizontally(ActionEvent actionEvent) {
    for (String s : alignList) {
      Node n = GRAPH.getNodeByID(s);
      n.setXCoord(selectedNode.getXCoord());
    }

    alignList = new ArrayList<>();
    draw();
  }

  public void setMapViewDraw(String floorSelected) {
    if (sFloor.equals(floorSelected)) {
      return;
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

  // TODO dragging functionality
  public void nodeDragEnter(MouseDragEvent mouseDragEvent) {}

  public void nodeDragRelease(MouseDragEvent mouseDragEvent) {}
}
