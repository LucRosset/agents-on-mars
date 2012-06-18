package arch;

import env.Percept;
import graph.Graph;
import jason.asSyntax.Literal;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to model the scenario.
 * 
 * @author mafranko
 */
public class WorldModel {

	private Graph graph;

	public WorldModel() {
		graph = new Graph();
	}

	public List<Literal> update(List<Literal> percepts) {
		List<Literal> newPercepts = new ArrayList<Literal>();
		for (Literal percept : percepts) {
			String functor = percept.getFunctor();
			switch (functor) {
			case Percept.visibleVertex:
				String vertexName = percept.getTerm(0).toString();
				int id = Integer.parseInt(vertexName.replace("vertex", ""));
				String team = percept.getTerm(1).toString();
				if (!graph.containsVertex(id, team)) {
					graph.addVertex(id, team);
					newPercepts.add(percept);
				}
				break;
			case Percept.visibleEdge:
				String vertex1 = percept.getTerm(0).toString();
				String vertex2 = percept.getTerm(1).toString();
				int v1 = Integer.parseInt(vertex1.replace("vertex", ""));
				int v2 = Integer.parseInt(vertex2.replace("vertex", ""));
				if (!graph.containsEdge(v1, v2)) {
					graph.addEdge(v1, v2);
					newPercepts.add(percept);
				}
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
