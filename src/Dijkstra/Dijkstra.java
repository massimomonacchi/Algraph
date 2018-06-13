package Dijkstra;

import DataStructure.Edge;
import DataStructure.Graph;
import DataStructure.PriorityQueue;
import DataStructure.Vertex;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.*;

public class Dijkstra {

    boolean primo = true; boolean secondo = false; boolean terzo = false; boolean Quarto = false;
    int x = 0;
    Vertex v1 = null;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double y = ((screenSize.getHeight() /8 ) /2);
    double dim = (screenSize.getHeight() - (screenSize.getHeight() / 8) - y) / 16;

    LinkedList<Edge> E = new LinkedList<>();
    Label lbl2 = new Label("Priority\nQueue");
    Rectangle rt2 = new Rectangle(80,dim);
    PriorityQueue Q;
    javafx.scene.control.TextArea txtArea;

    public  Rectangle getRectangle(){ return rt2; }

    public Label getLabel(){ return lbl2; }



    public void dijkstra(Group p, Graph G, String firstV, LinkedList<Line> listLine, Map<String, Vertex> Ac, Button step, Button end, javafx.scene.control.TextArea txt) {
        double Width = screenSize.width / 5 * 3;
        p.getChildren().addAll(step, end);
        Q = new PriorityQueue();
        txtArea = txt;


        for(int i = 0; i < G.getAdjList().size()+2; i ++){
            if(i == 0){
                rt2.setLayoutX((Width+Width/3) - 100);
                rt2.setLayoutY(y);
                rt2.setFill(Color.RED);
                lbl2.setLayoutX((Width+Width/3) - 80);
                lbl2.setLayoutY(y + 2);
                p.getChildren().addAll(rt2, lbl2);
            }

            if(i == G.getAdjList().size() + 1){
                Line l6 = new Line((Width+Width/3) - 100, ((screenSize.getHeight() /8 ) /2), (Width+Width/3) - 100, y);
                Line l7 = new Line((Width+Width/3) - 20, ((screenSize.getHeight() /8 ) /2), (Width+Width/3)  - 20, y);

                listLine.add(l6);
                listLine.add(l7);
                p.getChildren().addAll(l6,l7);
            }
            Line l = new Line((Width+Width/3) - 100 , y, (Width+Width/3) - 20, y);
            listLine.add(l);
            p.getChildren().add(l);
            y = y + dim;
        }


        for (Vertex v : G.getAdjList().keySet()) {
            if (v.getName().compareTo(firstV) != 0) {
                v.setValForDijkstra(Double.POSITIVE_INFINITY);
                v.getDijkstra().setX(v.getDijkstra().getX());
                v.getDijkstra().setText(Character.toString('\u221E'));
            }
            v.getDijkstra().setFill(Color.WHITE);
        }

        y = ((screenSize.getHeight() /8 ) /2);

        step.setOnAction(event -> {
            if (primo) {
                Q.getPriorityQueue().add(G.getFromSet(firstV));
                Ac.put(firstV, new Vertex(firstV));
                Ac.get(firstV).setCircle((Width + Width / 3) - 60, y + dim + dim / 2, 15);
                Ac.get(firstV).getCircle().setFill(Color.BLACK);
                Ac.get(firstV).setTextT(Ac.get(firstV).getCircle().getCenterX() - 4, Ac.get(firstV).getCircle().getCenterY());
                Ac.get(firstV).getTextT().setFill(Color.WHITE);
                p.getChildren().addAll(Ac.get(firstV).getCircle(), Ac.get(firstV).getTextT());

                txtArea.setText("- Inserimento primo nodo: "+ firstV);
                txtArea.appendText("");
                primo = false;
                secondo = true;
                y += dim + (dim / 2);
                return;
            }


            if (secondo) {
                if (!Q.getPriorityQueue().isEmpty()) {
                    v1 = Q.deleteMin();
                    v1.getDijkstra().setFill(Color.WHITE);
                    v1.setColor(Color.ORANGE);
                    v1.getCircle().setFill(Color.ORANGE);

                    txtArea.setText(txtArea.getText()+"\n - Lavoro sul nodo: " + v1.getName());
                    txtArea.appendText("");
                    Ac.get(v1.getName()).getCircle().setFill(Color.ORANGE);
                    E.clear();
                    x = 0;
                    for (Edge e1 : G.getAdjList().get(v1)) {
                        E.add(e1);
                    }
                    terzo = true;
                    secondo = false;
                }
                else{
                    v1.setColor(Color.GREEN);
                    v1.getCircle().setFill(v1.getColor());
                    p.getChildren().removeAll(Ac.get(v1.getName()).getCircle(), Ac.get(v1.getName()).getTextT());
                    p.getChildren().remove(step);
                    for (Vertex v : G.getAdjList().keySet()) {
                        if (v.getValForDijkstra() == Double.POSITIVE_INFINITY) {
                            v.setColor(Color.RED);
                            v.getCircle().setFill(v.getColor());
                        }
                    }
                    txtArea.setText(txtArea.getText()+"\n - Fine Dijkstra");
                    txtArea.appendText("");
                    p.getChildren().removeAll(step, end);
                }
                return;
            }


            if (terzo) {
                if (!E.isEmpty()) {
                    Edge e = E.get(x);
                    x++;
                    if (e.getV().getValForDijkstra() == Double.POSITIVE_INFINITY) {
                        Q.getPriorityQueue().add(e.getV());
                        y += dim;
                        Ac.put(e.getV().getName(), new Vertex(e.getV().getName()));
                        Ac.get(e.getV().getName()).setCircle((Width + Width / 3) - 60, y, 15);
                        Ac.get(e.getV().getName()).getCircle().setFill(Color.BLACK);
                        Ac.get(e.getV().getName()).setTextT(Ac.get(e.getV().getName()).getCircle().getCenterX() - 4, Ac.get(e.getV().getName()).getCircle().getCenterY());
                        Ac.get(e.getV().getName()).getTextT().setFill(Color.WHITE);
                        txtArea.setText(txtArea.getText()+"\n - Aggiunta in coda di priorità nodo: " + e.getV().getName());
                        txtArea.appendText("");
                        p.getChildren().addAll(Ac.get(e.getV().getName()).getCircle(), Ac.get(e.getV().getName()).getTextT());
                    }
                    terzo = false;
                    Quarto = true;
                } else {
                    secondo = true;
                    terzo = false;

                    if (v1 != null) {
                        v1.getCircle().setFill(Color.GREEN);
                        v1.setColor(Color.GREEN);
                    }

                    txtArea.setText(txtArea.getText()+"\n - Archi esauriti\n - Lavoro nodo "+ v1.getName() +" completato" );
                    txtArea.appendText("");

                    p.getChildren().remove(Ac.get(v1.getName()).getCircle());
                    p.getChildren().remove(Ac.get(v1.getName()).getDijkstra());
                }
                return;
            }

            if (Quarto) {
                Edge e = E.get(x - 1);
                if ((e.getU().getValForDijkstra() + e.getPeso()) < e.getV().getValForDijkstra()) {
                    e.getV().setValForDijkstra(e.getU().getValForDijkstra() + e.getPeso());
                    e.getV().getDijkstra().setText(Integer.toString((int) e.getV().getValForDijkstra()));
                    switch (Integer.toString((int) e.getV().getValForDijkstra()).length()) {
                        case 1:
                            e.getV().getDijkstra().setX(e.getV().getCircle().getCenterX() - 3.2);
                            break;
                        case 2:
                            e.getV().getDijkstra().setX(e.getV().getCircle().getCenterX() - 6.4);
                            break;
                        case 3:
                            e.getV().getDijkstra().setX(e.getV().getCircle().getCenterX() - 9.6);
                            break;
                        case 4:
                            e.getV().getDijkstra().setX(e.getV().getCircle().getCenterX() - 12.8);
                            break;
                    }
                    txtArea.setText(txtArea.getText()+"\n - Peso settato\n     Nodo: "+ e.getV().getName()+"\n     Peso: "+(int)e.getV().getValForDijkstra() );
                    txtArea.appendText("");
                    G.getFromSet(e.getV().getName()).getDijkstra().setFill(Color.RED);
                }else {
                    txtArea.setText(txtArea.getText()+"\n - Peso di "+ e.getV().getName() +" non settato ("+(e.getU().getValForDijkstra() + e.getPeso())+" > " +e.getV().getValForDijkstra()+")" );
                    txtArea.appendText("");
                }

                if (x == E.size()) {
                    if (v1 != null) {
                        v1.getCircle().setFill(Color.GREEN);
                        v1.setColor(Color.GREEN);
                    }

                    txtArea.setText(txtArea.getText()+"\n - Archi esauriti\n - Lavoro nodo "+ v1.getName() +" completato" );
                    txtArea.appendText("");
                    p.getChildren().remove(Ac.get(v1.getName()).getCircle());
                    p.getChildren().remove(Ac.get(v1.getName()).getDijkstra());
                    secondo = true;
                    terzo = false;
                }
                else{
                    terzo = true;
                }
                Quarto = false;
                return;
            }
        });


        end.setOnAction(event -> {
            txtArea.setText("");
            for (Vertex v: Ac.values()){
                p.getChildren().remove(v.getCircle());
            }
            for (Vertex v: G.getAdjList().keySet()){
                if (v.getName().compareTo(firstV) != 0) {
                    v.setValForDijkstra(Double.POSITIVE_INFINITY);
                }
            }

             Q = new PriorityQueue();
            Q.getPriorityQueue().add(G.getFromSet(firstV));

            txtArea.setText("- Inserimento primo nodo: "+ firstV);
            txtArea.appendText("");

            while (!Q.getPriorityQueue().isEmpty()) {
                Vertex v1 = Q.deleteMin();

                txtArea.setText(txtArea.getText()+"\n - Lavoro sul nodo: " + v1.getName());
                txtArea.appendText("");
                for (Edge e : G.getAdjList().get(v1)) {
                    if (e.getV().getValForDijkstra() == Double.POSITIVE_INFINITY) {
                        Q.getPriorityQueue().add(e.getV());
                        txtArea.setText(txtArea.getText()+"\n - Aggiunta in coda di priorità nodo: " + e.getV().getName());
                        txtArea.appendText("");
                    }
                    if ((e.getU().getValForDijkstra() + e.getPeso()) < e.getV().getValForDijkstra()) {
                        e.getV().setValForDijkstra(e.getU().getValForDijkstra() + e.getPeso());
                        e.getV().getDijkstra().setText(Integer.toString((int) e.getV().getValForDijkstra()));
                        switch (Integer.toString((int) e.getV().getValForDijkstra()).length()) {
                            case 1:
                                e.getV().getDijkstra().setX(e.getV().getCircle().getCenterX() - 3.2);
                                break;
                            case 2:
                                e.getV().getDijkstra().setX(e.getV().getCircle().getCenterX() - 6.4);
                                break;
                            case 3:
                                e.getV().getDijkstra().setX(e.getV().getCircle().getCenterX() - 9.6);
                                break;
                            case 4:
                                e.getV().getDijkstra().setX(e.getV().getCircle().getCenterX() - 12.8);
                                break;
                        }
                        txtArea.setText(txtArea.getText()+"\n - Peso settato\n     Nodo: "+ e.getV().getName()+"\n     Peso: "+(int)e.getV().getValForDijkstra() );
                        txtArea.appendText("");
                    }else {
                        txtArea.setText(txtArea.getText()+"\n - Peso di "+ e.getV().getName() +" non settato ("+(e.getU().getValForDijkstra() + e.getPeso())+" > " +e.getV().getValForDijkstra()+")" );
                        txtArea.appendText("");
                    }

                }
                txtArea.setText(txtArea.getText()+"\n - Archi esauriti\n - Lavoro nodo "+ v1.getName() +" completato" );
                txtArea.appendText("");
            }
            for (Vertex v : G.getAdjList().keySet()){
                v.getDijkstra().setFill(Color.WHITE);
                if(v.getValForDijkstra() == Double.POSITIVE_INFINITY){
                    v.setColor(Color.RED);
                    v.getCircle().setFill(v.getColor());
                }
                else{
                    v.setColor(Color.GREEN);
                    v.getCircle().setFill(v.getColor());
                }
            }

            txtArea.setText(txtArea.getText()+"\n - Fine Dijkstra");
            txtArea.appendText("");
            p.getChildren().removeAll(step, end);
        });
    }
}
