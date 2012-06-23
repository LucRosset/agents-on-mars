package arch;

import env.Percept;
import graph.Graph;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to model the scenario.
 * 
 * @author mafranko
 */
public class WorldModel {

	private Graph graph;

	private final static String myTeam = "A";

	public WorldModel() {
		graph = new Graph();
	}

	public List<Literal> update(List<Literal> percepts) {
		List<Literal> newPercepts = new ArrayList<Literal>();
		for (Literal percept : percepts) {
			String functor = percept.getFunctor();
			switch (functor) {
			case Percept.visibleVertex:
				if (graph.getVertices().size() < graph.getMaxNumOfVertices()) {
					String vertexName = percept.getTerm(0).toString();
					int id = Integer.parseInt(vertexName.replace("vertex", ""));
					String team = percept.getTerm(1).toString();
					if (!graph.containsVertex(id, team)) {
						graph.addVertex(id, team);
						newPercepts.add(percept);
					}
				}
				break;
			case Percept.probedVertex:
				String vertexName = percept.getTerm(0).toString();
				int id = Integer.parseInt(vertexName.replace("vertex", ""));
				int vValue = (int) ((NumberTerm) percept.getTerm(1)).solve();
				if (graph.getVertexValue(id) != vValue) {
					graph.addVertexValue(id, vValue);
					newPercepts.add(percept);
				}
				break;
			case Percept.visibleEdge:
				if (graph.getEdges().size() < graph.getMaxNumOfEdges()) {
					String vertex1 = percept.getTerm(0).toString();
					String vertex2 = percept.getTerm(1).toString();
					int v1 = Integer.parseInt(vertex1.replace("vertex", ""));
					int v2 = Integer.parseInt(vertex2.replace("vertex", ""));
					if (!graph.containsEdge(v1, v2)) {
						graph.addEdge(v1, v2);
						newPercepts.add(percept);
					}
				}
				break;
			case Percept.surveyedEdge:
				String vertex1 = percept.getTerm(0).toString();
				String vertex2 = percept.getTerm(1).toString();
				int v1 = Integer.parseInt(vertex1.replace("vertex", ""));
				int v2 = Integer.parseInt(vertex2.replace("vertex", ""));
				int eValue = (int) ((NumberTerm) percept.getTerm(2)).solve();
				if (graph.getEdgeValue(v1, v2) != eValue) {
					graph.addEdgeValue(v1, v2, eValue);
					newPercepts.add(percept);
				}
				break;
			case Percept.vertices:
				int vertices = (int) ((NumberTerm) percept.getTerm(0)).solve();
				graph.setMaxNumOfVertices(vertices);
				break;
			case Percept.edges:
				int edges = (int) ((NumberTerm) percept.getTerm(0)).solve();
				graph.setMaxNumOfEdges(edges);
				break;
			case Percept.visibleEntity:
				// TODO
				break;
			default:
				newPercepts.add(percept);
			}
		}
		return newPercepts;
	}

	public Graph getGraph() {
		return graph;
	}
}
