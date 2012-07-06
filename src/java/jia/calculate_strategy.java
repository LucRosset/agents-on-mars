package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

import java.util.List;

import model.graph.Vertex;

import arch.MarcianArch;
import arch.WorldModel;

/**
 * Returns the neighbors of the best zone.
 * </p>
 * Use: jia.calculate_strategy(-Y); </br>
 * Where: Y is the list of neighbors vertices of the best zone.
 * 
 * @author mafranko
 */
public class calculate_strategy extends DefaultInternalAction {

	private static final long serialVersionUID = -4427251890408442543L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		List<Vertex> bestZone = model.getBestZone();	// zone with the greatest value
		List<Vertex> zoneNeighbors = model.getZoneNeighbors(bestZone);

		ListTerm pos = new ListTermImpl();
		for (Vertex v : zoneNeighbors) {
			pos.add(ASSyntax.createString("vertex" + v.getId()));
		}
		return un.unifies(terms[0], pos);
	}

}
