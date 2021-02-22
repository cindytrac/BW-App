package edu.wpi.teamO;

import edu.wpi.teamO.Controllers.DatabaseFunctionality;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;

public class Opp extends Application {

  @Getter private static Stage primaryStage;

  public void init() {
    // This happens every time a scene starts up. Kinda cool but not currently useful
    System.out.println("Starting Up");
  }

  public void start(Stage ps) {

    Opp.primaryStage = ps;

    try {
      AnchorPane root = FXMLLoader.load(getClass().getResource("/Views/Index.fxml"));
      Scene scene = new Scene(root);

      ps.setScene(scene);
      ps.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    // shut down database and print message to user
    DatabaseFunctionality.shutDownDB();
    System.out.println("Shutting Down");
  }
}
