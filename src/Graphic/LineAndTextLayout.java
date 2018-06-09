package Graphic;

import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.LinkedList;

public class LineAndTextLayout {
    private Group root;
    private double Height;
    private double Width;
    private Label EdgeText;
    private CheckBox chbox;
    private Alert al;

    public  LineAndTextLayout(Group r, double d, double w){
        root = r;
        Height = d;
        Width = w;
        setEdgeTextLabel();
        CreateLine();
        setChbox();
        setAlert();
    }

    public Label getEdgeText(){
        return EdgeText;
    }

    private void setEdgeTextLabel(){
        EdgeText = new Label();
        EdgeText.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        EdgeText.setLayoutX(10);
        EdgeText.setLayoutY(Height/5*5.5);
        root.getChildren().add(EdgeText);
    }

    private void setAlert(){
         al = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) al.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("Images/Error.png"));
        al.setTitle("ERROR");
    }

    public Alert getAlert(){
        return al;
    }

    private void CreateLine(){
        LinkedList<Line> ListCreazione = new LinkedList<>();
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(Width * 5 / 3 - Width);
        rectangle.setHeight(Height * 5 / 3 );
        rectangle.setX(Width);
        rectangle.setY(0);
        rectangle.setFill(Color.valueOf("#e3ffe3"));
        root.getChildren().add(rectangle);

        Line l0 = new Line(1,1, 1, 50);
        ListCreazione.add(l0);
        l0.setStrokeWidth(1.5);
        Line l1 = new Line(1,1, 620, 1);
        ListCreazione.add(l1);
        Line l2 = new Line(2,50, Width, 50);
        ListCreazione.add(l2);
        l2.setStrokeWidth(1.5);
        Line l3 = new Line(259.5,2, 259.5, 50);
        ListCreazione.add(l3);
        l3.setStrokeWidth(1.5);
        Line l4 = new Line(540,2, 540, 50);
        ListCreazione.add(l4);
        Line l5 = new Line(620,2, 620, 50);
        ListCreazione.add(l5);
        Line div = new Line(Width, 0, Width,Width*5/3);
        ListCreazione.add(div);
        Line div2 = new Line(0,Height + 50,Width, Height+50);
        ListCreazione.add(div2);
        Line div3 = new Line(Width, 1, Width * 5 / 3, 1);
        ListCreazione.add(div3);
        for(Line l :ListCreazione){
            l.setStrokeWidth(1.5);
            l.setStroke(Color.BLACK);
            l.setFill(Color.BLACK);
            root.getChildren().add(l);
        }
        Label Dijkstra = new Label("Dijkstra");
        Dijkstra.setLayoutX(Width + (Width / 5));
        Dijkstra.setFont(Font.font("Times New Roman", 20));
        Dijkstra.setLayoutY(10);
        root.getChildren().add(Dijkstra);
    }

    private void setChbox(){
        chbox = new CheckBox();
        chbox.setLayoutX(Width - Width / 3);
        chbox.setText("Remove text from lines");
    }

    public CheckBox getChbox(){
        return chbox;
    }
}
