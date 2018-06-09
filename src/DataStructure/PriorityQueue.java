package DataStructure;
import java.util.ArrayList;

public class PriorityQueue {
    private ArrayList<Vertex> p;

    public PriorityQueue() { p = new ArrayList<>(); }

    public Vertex deleteMin(){
        Vertex v = p.get(0);
        for (int i = 1; i < p.size(); i++) {
            if (p.get(i).getValForDijkstra() < v.getValForDijkstra()) {
                v = p.get(i);
            }
        }
        p.remove(v);
        return v;
    }


    public ArrayList<Vertex> getPriorityQueue() { return p; }
}
