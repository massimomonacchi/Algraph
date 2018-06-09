import DataStructure.Edge;
import DataStructure.Graph;
import DataStructure.Vertex;
import EventMouse.EventMouse;
import Graphic.ButtonLayout;
import Graphic.LineAndTextLayout;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.awt.*;
import java.util.*;


public class Main extends Application {


    Pane border = new Pane();

    String compare = null;



    @Override
    public void start(Stage primaryStage){

        final Group root = new Group();
        border.setStyle("-fx-background-color:#d0e4e7;");
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double Width = screenSize.width/5*3.5;
        double Height = screenSize.height/5*3.5;
        border.setPrefSize(Width, Height);
        border.setLayoutX(0);
        border.setLayoutY(50);
        root.getChildren().add(border);

        Scene scene = new Scene(root, 850, 350);
        primaryStage.setTitle("ALGRAPH");
        primaryStage.getIcons().add(new Image("Images/A.png"));
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();



        Graph G = new Graph();

        LineAndTextLayout l = new LineAndTextLayout(root, Height, Width);
        ButtonLayout b = new ButtonLayout(G,root, l,border, Height, Width, primaryStage);
        EventMouse em = new EventMouse(scene,G,root,border,Height, Width, primaryStage,l,b);

        border.addEventHandler(MouseEvent.MOUSE_CLICKED, em.getEventClicked());

        border.addEventHandler(MouseEvent.MOUSE_PRESSED, em.getEventPressed());

        border.addEventHandler(MouseEvent.MOUSE_DRAGGED, em.getDraggedEventHandler());

        primaryStage.addEventHandler(MouseEvent.MOUSE_MOVED, em.getMovedEventHandle());

        root.setOnKeyPressed(em.getStepKey());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
