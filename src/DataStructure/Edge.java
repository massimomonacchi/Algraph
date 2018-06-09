package DataStructure;

import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Edge {
    private Vertex u, v;
    private int peso;
    private Line l;
    private Text t;

    public Edge(Vertex U, Vertex V, int p){
        u = U;
        v = V;
        peso = p;
    }

    public void setPeso(int p) { peso = p; }

    public void setLine(double sX, double sY, double eX, double eY){ l = new Line(sX, sY, eX, eY); }

    public void setText(double sX, double sY) { t = new Text(sX, sY,  u.getName() + "->" + v.getName() + "  |  " + (Integer.toString(peso))); }

    public String getHash() { return u.getName() + v.getName(); }

    public Vertex getU() { return u; }

    public Vertex getV() { return v; }

    public int getPeso() { return peso; }

    public Line getLine() { return l; }

    public Text getText() { return t; }
}
