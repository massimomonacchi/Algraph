package DataStructure;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class Vertex {
    private String n;
    private Circle c;
    private Text t, Dijkstra;
    private double valForDijkstra;
    private Color color;

    public Vertex(String S) { n = S; }

    public void setValForDijkstra(double valDijkstra) { valForDijkstra = valDijkstra; }

    public void setCircle(double X, double Y, int r) { c = new Circle(X, Y, r); color = Color.BLACK;}

    public void setTextT(double sX, double sY) { t = new Text(sX, sY,  n); }

    public void setTextDijkstra(double sX, double sY) { Dijkstra = new Text(sX, sY, Integer.toString((int) valForDijkstra)); }

    public void setColor(Color c){ color = c;}

    public Color getColor(){return  color;}

    public String getName() { return n; }

    public Circle getCircle() { return c; }

    public Text getTextT() { return t; }

    public Text getDijkstra() { return Dijkstra; }

    public double getValForDijkstra() { return valForDijkstra; }
}
