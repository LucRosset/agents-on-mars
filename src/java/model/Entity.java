package model;

import model.graph.Vertex;

public class Entity {

	private String name;
	private String team;
	private Vertex vertex;
	private String status;
	private String role;

	public Entity(String name) {
		this.name = name;
		this.vertex = new Vertex(-1);
	}

	public Entity(String name, String team, Vertex vertex, String status) {
		this.name = name;
		this.team = team;
		this.vertex = vertex;
		this.status = status;
	}

	/* Getters and Setters */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public Vertex getVertex() {
		return vertex;
	}

	public void setVertex(Vertex vertex) {
		this.vertex = vertex;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object object) {
		if (null == object) {
			return false;
		}
		if (object instanceof Entity) {
			Entity other = (Entity) object;
			if (this.name.equals(other.getName())) {
				return true;
			}
		}
		return false;
	}
}
