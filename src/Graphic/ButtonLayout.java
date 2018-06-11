package Graphic;
import DataStructure.Edge;
import Dijkstra.Dijkstra;
import DataStructure.Vertex;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;


public class ButtonLayout {

    private Button OpenFile; private Button Dijkstra; private Button step; private Button end; private Button Reset; private Button Delete; private Button Random; private Button SaveOnFile; private Button Help;
    private DataStructure.Graph G;
    private Dijkstra algo;
    private Group root;
    private Pane border;
    private double Height;
    private double Width;
    private Stage primaryStage;
    private LinkedList<Line> listLine = new LinkedList<>();
    private Map<String, Vertex> Ac = new HashMap<>();
    private Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    private LineAndTextLayout layout;

    public static boolean isInt(String s) { try { Integer.parseInt(s); return true; } catch(NumberFormatException e) { return false; } }

    public  ButtonLayout(DataStructure.Graph g , Group r, LineAndTextLayout l, Pane p, double d, double w, Stage pr){
        G = g;
        root = r;
        layout = l;
        border = p;
        Height = d;
        Width = w;
        primaryStage = pr;
        OpenFile = new Button(); Dijkstra= new Button();  step= new Button(); end= new Button();  Reset= new Button();  Delete= new Button();  Random= new Button();  SaveOnFile= new Button();  Help= new Button();
        setButtonOpen();
        setButtonDelete();
        setButtonDijkstra();
        setButtonHelp();
        setButtonRandom();
        setButtonReset();
        setButtonSave();
        setButtonStep();
        setButtonEnd();
        root.getChildren().addAll(getButtonOpen(), getButtonDelete(), getButtonReset(), getButtonRandom(), getButtonSave(), getButtonHelp(), getButtonDijkstra());

    }

    private void setButtonOpen(){
        OpenFile.setText("Open");
        OpenFile.setGraphic(new ImageView(new Image("Images/OpenFile.png")));
        OpenFile.setLayoutX(5);
        OpenFile.setLayoutY(7);
        OpenFile.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open");
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if(selectedFile == null){
                return;
            }
            if (selectedFile.toString().charAt(selectedFile.toString().length() - 4) != '.' || selectedFile.toString().charAt(selectedFile.toString().length() - 3) != 't' || selectedFile.toString().charAt(selectedFile.toString().length() - 2) != 'x' || selectedFile.toString().charAt(selectedFile.toString().length() - 1) != 't'){
                layout.getAlert().setHeaderText("Extension file wrong! It must be .txt");
                layout.getAlert().showAndWait();
                return;
            }
            try {
                FileReader fileReader = new FileReader(selectedFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                double n_Vertex = 0.00;
                double a = 0.00;
                line = bufferedReader.readLine();
                LinkedList<String> listVer = new LinkedList<>();
                if(line.length() == 1){
                    layout.getAlert().setHeaderText("Wrong file!");
                    layout.getAlert().showAndWait();
                    return;
                }
                while ((line = bufferedReader.readLine()) != null) {
                    if(line.length()== 0 ){

                    }
                    else if(line.length()> 1) {
                        if ((line.charAt(0) == 'A' && line.charAt(1) == 'r') ||(line.charAt(0) == 'a' && line.charAt(1) == 'R')||(line.charAt(0) == 'a' && line.charAt(1) == 'r')) {
                            break;
                        }else{
                            line =  line.replace(" ", "");
                            if(line.length() == 1){
                                if(listVer.contains(line) == false) {
                                    listVer.add(line);
                                    n_Vertex = n_Vertex + 1.00;
                                }
                            }else {
                                layout.getAlert().setHeaderText("Node name '" + line + "' at line " + (int) (n_Vertex+2.00) +" is wrong");
                                layout.getAlert().showAndWait();
                                fileReader.close();
                                return;
                            }
                        }
                    }else if(line.length() == 1) {
                        if(listVer.contains(line) == false) {
                            listVer.add(line);
                            n_Vertex = n_Vertex + 1.00;
                        }
                    }
                }
                double ang = (double)360 / n_Vertex;
                fileReader.close();

                if(n_Vertex > 15){
                    layout.getAlert().setHeaderText("The max number of vertex is 15");
                    layout.getAlert().showAndWait();
                    return;
                }
                listVer.clear();
                FileReader fileReader2 = new FileReader(selectedFile);
                BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
                String line2;
                while ((line2 = bufferedReader2.readLine()) != null) {
                    if(line2.length() != 0){
                        line2 =  line2.replace(" ", "");
                        if(line2.length()> 1) {
                            if ((line2.charAt(0) == 'A' && line2.charAt(1) == 'R') ||(line2.charAt(0) == 'a' && line.charAt(1) == 'R')||(line2.charAt(0) == 'a' && line2.charAt(1) == 'r')|| (line2.charAt(0) == 'A' && line2.charAt(1) == 'r')) {
                                break;
                            }
                        }else {
                            if (listVer.contains(String.valueOf(line2.charAt(0))) == false) {
                                Vertex nodo = new Vertex(String.valueOf(line2.charAt(0)));
                                nodo.setValForDijkstra(0);
                                nodo.setCircle(((Height - 100)/2)*Math.cos(Math.toRadians(a))+ Width/2, ((Height - 100)/2)*Math.sin(Math.toRadians(a))+ Height/2, 20);
                                nodo.setTextT(nodo.getCircle().getCenterX() - 4, nodo.getCircle().getCenterY() - 5);
                                nodo.setValForDijkstra(0);
                                nodo.setTextDijkstra(nodo.getCircle().getCenterX() - 4, nodo.getCircle().getCenterY() + 12);
                                nodo.getTextT().setFill(Color.WHITE);
                                border.getChildren().add(nodo.getCircle());
                                border.getChildren().add(nodo.getTextT());
                                border.getChildren().add(nodo.getDijkstra());
                                G.addVertex(nodo);
                                a = a + ang;
                                listVer.add(String.valueOf(line2.charAt(0)));
                            }
                        }
                    }
                }
                while ((line2 = bufferedReader2.readLine()) != null) {
                    line2 = line2.replace(" ", "");
                    if(line2.length() != 0){
                        if(line2.length() > 4 || line2.length() < 4){
                            for (Set<Edge> list : G.getAdjList().values()) {
                                for (Edge i: list){
                                    border.getChildren().remove(i.getLine());
                                    border.getChildren().remove(i.getText());
                                }
                            }
                            for (Vertex v: G.getAdjList().keySet()){
                                border.getChildren().remove(v.getCircle());
                                border.getChildren().remove(v.getTextT());
                                border.getChildren().remove(v.getDijkstra());
                            }
                            G.getAdjList().clear();
                            Delete.setDisable(true);
                            Dijkstra.setDisable(true);
                            Random.setDisable(false);
                            OpenFile.setDisable(false);
                            SaveOnFile.setDisable(true);
                            layout.getAlert().setHeaderText("Error input of the edge");
                            layout.getAlert().showAndWait();
                            fileReader2.close();
                            return;
                        }
                        String v1 = String.valueOf(line2.charAt(0));
                        String v2 = String.valueOf(line2.charAt(3));
                        line2 = bufferedReader2.readLine();
                        if(line2 == null || line2.length() >2  || line2.length() == 0){
                            for (Set<Edge> list : G.getAdjList().values()) {
                                for (Edge i: list){
                                    border.getChildren().remove(i.getLine());
                                    border.getChildren().remove(i.getText());
                                }
                            }
                            for (Vertex v: G.getAdjList().keySet()){
                                border.getChildren().remove(v.getCircle());
                                border.getChildren().remove(v.getTextT());
                                border.getChildren().remove(v.getDijkstra());
                            }
                            G.getAdjList().clear();
                            Delete.setDisable(true);
                            Dijkstra.setDisable(true);
                            Random.setDisable(false);
                            OpenFile.setDisable(false);
                            SaveOnFile.setDisable(false);
                            layout.getAlert().setHeaderText("Weight wrong");
                            layout.getAlert().showAndWait();
                            return;
                        }

                        for (Vertex v : G.getAdjList().keySet()) {
                            if (v.getName().compareTo(v1) == 0) {
                                Edge e = new Edge(G.getFromSet(v1), G.getFromSet(v2), Integer.parseInt(line2));
                                double setStartX = v.getCircle().getCenterX();
                                double setStartY = v.getCircle().getCenterY();
                                double setEndX = G.getFromSet(v2).getCircle().getCenterX();
                                double setEndY = G.getFromSet(v2).getCircle().getCenterY();
                                if (G.getInvertedEdge(e)) {
                                    e.setLine(setStartX, (setStartY + 15.0), setEndX, (setEndY + 15.0));
                                    e.setText((((setEndX + setStartX) / 2.0) - 20.0), (((setEndY + setStartY) / 2.0) + 30.0));
                                } else {
                                    e.setLine(setStartX, (setStartY - 15.0), setEndX, (setEndY - 15.0));
                                    e.setText((((setEndX + setStartX) / 2.0) - 20.0), (((setEndY + setStartY) / 2.0) - 20.0));
                                }
                                e.getLine().setStrokeWidth(1.5);
                                for (Vertex v3 : G.getAdjList().keySet()) {
                                    border.getChildren().remove(v3.getCircle());
                                    border.getChildren().remove(v3.getTextT());
                                    border.getChildren().remove(v3.getDijkstra());
                                }
                                border.getChildren().add(e.getLine());
                                border.getChildren().add(e.getText());
                                for (Vertex v4 : G.getAdjList().keySet()) {
                                    border.getChildren().add(v4.getCircle());
                                    border.getChildren().add(v4.getTextT());
                                    border.getChildren().add(v4.getDijkstra());
                                }
                                G.addEdge(e);
                            }
                        }
                    }
                }
                fileReader2.close();
                int x = 0;
                for(Set<Edge> c : G.getAdjList().values()){
                    for(Edge e1: c){
                        x++;
                    }
                }
                if( x >= 1){
                    root.getChildren().add(layout.getChbox());
                }
                Dijkstra.setDisable(false);
                Delete.setDisable(false);
                Random.setDisable(true);
                OpenFile.setDisable(true);
                SaveOnFile.setDisable(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Button getButtonOpen(){ return OpenFile; }

    private void setButtonDijkstra(){
        Dijkstra.setText("Dijkstra");
        Dijkstra.setGraphic(new ImageView(new Image("Images/Dijkstra.png")));
        Dijkstra.setLayoutX(360);
        Dijkstra.setLayoutY(7);
        Dijkstra.setDisable(true);
        Dijkstra.setOnAction(e -> {
            TextInputDialog primoNodo = new TextInputDialog();
            primoNodo.setTitle("First vertex");
            primoNodo.setHeaderText("Enter the first vertex");
            Optional<String> firstV = primoNodo.showAndWait();
            if(firstV.isPresent() && (G.getFromSet(firstV.get()) != null) ){
                G.setStateDijkstraOn();
                Reset.setDisable(false);
                Delete.setDisable(true);
                Dijkstra.setDisable(true);

                algo = new Dijkstra();
                algo.dijkstra(root, G, firstV.get(),listLine, Ac, step, end);
            }
            else{
                if (firstV.isPresent()) {
                    layout.getAlert().setHeaderText("The node doesn't exist");
                    layout.getAlert().showAndWait();
                }
            }
        });
    }

    public Button getButtonDijkstra(){ return Dijkstra; }

    private void setButtonStep(){
        step.setText("Step by step");
        step.setMaxWidth(100);
        step.setGraphic(new ImageView(new Image("Images/DijsktraStepByStep.png")));
        step.setLayoutX(screenSize.getWidth() - screenSize.getWidth() / 7);
        step.setLayoutY(screenSize.height / 2 - 80);
    }

    public Button getButtonStep(){ return step; }

    private void setButtonEnd(){
        end.setGraphic(new ImageView(new Image("Images/DijkstraToTheEnd.png")));
        end.setLayoutX(screenSize.getWidth() - screenSize.getWidth() / 7);
        end.setLayoutY(screenSize.height / 2 - 40);
        end.setPrefSize(100,10);
        end.setText("  To end");
        end.setMaxWidth(100);
    }

    public Button getButtonEnd(){
        return end;
    }

    private void setButtonReset(){
        Reset.setText("Reset");
        Reset.setGraphic(new ImageView(new Image("Images/Reset.png")));
        Reset.setDisable(true);
        Reset.setLayoutX(455);
        Reset.setLayoutY(7);
        Reset.setOnAction(event -> {
            for (Vertex v: G.getAdjList().keySet()){
                v.setValForDijkstra(0);
                v.getDijkstra().setText("0");
                v.getDijkstra().setX(v.getCircle().getCenterX() - 4);
                v.getDijkstra().setFill(Color.BLACK);
                v.getCircle().setFill(Color.BLACK);
                v.setColor(Color.BLACK);
            }
            G.setStateDijkstraOff();

            root.getChildren().removeAll(algo.getRectangle(), algo.getLabel());

            for(Vertex v : Ac.values()){
                root.getChildren().removeAll(v.getCircle());
            }

            root.getChildren().removeAll(listLine);
            root.getChildren().removeAll(step, end);

            Ac.clear();
            listLine.clear();

            Dijkstra.setDisable(false);
            Delete.setDisable(false);
            Reset.setDisable(true);
        });
    }

    public Button getButtonReset(){ return Reset; }

    private void setButtonDelete(){
        Delete.setText("Cancel");
        Delete.setGraphic(new ImageView(new Image("Images/DeleteFile.png")));
        Delete.setDisable(true);
        Delete.setLayoutX(170);
        Delete.setLayoutY(7);
        Delete.setOnAction(e -> {
            for (Set<Edge> list : G.getAdjList().values()) {
                for (Edge i: list){
                    border.getChildren().remove(i.getLine());
                    border.getChildren().remove(i.getText());
                }
            }
            for (Vertex v: G.getAdjList().keySet()){
                border.getChildren().remove(v.getCircle());
                border.getChildren().remove(v.getTextT());
                border.getChildren().remove(v.getDijkstra());
            }
            G.getAdjList().clear();
            Delete.setDisable(true);
            Dijkstra.setDisable(true);
            Random.setDisable(false);
            OpenFile.setDisable(false);
            SaveOnFile.setDisable(true);
            layout.getChbox().setSelected(false);
            root.getChildren().remove(layout.getChbox());
        });
    }

    public Button getButtonDelete(){ return Delete; }

    private void setButtonRandom(){

        Random.setText("Random");
        Random.setGraphic(new ImageView(new Image("Images/Random.png")));
        Random.setPrefSize(Random.getPrefWidth(), 39.00);
        Random.setLayoutX(265);
        Random.setLayoutY(7);
        Random.setDefaultButton(false);
        Random.setOnAction(event -> {
            int n_Vertex;
            int w_Edges;
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Create Random Graph");

            // Set the button types.
            ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            TextField to = new TextField();
            TextField from = new TextField();

            gridPane.add(new Label("Insert the number of the vertex (1-15):"), 0, 0);
            gridPane.add(from, 1, 0);
            gridPane.add(new Label("Insert the max weight of edges (0-50):"), 0, 1);
            gridPane.add(to, 1, 1);

            dialog.getDialogPane().setContent(gridPane);

            Platform.runLater(() -> from.requestFocus());

            dialog.setResultConverter(dialogButton -> {
                Pair prova = new Pair<>(from.getText(), to.getText());
                if (dialogButton == loginButtonType) {
                    try {
                        Integer.parseInt(prova.getKey().toString());
                        Integer.parseInt(prova.getValue().toString());
                    }
                    catch (NumberFormatException e) {
                        layout.getAlert().setHeaderText("Error string input");
                        layout.getAlert().showAndWait();
                        return new Pair<>("", "");
                    }
                    catch (NullPointerException e) {
                        return new Pair<>("", "");
                    }
                }
                return new Pair<>(from.getText(), to.getText());
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();
            if (result.isPresent()  && isInt(result.get().getKey()) && isInt(result.get().getValue()) && Integer.parseInt(result.get().getKey()) >= 1 && Integer.parseInt(result.get().getKey()) <= 15 && Integer.parseInt(result.get().getValue()) >= 0 && Integer.parseInt(result.get().getValue()) <= 50) {
                n_Vertex = Integer.parseInt(result.get().getKey());
                w_Edges = Integer.parseInt(result.get().getValue());
            } else {
                if (result.isPresent() && (result.get().getKey().length() >= 1 || result.get().getValue().length() >= 1)){
                    if (Integer.parseInt(result.get().getKey()) < 1 || Integer.parseInt(result.get().getKey()) > 15 ){
                        layout.getAlert().setHeaderText("The number of vertex must be between 1 and 15");
                        layout.getAlert().showAndWait();
                    }
                    else if (Integer.parseInt(result.get().getValue()) < 0 || Integer.parseInt(result.get().getValue()) > 50){
                        layout.getAlert().setHeaderText("The weight must be between 0 and 50");
                        layout.getAlert().showAndWait();
                    }
                    return;
                }
                return;
            }

            String vett[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "L", "M", "N", "O", "P", "Q"};
            double ang = (double) 360 / (double) n_Vertex;
            double a = 0.00;
            for (int i = 1; i <= n_Vertex; i++) {
                Vertex nodo = new Vertex(vett[i - 1]);
                nodo.setValForDijkstra(0);
                nodo.setCircle(((Height - 100) / 2) * Math.cos(Math.toRadians(a)) + Width / 2, ((Height - 100) / 2) * Math.sin(Math.toRadians(a)) + Height / 2, 20);
                nodo.setTextT(nodo.getCircle().getCenterX() - 4, nodo.getCircle().getCenterY() - 5);
                nodo.setValForDijkstra(0);
                nodo.setTextDijkstra(nodo.getCircle().getCenterX() - 4, nodo.getCircle().getCenterY() + 12);
                nodo.getTextT().setFill(Color.WHITE);
                border.getChildren().add(nodo.getCircle());
                border.getChildren().add(nodo.getTextT());
                border.getChildren().add(nodo.getDijkstra());
                G.addVertex(nodo);
                a = a + ang;
            }
            int prob = 100 - (n_Vertex * 6);
            for (Vertex v : G.getAdjList().keySet()) {
                for (int i = 1; i <= n_Vertex; i++) {
                    if (((Math.random() * 100) <= prob) && v.getName().compareTo(vett[i - 1]) != 0) {
                        Edge e = new Edge(v, G.getFromSet(vett[i - 1]), (int) (Math.random() * w_Edges));
                        double setStartX = v.getCircle().getCenterX();
                        double setStartY = v.getCircle().getCenterY();
                        double setEndX = G.getFromSet(vett[i - 1]).getCircle().getCenterX();
                        double setEndY = G.getFromSet(vett[i - 1]).getCircle().getCenterY();
                        if (G.getInvertedEdge(e)) {
                            e.setLine(setStartX, (setStartY + 15.0), setEndX, (setEndY + 15.0));
                            e.setText((((setEndX + setStartX) / 2.0) - 20.0), (((setEndY + setStartY) / 2.0) + 30.0));
                        } else {
                            e.setLine(setStartX, (setStartY - 15.0), setEndX, (setEndY - 15.0));
                            e.setText((((setEndX + setStartX) / 2.0) - 20.0), (((setEndY + setStartY) / 2.0) - 20.0));
                        }
                        e.getLine().setStrokeWidth(1.5);
                        for (Vertex v1 : G.getAdjList().keySet()) {
                            border.getChildren().remove(v1.getCircle());
                            border.getChildren().remove(v1.getTextT());
                            border.getChildren().remove(v1.getDijkstra());
                        }
                        border.getChildren().add(e.getLine());
                        border.getChildren().add(e.getText());
                        for (Vertex v2 : G.getAdjList().keySet()) {
                            border.getChildren().add(v2.getCircle());
                            border.getChildren().add(v2.getTextT());
                            border.getChildren().add(v2.getDijkstra());
                        }

                        G.addEdge(e);


                    }
                }
            }
            int x = 0;
            for(Set<Edge> c : G.getAdjList().values()){
                for(Edge e1: c){
                    x++;
                }
            }
            if( x >= 1){
                root.getChildren().add(layout.getChbox());
            }
            Dijkstra.setDisable(false);
            Delete.setDisable(false);
            Random.setDisable(true);
            OpenFile.setDisable(true);
            SaveOnFile.setDisable(false);

        });

    }

    public Button getButtonRandom(){ return Random; }

    private void setButtonSave(){
        SaveOnFile.setText("Save");
        SaveOnFile.setGraphic(new ImageView(new Image("Images/SaveFile.png")));
        SaveOnFile.setLayoutX(90);
        SaveOnFile.setLayoutY(7);
        SaveOnFile.setDisable(true);
        SaveOnFile.setOnAction(e -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);

                File file = fileChooser.showSaveDialog(primaryStage);
                if(file != null){
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bf = new BufferedWriter(fileWriter);
                    bf.write("Nodi:"+"\r\n");
                    for (Vertex v: G.getAdjList().keySet()) {
                        bf.write(v.getName()+"\r\n");
                    }
                    bf.write("Archi:\r\n");
                    for (Set<Edge> list : G.getAdjList().values()) {
                        for (Edge i: list){
                            bf.write(i.getU().getName()+"->"+i.getV().getName()+"\r\n" + i.getPeso()+"\r\n");
                        }
                    }
                    bf.close();
                    fileWriter.close();
                }
            } catch (IOException c) {
                c.printStackTrace();
            }

        });
    }

    public Button getButtonSave(){ return SaveOnFile; }

    private void setButtonHelp(){
        Help.setText("Help");
        Help.setPrefSize(70.00, 39.00);
        Help.setGraphic(new ImageView(new Image("Images/Help.png")));
        Help.setLayoutX(545);
        Help.setLayoutY(7);
        Help.setOnAction(event -> {
            Label secondLabel = new Label(" Il pulsante \"Open\" genera un grafo orientato partendo da un semplice file .txt.\n" +
                    " La sinstassi del linguaggio del file dovrà essere la seguente:\n" +
                    "\tnella prima riga ci dovrà essere scritta la parola Nodi seguita dai due punti (es \"Nodi:\"), per poi andare a capo\n" +
                    "\ta questo punto in ogni riga ci dovrà essere il nome del nodo FORMATO DA UN SOLO CARATTERE (es \"a\" riga successiva \"b\")\n" +
                    "\tdopo aver scritto l'ultimo nodo, nella riga successiva dovrà essere inserita la parola Archi seguita dai due punti (es \"Archi:\"), per poi andare a capo\n" +
                    "\tora, ad ogni riga corrisponde il nome del nodo uscente seguito da un trattino e un simbolo di maggiore (come una freccia) per poi esserci il nodo entrante (es \"a->b\")\n" +
                    "\tsotto ogni arco ci andrà un numero il quale dovrà essere un intero strettamento positivo minore di 50: esso sta per il peso che si vuole assegnare all'arco soprastante (es \"2\")\n" +
                    "\n" +
                    " Un esempio pratico di un grafo con i nodi a, b, c, con gli archi a->b, c->b rispettivamente di peso 4 e 8\n" +
                    "\n" +
                    " Nodi:\n" +
                    " a\n" +
                    " b\n" +
                    " c\n" +
                    " Archi:\n" +
                    " a->b\n" +
                    " 4\n" +
                    " c->b\n" +
                    " 8\n" +
                    "\n" +
                    " Il pulsante \"Save\" salva su un file il grafo su cui si sta lavorando: la sintassi con cui verrà salvato rispetterà le condizioni del pulsante \"Open\"\n" +
                    "\n" +
                    " Il pulsante \"Cancel\" elimina completamente il grafo su cui si sta lavorando\n" +
                    "\n" +
                    " Il pulsante \"Random\" crea un grafo casuale, facendo scegliere all'utente esclusivamente il numero di nodi e il massimo peso che ogni arco può avere\n" +
                    "\n" +
                    " Il pulsante \"Dijkstra\" esegue l'algoritmo di Dijkstra partendo da un determinato nodo\n" +
                    "\n" +
                    " Il pulsante \"Reset\" resetta il grafo se in precedenza è stato applicato Dijkstra in modo da poter riapplicare nuovamente l'algoritmo\n" +
                    "\n" +
                    " Il pulsante \"Help\" apre questa finestra");
            secondLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
            /*Pane secondaryLayout = new Pane();
            secondaryLayout.getChildren().add(secondLabel);

            Scene secondScene = new Scene(secondaryLayout, 800 , 600);

            // New window (Stage)
            Stage newWindow = new Stage();
            newWindow.getIcons().add(new Image("Images/Help.png"));
            newWindow.setTitle("Help");
            newWindow.setScene(secondScene);

            // Set position of second window, related to primary window.
            newWindow.setX(primaryStage.getX() + 200);
            newWindow.setY(primaryStage.getY() + 100);

            newWindow.show();*/
            JFrame window2 = new JFrame("Help");
            final JTextArea textArea = new JTextArea(10, 100);
            JScrollPane scroll = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            textArea.setText(secondLabel.getText());
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            window2.add(scroll);
            window2.setSize(1100, 500);
            window2.setVisible(true);
            window2.setLocationRelativeTo(null);
            window2.toFront();
        });

    }

    public Button getButtonHelp(){ return Help;}
}
