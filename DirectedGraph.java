///////////////////////////////////////////////////////////////////////////////
// Main Class File:  TheGame.java
// File:             DirectedGraph.java
// Semester:         CS367 Fall 2015
//
// Author:           Fuhito Yoshida
// CS Login:         fuhito
// Lecturer's Name:  Jim Skrentny 
//
///////////////////////////////////////////////////////////////////////////////
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class implements GraphADT with an adjacency lists representation.
 * The graph has no loops, and each edge will be a directed edge.
 *
 * @author Fuhito Yoshida
 */
public class DirectedGraph<V> implements GraphADT<V>{
	//graph
	private HashMap<V, ArrayList<V>> hashmap;
	//DO NOT ADD ANY OTHER DATA MEMBERS

	/**
	 * Constructor that creates an empty graph.
	 */
	public DirectedGraph() {
		this(new HashMap<V, ArrayList<V>>());
	}

	/**
	 * Creates a graph from a preconstructed hashmap.
	 * @param (hashmap) preconstructed hashmap
	 */
	public DirectedGraph(HashMap<V, ArrayList<V>> hashmap) {
		if(hashmap == null) throw new IllegalArgumentException();
		this.hashmap = hashmap;
	}

	/**
	 * Adds the specified vertex to this graph if not already present. More
	 * If this graph already contains such vertex, the call leaves this graph
	 * unchanged and return false.
	 * 
	 * @param (vertex) vertex that we are trying to add.
	 * @return return true if we have successfully added. Otherwise return
	 * false.
	 */
	@Override
	public boolean addVertex(V vertex) {
		if(vertex == null) throw new IllegalArgumentException();
		//if graph already contains the vertex
		if(hashmap.containsKey(vertex)) return false;

		hashmap.put(vertex, new ArrayList<V>());
		return true;
	}

	/**
	 * Creates a new edge from vertex1, vertex2, and return true. If v1 and
	 * v2 are not the same vertex and an edge does not already exist from
	 * v1 to v2.
	 * 
	 * @param (v1, v2) vertexes that we are using to create edge between them.
	 * @return return true if we have successfully added. 
	 */
	@Override
	public boolean addEdge(V v1, V v2) {
		if(v1 == null || v2 == null) throw new IllegalArgumentException();
		//if graph does not have these two vertices
		if(!hashmap.containsKey(v1) || !hashmap.containsKey(v2)) throw new 
		IllegalArgumentException(); 
		//if v1 and v2 are the same vertex
		if(v1.equals(v2)) return false;

		ArrayList<V> v1Neighbors = hashmap.get(v1);
		//if an edge already exist
		if(v1Neighbors.contains(v2)) return false;

		v1Neighbors.add(v2);
		return true;
	}

	/**
	 * Returns a set of all vertices to which there are outward edges from v.
	 * @param (vertex) we are trying to find vertex's neighbors.
	 * @return return set of neighbors. 
	 */
	@Override
	public Set<V> getNeighbors(V vertex) {
		if(vertex == null) throw new IllegalArgumentException();
		//if graph does not contain the vertex
		if(!hashmap.containsKey(vertex)) throw new IllegalArgumentException();

		Set<V> neighbors = new HashSet<V>();
		ArrayList<V> list = hashmap.get(vertex);
		Iterator<V> itr = list.iterator();
		while(itr.hasNext()) neighbors.add(itr.next());
		return neighbors;

	}

	/**
	 * Remove edge between v1 and v2. If both v1 and v2 exist in the graph,
	 * and an edge exits from v1 to v2. 
	 * @param (v1, v2) we are trying to remove edge between v1 and v2
	 * @return return true if we have successfully added.
	 */
	@Override
	public void removeEdge(V v1, V v2) {
		if(v1 == null || v2 == null) throw new IllegalArgumentException();
		//if graph does not contain two vertices
		if(!hashmap.containsKey(v1) || !hashmap.containsKey(v2)) return;
		//if there is no edge between the vertices
		if(hashmap.get(v1).contains(v2)){
			hashmap.get(v1).remove(v2);
		}
	}

	/**
	 * Returns a set of all the vertices in the graph.
	 * @return return set of all vertices.
	 */
	@Override
	public Set<V> getAllVertices() {
		return hashmap.keySet();

	}

	@Override
	//Returns a String that depicts the Structure of the Graph
	//This prints the adjacency list
	//This has been done for you
	//DO NOT MODIFY
	public String toString() {
		StringWriter writer = new StringWriter();
		for (V vertex: this.hashmap.keySet()) {
			writer.append(vertex + " -> " + hashmap.get(vertex) + "\n");
		}
		return writer.toString();
	}

}
