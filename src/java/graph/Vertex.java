package graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Vertex class.
 * 
 * @author mafranko
 */
public class Vertex {

	private int id;
	private String team = "none";
	private int value = -1;

	private Set<Vertex> neighbors; 

	public Vertex(int id, String team) {
		this.id = id;
		this.team = team;
		neighbors = new HashSet<Vertex>();
	}

	public Vertex(int id) {
		this.id = id;
		neighbors = new HashSet<Vertex>();
	}

	public void addNeighbor(Vertex v) {
		neighbors.add(v);
	}

	public int getId() {
		return id;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object object) {
		if (null == object) {
			return false;
		}
		if (object instanceof Vertex) {
			Vertex other = (Vertex) object;
			if (this.id == other.id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return "vertex(" + id + "," + team + "," + value +")"; 
	}
}
