package EventMouse;

import DataStructure.Edge;
import DataStructure.Graph;
import DataStructure.Vertex;
import Dijkstra.Dijkstra;
import Graphic.ButtonLayout;
import Graphic.LineAndTextLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.awt.*;
import java.util.*;

public class EventMouse {
    private DataStructure.Graph G;
    private Dijkstra algo;
    private Group root;
    private Pane border;
    private double Height;
    private double Width;
    private Stage primaryStage;
    private  EventHandler<MouseEvent> CreateEventHandler;
    private  EventHandler<MouseEvent> pressedEventHandler;
    private EventHandler<MouseEvent> draggedEventHandler;
    private EventHandler<MouseEvent> movedEventHandle;
    private  String compare;
    private LineAndTextLayout layout;
    private ButtonLayout b;
    private  EventHandler<KeyEvent> stepKey;
    private Scene scene;
    public  EventMouse(Scene sc, DataStructure.Graph g , Group r, Pane p, double d, double w, Stage pr, LineAndTextLayout l , ButtonLayout btn){
        G = g;
        scene = sc;
        root = r;
        border = p;
        Height = d;
        Width = w;
        primaryStage = pr;
        compare = null;
        layout = l;
        b = btn;
        setEventClicked();
        setEventPressed();
        setEventeDragged();
        setEventMoved();
        setCheckedBox();
        setKeyCombination();
    }

    public static String selectPressed(Graph G, double X, double Y){

        Iterator<Vertex> iteratorCircle = G.getAdjList().keySet().iterator();
        while (iteratorCircle.hasNext()) {
            Vertex v = iteratorCircle.next();
            if ((X <= v.getCircle().getCenterX() + 20 && X >= v.getCircle().getCenterX() - 20) && (Y <= v.getCircle().getCenterY() + 20 && Y >= v.getCircle().getCenterY() - 20)) {
                return v.getName();
            }
        }
        return null;
    }


    public static boolean isInt(String s) { try { Integer.parseInt(s); return true; } catch(NumberFormatException e) { return false; } }

    private void setEventClicked(){

       CreateEventHandler = t -> {
            String s = selectPressed(G, t.getSceneX(), t.getSceneY()-50);
            if (t.getButton() == MouseButton.SECONDARY && !G.getStateDijkstra()) {

                if (s == null) {
                    javafx.scene.control.Label label = new javafx.scene.control.Label();
                    ContextMenu cm = new ContextMenu();
                    javafx.scene.control.MenuItem addV = new javafx.scene.control.MenuItem("Add Vertex");
                    cm.getItems().add(addV);
                    border.getChildren().add(label);
                    cm.show(label, t.getScreenX(), t.getScreenY());

                    if(G.getAdjList().keySet().size() >= 15){
                        addV.setDisable(true);
                    }

                    // Evento che mi crea un nodo e lo aggiunge al grafo
                    addV.setOnAction(event -> {
                        TextInputDialog D = new TextInputDialog();
                        D.setTitle("Creation Node");
                        D.setHeaderText("Enter the name of the node (one character)");
                        Optional<String> result = D.showAndWait();
                        if (result.isPresent() && result.get().length() == 1 && (G.getFromSet(result.get())) == null) {
                            Vertex v = new Vertex(result.get());
                            v.setCircle(t.getSceneX(), t.getSceneY() - 50, 20);
                            v.setTextT(v.getCircle().getCenterX() - 4, v.getCircle().getCenterY() - 5);
                            v.setValForDijkstra(0);
                            v.setColor(javafx.scene.paint.Color.BLACK);
                            v.setTextDijkstra(v.getCircle().getCenterX() - 4, v.getCircle().getCenterY() + 12);
                            v.getTextT().setFill(Color.WHITE);
                            border.getChildren().add(v.getCircle());
                            border.getChildren().add(v.getTextT());
                            border.getChildren().add(v.getDijkstra());
                            G.addVertex(v);
                            b.getButtonDijkstra().setDisable(false);
                            b.getButtonDelete().setDisable(false);
                            b.getButtonRandom().setDisable(true);
                            b.getButtonSave().setDisable(false);
                            b.getButtonOpen().setDisable(true);
                        }
                        else {
                            if (result.isPresent()){
                                if (result.get().length() > 1) {
                                    layout.getAlert().setHeaderText("The name of the node must be one character");
                                    layout.getAlert().showAndWait();
                                }
                                else if (G.getFromSet(result.get()) != null){
                                    layout.getAlert().setHeaderText("The node already exist");
                                    layout.getAlert().showAndWait();
                                }
                            }
                        }
                    });
                }
                else {

                    // Creazione e controlli dei vari item
                    javafx.scene.control.Label label = new javafx.scene.control.Label();
                    ContextMenu cm = new ContextMenu();
                    javafx.scene.control.MenuItem deleteV = new javafx.scene.control.MenuItem("Delete Vertex");
                    javafx.scene.control.MenuItem addE = new javafx.scene.control.MenuItem("Add Edge");
                    javafx.scene.control.MenuItem weightE = new javafx.scene.control.MenuItem("Change Weight of Edge");
                    javafx.scene.control.MenuItem deleteE = new MenuItem("Delete Edge");


                    cm.getItems().add(deleteV);
                    cm.getItems().add(addE);
                    cm.getItems().add(weightE);
                    cm.getItems().add(deleteE);
                    border.getChildren().add(label);
                    cm.show(label, t.getScreenX(), t.getScreenY());

                    Vertex V = G.getFromSet(s);
                    if (G.getAdjList().size() <= 1 || (G.getAdjList().get(V).size() == (G.getAdjList().keySet().size() - 1))) {
                        addE.setDisable(true);
                    }
                    if (G.getAdjList().size() <= 1 || G.getAdjList().get(V).isEmpty()) {
                        weightE.setDisable(true);
                        deleteE.setDisable(true);
                    }


                    // Evento che mi elimina un vertice del grafo
                    deleteV.setOnAction(event -> {
                        for (Set<Edge> list : G.getAdjList().values()) {
                            Iterator<Edge> iterator = list.iterator();
                            while (iterator.hasNext()) {
                                Edge i = iterator.next();
                                if (V.getName().compareTo(Character.toString(i.getHash().charAt(0))) == 0 || V.getName().compareTo(Character.toString(i.getHash().charAt(1))) == 0) {
                                    border.getChildren().remove(i.getLine());
                                    border.getChildren().remove(i.getText());
                                    iterator.remove();
                                }
                            }
                        }
                        border.getChildren().remove(V.getCircle());
                        border.getChildren().remove(V.getTextT());
                        border.getChildren().remove(V.getDijkstra());
                        G.removeVertex(V);
                        if(G.getAdjList().isEmpty()){
                            b.getButtonDijkstra().setDisable(true);
                            b.getButtonDelete().setDisable(true);
                            b.getButtonRandom().setDisable(false);
                            b.getButtonSave().setDisable(true);
                            b.getButtonOpen().setDisable(false);
                            border.getChildren().remove(layout.getChbox());
                        }
                        int x = 0;
                        for(Set<Edge> c : G.getAdjList().values()){
                            for(Edge e1: c){
                                x++;
                            }
                        }
                        if( x ==0 ){

                            layout.getChbox().setSelected(false);
                            root.getChildren().remove(layout.getChbox());
                        }
                        layout.getEdgeText().setText("");
                    });





                    // Evento che mi aggiunge un arco al grafo
                    addE.setOnAction(event -> {
                        TextInputDialog D1 = new TextInputDialog();
                        D1.setTitle("Set node V");
                        D1.setHeaderText("Enter the name of the node V");
                        Optional<String> result1 = D1.showAndWait();
                        if (result1.isPresent()) {
                            Vertex U = G.getFromSet(result1.get());
                            Edge e = new Edge(V, U, 0);;
                            if (U != null && !(result1.get().compareTo(V.getName()) == 0) && !G.checkIfEdgeIsPresent(e)) {
                                TextInputDialog D2 = new TextInputDialog();
                                D2.setTitle("Set weight");
                                D2.setHeaderText("Enter the weight of edge");
                                Optional<String> result2 = D2.showAndWait();
                                if (result2.isPresent() && isInt(result2.get()) && (Integer.parseInt(result2.get()) <= 50) && (Integer.parseInt(result2.get()) >= 0)) {
                                    e.setPeso(Integer.parseInt(result2.get()));
                                    double setStartX = V.getCircle().getCenterX();
                                    double setStartY = V.getCircle().getCenterY();
                                    double setEndX = U.getCircle().getCenterX();
                                    double setEndY = U.getCircle().getCenterY();
                                    if (G.getInvertedEdge(e)) {
                                        e.setLine(setStartX , (setStartY + 15.0), setEndX, (setEndY + 15.0));
                                        e.setText((((setEndX + setStartX) / 2.0) - 20.0), (((setEndY + setStartY) / 2.0) + 30.0));
                                    } else {
                                        e.setLine(setStartX, (setStartY - 15.0), setEndX, (setEndY - 15.0));
                                        e.setText((((setEndX + setStartX) / 2.0) - 20.0), (((setEndY + setStartY) / 2.0) - 20.0));
                                    }
                                    e.getLine().setStrokeWidth(1.5);
                                    for (Vertex v : G.getAdjList().keySet()) {
                                        border.getChildren().remove(v.getCircle());
                                        border.getChildren().remove(v.getTextT());
                                        border.getChildren().remove(v.getDijkstra());
                                    }
                                    border.getChildren().add(e.getLine());
                                    border.getChildren().add(e.getText());
                                    for (Vertex v : G.getAdjList().keySet()) {
                                        border.getChildren().add(v.getCircle());
                                        border.getChildren().add(v.getTextT());
                                        border.getChildren().add(v.getDijkstra());
                                    }
                                    G.addEdge(e);
                                    int x = 0;
                                    for(Set<Edge> c : G.getAdjList().values()){
                                        for(Edge e1: c){
                                            x++;
                                        }
                                    }
                                    if( x == 1){
                                        root.getChildren().add(layout.getChbox());
                                    }
                                }
                                else{
                                    if(result2.isPresent()){
                                        if(!isInt(result2.get())){
                                            layout.getAlert().setHeaderText("Weight must be an integer");
                                            layout.getAlert().showAndWait();
                                        }
                                        else {
                                            if(Integer.parseInt(result2.get()) > 50 || (Integer.parseInt(result2.get()) < 0)){
                                                layout.getAlert().setHeaderText("Weight must be a umber between 0 and 50");
                                                layout.getAlert().showAndWait();
                                            }
                                        }
                                    }
                                }
                            }
                            else{
                                if (U == null) {
                                    layout.getAlert().setHeaderText("Node U doesn't exist in the graph");
                                    layout.getAlert().showAndWait();
                                }
                                else if(result1.get().compareTo(V.getName()) == 0){
                                    layout.getAlert().setHeaderText("The nodes are equal");
                                    layout.getAlert().showAndWait();
                                }
                            }
                        }
                    });



                    // Evento che mi cambia il peso di un arco specifico del grafo
                    weightE.setOnAction(event -> {
                        TextInputDialog D1 = new TextInputDialog();
                        D1.setTitle("Change weight of Edge");
                        D1.setHeaderText("Enter the name of the incoming vertex");
                        Optional<String> result1 = D1.showAndWait();
                        if (result1.isPresent() && G.getFromSet(result1.get()) != null) {
                            String hash = s + result1.get();
                            Iterator<Edge> iterator = G.getAdjList().get(V).iterator();
                            while (iterator.hasNext()) {
                                Edge e = iterator.next();
                                if (hash.compareTo(e.getHash()) == 0) {
                                    TextInputDialog D2 = new TextInputDialog();
                                    D2.setTitle("Change weight of Edge");
                                    D2.setHeaderText("Enter the weight");

                                    Optional<String> result2 = D2.showAndWait();
                                    if (result2.isPresent() && isInt(result2.get()) && (Integer.parseInt(result2.get()) <= 50) && (Integer.parseInt(result2.get()) >= 0)) {
                                        border.getChildren().remove(e.getText());
                                        e.setPeso(Integer.parseInt(result2.get()));
                                        e.setText(e.getText().getX(), e.getText().getY());
                                        border.getChildren().add(e.getText());
                                        break;
                                    }
                                    else{
                                        if(result2.isPresent()){
                                            if(!isInt(result2.get())){
                                                layout.getAlert().setHeaderText("Weight must be an integer");
                                                layout.getAlert().showAndWait();
                                            }
                                            else {
                                                if(Integer.parseInt(result2.get()) > 50 || (Integer.parseInt(result2.get()) < 0)){
                                                    layout.getAlert().setHeaderText("Weight must be a umber between 0 and 50");
                                                    layout.getAlert().showAndWait();
                                                }
                                            }
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            if(result1.isPresent()){
                                if(G.getFromSet(result1.get()) == null){
                                    layout.getAlert().setHeaderText("The node V doesn't exist");
                                    layout.getAlert().showAndWait();
                                }
                            }
                        }
                    });


                    // Evento che elimina un arco specifico del grafo
                    deleteE.setOnAction(event -> {
                        TextInputDialog D = new TextInputDialog();
                        D.setTitle("Delete Edge");
                        D.setHeaderText("Enter the name of the incoming vertex");

                        Optional<String> result = D.showAndWait();
                        if (result.isPresent() && G.getFromSet(result.get()) != null) {
                            String hash = s + result.get();
                            Iterator<Edge> iterator = G.getAdjList().get(V).iterator();
                            while (iterator.hasNext()) {
                                Edge e = iterator.next();
                                if (hash.compareTo(e.getHash()) == 0) {
                                    border.getChildren().remove(e.getLine());
                                    border.getChildren().remove(e.getText());
                                    G.removeEdge(e);

                                    int x = 0;
                                    for(Set<Edge> c : G.getAdjList().values()){
                                        for(Edge e1: c){
                                            x++;
                                        }
                                    }
                                    if(x == 0){

                                        layout.getChbox().setSelected(false);
                                        root.getChildren().remove(layout.getChbox());
                                    }
                                    break;
                                }
                            }
                        }
                        else{
                            if(result.isPresent()){
                                if (G.getFromSet(result.get()) == null){
                                    layout.getAlert().setHeaderText("The node V doesn't exist");
                                    layout.getAlert().showAndWait();
                                }
                            }
                        }
                    });
                }
            }
        };
    }

    public  EventHandler<MouseEvent> getEventClicked(){
        return  CreateEventHandler;
    }


    private void setEventPressed(){

        pressedEventHandler = t ->{

            if(t.getButton() == MouseButton.PRIMARY){
                compare = selectPressed(G, t.getSceneX(), t.getSceneY()-50);
            }
        };
    }

    public  EventHandler<MouseEvent> getEventPressed(){
        return pressedEventHandler;
    }

    private void setEventeDragged(){

        draggedEventHandler = t ->{
            if(t.getButton() == MouseButton.PRIMARY && !G.getAdjList().keySet().isEmpty() && compare != null) {
                Vertex V = G.getFromSet(compare);
                int radius = 20;
                if ((t.getSceneX() >  radius && t.getSceneX() <  Width - radius) && (t.getSceneY() > 50 + radius && t.getSceneY() < 50 + Height - radius)) {
                    for (Set<Edge> E : G.getAdjList().values()) {
                        for (Edge edge : E) {
                            if (edge.getU().getName().compareTo(V.getName()) == 0) {
                                if (edge.getLine().getStartX() == V.getCircle().getCenterX() && edge.getLine().getStartY() == (V.getCircle().getCenterY() + 15.0)) {
                                    edge.getLine().setStartX(t.getSceneX());
                                    edge.getLine().setStartY(t.getSceneY() + 15.0 - 50);
                                    edge.getText().setX(((edge.getLine().getEndX() + edge.getLine().getStartX()) / 2.0) - 20.0);
                                    edge.getText().setY(((edge.getLine().getEndY() + edge.getLine().getStartY()) / 2.0) + 15.0);
                                } else if (edge.getLine().getStartX() == V.getCircle().getCenterX() && edge.getLine().getStartY() == (V.getCircle().getCenterY() - 15.0)) {
                                    edge.getLine().setStartX(t.getSceneX());
                                    edge.getLine().setStartY(t.getSceneY() - 15.0 - 50);
                                    edge.getText().setX(((edge.getLine().getEndX() + edge.getLine().getStartX()) / 2.0) - 20.0);
                                    edge.getText().setY(((edge.getLine().getEndY() + edge.getLine().getStartY()) / 2.0) - 5.0);
                                }
                            } else if (edge.getV().getName().compareTo(V.getName()) == 0) {

                                if (edge.getLine().getEndX() == V.getCircle().getCenterX() && edge.getLine().getEndY() == (V.getCircle().getCenterY() + 15.0)) {
                                    edge.getLine().setEndX(t.getSceneX() );
                                    edge.getLine().setEndY(t.getSceneY() + 15.0 - 50);
                                    edge.getText().setX(((edge.getLine().getEndX() + edge.getLine().getStartX()) / 2.0) - 20.0);
                                    edge.getText().setY(((edge.getLine().getEndY() + edge.getLine().getStartY()) / 2.0) + 15.0);
                                } else if (edge.getLine().getEndX() == V.getCircle().getCenterX() && edge.getLine().getEndY() == (V.getCircle().getCenterY() - 15.0)) {
                                    edge.getLine().setEndX(t.getSceneX() );
                                    edge.getLine().setEndY(t.getSceneY() - 15.0 - 50);
                                    edge.getText().setX(((edge.getLine().getEndX() + edge.getLine().getStartX()) / 2.0) - 20.0);
                                    edge.getText().setY(((edge.getLine().getEndY() + edge.getLine().getStartY()) / 2.0) - 5.0);
                                }
                            }
                        }
                    }
                    V.getCircle().setCenterX(t.getSceneX() );
                    V.getCircle().setCenterY(t.getSceneY() - 50);
                    V.getTextT().setX(t.getSceneX() - 4 );
                    V.getTextT().setY(t.getSceneY() - 5 - 50);

                    if (V.getValForDijkstra() == Double.POSITIVE_INFINITY){
                        V.getDijkstra().setX(V.getCircle().getCenterX() - 3.2);
                    }
                    else {
                        switch (Integer.toString((int) V.getValForDijkstra()).length()) {
                            case 1:
                                V.getDijkstra().setX(V.getCircle().getCenterX() - 3.2);
                                break;
                            case 2:
                                V.getDijkstra().setX(V.getCircle().getCenterX() - 6.4);
                                break;
                            case 3:
                                V.getDijkstra().setX(V.getCircle().getCenterX() - 9.6);
                                break;
                            case 4:
                                V.getDijkstra().setX(V.getCircle().getCenterX() - 12.8);
                                break;
                        }
                    }
                    V.getDijkstra().setY(t.getSceneY() + 12 - 50);
                }
            }
        };
    }

    public EventHandler<MouseEvent> getDraggedEventHandler(){
        return draggedEventHandler;
    }


    private void setEventMoved(){
        movedEventHandle = t ->{
            compare = selectPressed(G, t.getSceneX() , t.getSceneY() - 50);
            if (compare != null) {
                Vertex V = G.getFromSet(compare);
                V.getCircle().setFill(Color.BLUE);
                if(G.getStateDijkstra()){
                    if(V.getDijkstra().getFill() == Color.RED){
                        V.getDijkstra().setFill(Color.RED);
                    }
                    else{
                        V.getDijkstra().setFill(Color.WHITE);
                    }
                }
                else{
                    V.getDijkstra().setFill(Color.BLUE);
                }
                layout.getEdgeText().setText("Nome nodo: " + G.getFromSet(compare).getName() + "\n");
                int i = 0;
                for (Edge e : G.getAdjList().get(V)) {
                    if(i == 0){
                        layout.getEdgeText().setText(layout.getEdgeText().getText() + "| " + e.getU().getName() + "->" + e.getV().getName() + "  " + e.getPeso());
                    }
                    else {
                        layout.getEdgeText().setText(layout.getEdgeText().getText() + " | " + e.getU().getName() + "->" + e.getV().getName() + "  " + e.getPeso());
                    }
                }
            } else {
                for (Vertex v : G.getAdjList().keySet()) {
                    if(G.getStateDijkstra()){
                        v.getCircle().setFill(v.getColor());
                        if(v.getDijkstra().getFill() == Color.RED){
                            v.getDijkstra().setFill(Color.RED);
                        }
                        else{
                            v.getDijkstra().setFill(Color.WHITE);
                        }
                    }
                    else {
                        v.getCircle().setFill(Color.BLACK);
                        v.getDijkstra().setFill(Color.BLACK);
                    }
                    layout.getEdgeText().setText("");
                }
            }
        };

    }

    public EventHandler<MouseEvent> getMovedEventHandle(){
        return movedEventHandle;
    }

    private void setCheckedBox(){
            layout.getChbox().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(layout.getChbox().isSelected()){
                        for(Set<Edge> e : G.getAdjList().values()){
                            for(Edge e1: e){
                                border.getChildren().remove(e1.getText());
                            }
                        }
                    }else {
                        for(Set<Edge> e : G.getAdjList().values()){
                            for(Edge e1: e){
                                border.getChildren().add(e1.getText());
                            }
                        }
                    }
                }
            });
    }

    private void setKeyCombination(){


        stepKey = event -> { if(event.getCode() == KeyCode.ENTER){ b.getButtonStep().fire(); } };
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN), () -> b.getButtonEnd().fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), () -> b.getButtonSave().fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN), () -> b.getButtonOpen().fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN), () -> b.getButtonHelp().fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN), () -> b.getButtonRandom().fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN), () -> b.getButtonDijkstra().fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN), () -> b.getButtonDelete().fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN), () -> b.getButtonReset().fire());
    }

    public  EventHandler<KeyEvent> getStepKey(){
        return stepKey;
    }

}
