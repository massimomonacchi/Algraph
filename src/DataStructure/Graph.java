package DataStructure;
import java.util.*;

public class Graph {

        private Map<Vertex, Set<Edge>> adjList;
        private boolean stateDijkstra;

        // Costruttore
        public Graph() {
            adjList = new HashMap<>();
            stateDijkstra = false;
        }


        // Funzione che mi aggiunge un vertice al grafo
        public void addVertex(Vertex v) {
            adjList.putIfAbsent(v, new HashSet<>());
        }


        // Funzione che mi rimuove un vertice dal grafo
        public void removeVertex(Vertex v) {
            adjList.remove(v);
        }


        // Funzione che mi aggiunge un arco al grafo
        public void addEdge(Edge e){ adjList.get(e.getU()).add(e); }


        // Funzione che mi rimuove un arco dal grafo
        public void removeEdge(Edge e){ adjList.get(e.getU()).remove(e); }


        // Funzione che mi controlla se esiste un determinato arco
        public boolean checkIfEdgeIsPresent(Edge e){
            if(!adjList.get(e.getU()).isEmpty()){
                Set<Edge> E = adjList.get(e.getU());
                for(Edge edge: E){
                    if(edge.getHash().compareTo(e.getHash()) == 0){
                        return true;
                    }
                }
            }
            return false;
        }


        // Funzione che dato un arco, mi restituisce true se esiste l'arco inverso, false altrimenti
        public boolean getInvertedEdge(Edge e){
            if(!adjList.get(e.getV()).isEmpty()) {
                Set<Edge> E = adjList.get(e.getV());
                String nameInverted = e.getV().getName() + e.getU().getName();
                for (Edge inverted : E) {
                    if (nameInverted.compareTo(inverted.getHash()) == 0) {
                        return true;
                    }
                }
            }
            return false;
        }


        // Funzione che data una stringa, mi restituisce il vertice corrispondente al nome, null altrimenti
        public Vertex getFromSet(String s){
            for(Vertex V: adjList.keySet()){
                if(s.compareTo(V.getName()) == 0){
                    return V;
                }
            }
            return null;
        }

        public Map<Vertex, Set<Edge>> getAdjList() { return adjList; }

        public void setStateDijkstraOn() { stateDijkstra = true; }

        public void setStateDijkstraOff() { stateDijkstra = false; }

        public boolean getStateDijkstra() { return stateDijkstra; }
}
