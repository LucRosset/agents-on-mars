package graph;

import java.util.HashMap;

/**
 * Graph class.
 * 
 * @author mafranko
 */
public class Graph {

	private HashMap<Integer, Vertex> vertexs;

	/**
	 * Map <Edge, value>. Value is the weight of the edge. 
	 */
	private HashMap<Edge, Integer> edges;

	public Graph() {
		vertexs = new HashMap<Integer, Vertex>();
		edges = new HashMap<Edge, Integer>();
	}

	public void addVertex(int id, String team) {
		Vertex v = vertexs.get(id);
		if (null == v) {
			vertexs.put(id, new Vertex(id, team));
		} else {
			v.setTeam(team);
		}
	}

	public void addVertex(Vertex v) {
		Vertex vertex = vertexs.get(v.getId());
		if (null == vertex) {
			vertexs.put(v.getId(), v);
		} else {
			vertex.setTeam(v.getTeam());
		}
	}

	public void addEdge(int v1, int v2) {
		edges.put(new Edge(v1, v2), -1);
		Vertex vertex1 = vertexs.get(v1);
		if (null == vertex1) {
			vertex1 = new Vertex(v1, "notknow");
			addVertex(vertex1);
		}
		Vertex vertex2 = vertexs.get(v2);
		if (null == vertex2) {
			vertex2 = new Vertex(v2, "notknow");
			addVertex(vertex2);
		}
		vertex1.addNeighbor(vertex2);
		vertex2.addNeighbor(vertex1);
	}

	public boolean containsVertex(int id, String team) {
		if (vertexs.containsKey(id)) {
			Vertex v = vertexs.get(id);
			if (v.getTeam().equals(team)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsEdge(int v1, int v2) {
		return edges.containsKey(new Edge(v1, v2));
	}
}
