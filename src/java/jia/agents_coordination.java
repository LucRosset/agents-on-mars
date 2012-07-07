package jia;


import java.util.HashMap;
import java.util.List;

import model.Entity;
import model.graph.Vertex;

import arch.MarcianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

public class agents_coordination extends DefaultInternalAction {

	private static final long serialVersionUID = -6858228332440013608L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		synchronized (this) {
			ListTerm positions = new ListTermImpl();
			ListTerm agents = new ListTermImpl();

			WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
			List<Vertex> bestZone = model.getBestZone();	// zone with the greatest value

			if (null == bestZone || bestZone.isEmpty()) {
				return un.unifies(terms[0], agents) & un.unifies(terms[1], positions);
			}

			List<Vertex> zoneNeighbors = model.getZoneNeighbors(bestZone);
			List<Vertex> bestNeighbors = model.getBestZoneNeighbors(zoneNeighbors);
			zoneNeighbors.removeAll(bestNeighbors);

			if (zoneNeighbors.isEmpty() && bestNeighbors.isEmpty()) {
				return un.unifies(terms[0], agents) & un.unifies(terms[1], positions);
			}

			HashMap<String, Entity> coworkers = model.getCoworkers();	// TODO order by agent type

			for (Entity coworker : coworkers.values()) {
				Vertex target = null;
				Vertex agentPosition = coworker.getVertex();
				if (bestZone.contains(agentPosition)) {	// the agent is part of the best zone
					if (model.isFrontier(agentPosition)) {
						// TODO verify if the agent can move to a neighbor without break the zone
					} else {
						if (!bestNeighbors.isEmpty()) {
							target = model.closerVertex(agentPosition, bestNeighbors);
							if (null != target) {
								bestNeighbors.remove(target);
							}
						} else if (!zoneNeighbors.isEmpty()) {
							target = model.closerVertex(agentPosition, bestNeighbors);
							if (null != target) {
								zoneNeighbors.remove(target);
							}
						}
					}
				} else {
					if (!bestNeighbors.isEmpty()) {
						target = model.closerVertex(agentPosition, bestNeighbors);
						if (null != target) {
							bestNeighbors.remove(target);
						}
					} else if (!zoneNeighbors.isEmpty()) {
						target = model.closerVertex(agentPosition, bestNeighbors);
						if (null != target) {
							zoneNeighbors.remove(target);
						}
					}
				}

				if (null != target) {
					agents.add(ASSyntax.createString(coworker.getName()));
					positions.add(ASSyntax.createString("vertex" + target.getId()));
				}
			}

			return un.unifies(terms[0], agents) & un.unifies(terms[1], positions);
		}
	}
}
