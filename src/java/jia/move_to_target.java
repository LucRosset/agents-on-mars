package jia;

import model.graph.Graph;
import arch.MarcianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

/**
 * 
 * @author mafranko 
 */
public class move_to_target extends DefaultInternalAction {

	private static final long serialVersionUID = -6710390609224328605L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String vertex1 = ((Atom) terms[0]).getFunctor();
		vertex1 = vertex1.replace("vertex", "");
		String vertex2 = ((StringTerm) terms[1]).getString();
		vertex2 = vertex2.replace("vertex", "");
		int v1 = Integer.parseInt(vertex1);
		int v2 = Integer.parseInt(vertex2);

		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();

		if (v1 == v2) {
			String vertex = "vertex" + v1;
			return un.unifies(terms[2], ASSyntax.createString(vertex));
		}

		if (graph.existsPath(v1, v2)) {
			int nextMove = graph.returnNextMove(v1, v2);
			String vertex = "vertex" + nextMove;
			return un.unifies(terms[2], ASSyntax.createString(vertex));
		} else {
			System.out.println("Could not find a path from vertex" + v1 + "to vertex" + v2);
			int nextMove = graph.returnRandomMove(v1);
			String vertex = "vertex" + nextMove;
			return un.unifies(terms[2], ASSyntax.createString(vertex));
		}
	}
	
}
