package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;
import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;

public class agent_position extends DefaultInternalAction {

	private static final long serialVersionUID = -1524646971431382627L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String agentName = ((StringTerm) terms[0]).getString();
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Vertex v = model.getCoworkerPosition(agentName);
		if (null == v) {
			return un.unifies(terms[1], new Atom("null"));
		}
		return un.unifies(terms[1], new Atom("vertex" + v.getId()));
	}
}
