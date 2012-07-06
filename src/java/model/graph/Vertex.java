package model.graph;

import java.util.HashSet;
import java.util.Set;

import env.Percept;

/**
 * Vertex class.
 * 
 * @author mafranko
 */
public class Vertex {

	public final static int BLUE = 1;	// my color
	public final static int WHITE = 0;
	public final static int RED = -1;	// opponents color

	private int id;
	private String team = Percept.TEAM_UNKNOWN;
	private int value = 1;
	private int color = WHITE;

	private boolean probed = false;

	private int distance = 0;
	private Vertex parent = null;

	private int numOfCoworkers = 0;	// TODO remove if not used
	private int numOfOpponents = 0; // TODO remove if not used

	private Set<Vertex> neighbors;

	public Vertex(int id, String team) {
		this.id = id;
		team = team.replaceAll("\"", "");
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

	public void removeColor() {
		color = WHITE;
	}

	public void addDistance(int value) {
		distance += value;
	}

	/* Getters and Setters */

	public int getId() {
		return id;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		team = team.replaceAll("\"", "");
		this.team = team;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getNumOfCoworkers() {
		return numOfCoworkers;
	}

	public int getNumOfOpponents() {
		return numOfOpponents;
	}

	public Set<Vertex> getNeighbors() {
		return neighbors;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Vertex getParent() {
		return parent;
	}

	public void setParent(Vertex parent) {
		this.parent = parent;
	}

	public boolean isProbed() {
		return probed;
	}

	public void setProbed(boolean probed) {
		this.probed = probed;
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
		return "vertex(" + id + "," + team + "," + value +"," + color +")"; 
	}
}