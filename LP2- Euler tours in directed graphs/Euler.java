/** Starter code for LP2
 *  @author rbk ver 1.0
 *  @author SA ver 1.1
 */

// change to your netid
package idsa;

import idsa.Graph.Vertex;
import idsa.Graph.Edge;
import idsa.Graph.GraphAlgorithm;
import idsa.Graph.Factory;
import idsa.Graph.Timer;

import java.util.Iterator;
import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;


public class Euler extends GraphAlgorithm<Euler.EulerVertex> {
    static int VERBOSE = 1;
    Vertex start;
    List<Vertex> tour;
    
	// You need this if you want to store something at each node
    static class EulerVertex implements Factory {
	        

        EulerVertex(Vertex u) {
	    
        }
	public EulerVertex make(Vertex u) { return new EulerVertex(u); }

    }

    // To do: function to find an Euler tour
	public Euler(Graph g, Vertex start) {
	super(g, new EulerVertex(null));
	this.start = start;
	
	tour = new LinkedList<>();
    }

    /* To do: test if the graph is Eulerian.
     * If the graph is not Eulerian, it prints the message:
     * "Graph is not Eulerian" and one reason why, such as
     * "inDegree = 5, outDegree = 3 at Vertex 37" or
     * "Graph is not strongly connected"
     */

    public boolean isEulerian() {
    	Vertex[] vertices;
    	vertices = g.getVertexArray();
    	for(int i = 0; i < vertices.length; i++) {
    		if(vertices[i].inDegree() != vertices[i].outDegree()) {
    			System.out.println("Indegree = "+ vertices[i].inDegree() + " Outdegree = " + vertices[i].outDegree() + "at vertex" + i+1);
    			return false;
    		}
    	}
		return isSCC();
	}
    
    public boolean isSCC(){
    	boolean visited[] = new boolean[g.size()];
    	DFS(start, visited);
    	for(boolean i: visited)
    		if(i == false) {
    			System.out.println(" Graph is not Strongly Connected");
    			return false;
    		}
    	for(boolean j: visited)
    		j = false;
    	g.reverseGraph();
    	DFS(start,visited);
    	for(boolean k: visited)
    		if(k == false) {
    			System.out.println(" Graph is not Strongly connected");
    			return false;
    		}
    	return true;
    }
    
    public void DFS(Vertex start, boolean[] visited) {
    	visited[start.getIndex()] = true;
    	for(Edge x: g.incident(start)) {
    		Vertex next = x.otherEnd(g.getVertex(start));
    		if(!visited[next.getIndex()]) {
    			DFS(next,visited);
    		}
    	}
    }


    public List<Vertex> findEulerTour() {
	if(!isEulerian()) { return new LinkedList<Vertex>(); }
       // Graph is Eulerian...find the tour and return tour
	return tour;
    }
    
    public static void main(String[] args) throws Exception {
        Scanner in;
        if (args.length > 1) {
            in = new Scanner(System.in);
        } else {
	    String input = "9 13 1 2 1 2 3 1 3 1 1 3 4 1 4 5 1 5 6 1 6 3 1 4 7 1 7 8 1 8 4 1 5 7 1 7 9 1 9 5 1";
            in = new Scanner(input);
        }
	int start = 1;
        if(args.length > 1) {
	    start = Integer.parseInt(args[1]);
		}
		if(args.length > 2) {
            VERBOSE = Integer.parseInt(args[2]);
        }
        Graph g = Graph.readDirectedGraph(in);
	Vertex startVertex = g.getVertex(start);
        Timer timer = new Timer();


	Euler euler = new Euler(g, startVertex);
	System.out.println(euler.isEulerian());
	List<Vertex> tour = euler.findEulerTour();
	
		
        timer.end();
        if(VERBOSE > 0) {
	    System.out.println("Output:");
            // print the tour as sequence of vertices (e.g., 3,4,6,5,2,5,1,3)
	    System.out.println();
        }
        System.out.println(timer);

	
    }

    public void setVerbose(int ver) {
	VERBOSE = ver;
    }
}