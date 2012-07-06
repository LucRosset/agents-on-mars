package jia;

import model.graph.Graph;
import arch.MarcianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Term;

/**
 * 
 * @author mafranko
 */
public class is_probed_vertex extends DefaultInternalAction {

	private static final long serialVersionUID = 1178501089234439800L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String vertex1 = ((Atom) terms[0]).getFunctor();
		vertex1 = vertex1.replace("vertex", "");
		int v1 = Integer.parseInt(vertex1);

		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();

		return graph.isProbedVertex(v1);
	}

}
