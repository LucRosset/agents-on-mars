package arch;

import env.Percept;
import graph.Graph;
import graph.Vertex;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Class used to model the scenario.
 * 
 * @author mafranko
 */
public class WorldModel {

	private Graph graph;

	private HashMap<String, Entity> opponents;
	private HashMap<String, Entity> coworkers;

	private final static String myTeam = "A";

	public WorldModel() {
		graph = new Graph();
		opponents = new HashMap<String, Entity>();
		coworkers = new HashMap<String, Entity>();
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
				String name = percept.getTerm(0).toString();
				String vertex = percept.getTerm(1).toString();
				int v = Integer.parseInt(vertex.replace("vertex", ""));
				String team = percept.getTerm(2).toString();
				team = team.replaceAll("\"", "");
				String status = percept.getTerm(3).toString();
				if (!team.equals(myTeam) && !containsOpponent(name, v, team, status)) {
					addOpponent(name, v, team, status);
					newPercepts.add(percept);
				}
				break;
			case Percept.coworkerPosition:
				String aName = percept.getTerm(0).toString();
				String position = percept.getTerm(1).toString();
				int pos = Integer.parseInt(position);
				if (!containsCoworker(aName, pos)) {
					addCoworker(aName, pos);
					newPercepts.add(percept);
				}
				break;
			case Percept.coworkerRole:
				String agName = percept.getTerm(0).toString();
				String role = percept.getTerm(1).toString();
				role = role.replaceAll("\"", "");
				Entity e = new Entity(agName);
				e.setRole(role);
				coworkers.put(agName, e);
				newPercepts.add(percept);
				break;
			default:
				newPercepts.add(percept);
			}
		}
		return newPercepts;
	}

	private void addOpponent(String name, int vertex, String team, String status) {
		Vertex v = graph.getVertices().get(vertex);
		if (null == v) {
			v = new Vertex(vertex);
			graph.addVertex(vertex, team);
		}
		Entity e = new Entity(name, team, v, status);
		opponents.put(name, e);
	}

	private boolean containsOpponent(String name, int vertex, String team, String status) {
		if (opponents.containsKey(name)) {
			Entity opponent = opponents.get(name);
			if (opponent.getVertex().getId() == vertex && opponent.getTeam().equals(team)
					&& opponent.getStatus().equals(status)) {
				return true;
			}
		}
		return false;
	}

	private void addCoworker(String name, int vertex) {
		Vertex v = graph.getVertices().get(vertex);
		if (null == v) {
			v = new Vertex(vertex);
			graph.addVertex(vertex, myTeam);
		}
		Entity coworker = coworkers.get(name);
		if (null == coworker) {
			coworker = new Entity(name);
		}
		coworker.setVertex(v);
		coworkers.put(name, coworker);
	}

	private boolean containsCoworker(String name, int vertex) {
		if (coworkers.containsKey(name)) {
			Entity coworker = coworkers.get(name);
			if (coworker.getVertex().getId() == vertex) {
				return true;
			}
		}
		return false;
	}

	public Graph getGraph() {
		return graph;
	}
}
