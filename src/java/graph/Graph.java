package graph;

import java.util.HashMap;

/**
 * Graph class.
 * 
 * @author mafranko
 */
public class Graph {

	/**
	 * Map <id, Vertex>. Id is the vertex number.
	 */
	private HashMap<Integer, Vertex> vertices;

	/**
	 * Map <Edge, value>. Value is the weight of the edge. 
	 */
	private HashMap<Edge, Integer> edges;

	private int maxNumOfVertices = Integer.MAX_VALUE;
	private int maxNumOfEdges = Integer.MAX_VALUE;

	public Graph() {
		vertices = new HashMap<Integer, Vertex>();
		edges = new HashMap<Edge, Integer>();
	}

	public void addVertex(int id, String team) {
		Vertex v = vertices.get(id);
		if (null == v) {
			vertices.put(id, new Vertex(id, team));
		} else {
			v.setTeam(team);
		}
	}

	public void addVertex(Vertex v) {
		Vertex vertex = vertices.get(v.getId());
		if (null == vertex) {
			vertices.put(v.getId(), v);
		} else {
			vertex.setTeam(v.getTeam());
		}
	}

	public void addEdge(int v1, int v2) {
		edges.put(new Edge(v1, v2), -1);
		Vertex vertex1 = vertices.get(v1);
		if (null == vertex1) {
			vertex1 = new Vertex(v1, "unknown");
			addVertex(vertex1);
		}
		Vertex vertex2 = vertices.get(v2);
		if (null == vertex2) {
			vertex2 = new Vertex(v2, "unknown");
			addVertex(vertex2);
		}
		vertex1.addNeighbor(vertex2);
		vertex2.addNeighbor(vertex1);
	}

	public boolean containsVertex(int id, String team) {
		if (vertices.containsKey(id)) {
			Vertex v = vertices.get(id);
			if (v.getTeam().equals(team)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsEdge(int v1, int v2) {
		return edges.containsKey(new Edge(v1, v2));
	}

	public int getVertexValue(int id) {
		if (vertices.containsKey(id)) {
			Vertex v = vertices.get(id);
			return v.getValue();
		}
		return -1;
	}

	public void addVertexValue(int id, int value) {
		if (vertices.containsKey(id)) {
			Vertex v = vertices.get(id);
			v.setValue(value);
		} else {
			Vertex v = new Vertex(id, "unknown");
			v.setValue(value);
			vertices.put(id, v);
		}
	}

	public int getEdgeValue(int v1, int v2) {
		Edge e = new Edge(v1, v2);
		if (edges.containsKey(e)) {
			return edges.get(e);
		}
		return -1;
	}

	public void addEdgeValue(int v1, int v2, int value) {
		Edge e = new Edge(v1, v2);
		if (edges.containsKey(e)) {
			edges.put(e, value);
		}
	}

	/* Getters and Setters */

	public HashMap<Integer, Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(HashMap<Integer, Vertex> vertices) {
		this.vertices = vertices;
	}

	public HashMap<Edge, Integer> getEdges() {
		return edges;
	}

	public void setEdges(HashMap<Edge, Integer> edges) {
		this.edges = edges;
	}

	public int getMaxNumOfVertices() {
		return maxNumOfVertices;
	}

	public void setMaxNumOfVertices(int maxNumOfVertexs) {
		this.maxNumOfVertices = maxNumOfVertexs;
	}

	public int getMaxNumOfEdges() {
		return maxNumOfEdges;
	}

	public void setMaxNumOfEdges(int maxNumOfEdges) {
		this.maxNumOfEdges = maxNumOfEdges;
	}
}
