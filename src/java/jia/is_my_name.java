package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

public class is_my_name extends DefaultInternalAction {

	private static final long serialVersionUID = 4244656679284159365L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String name = ((StringTerm) terms[0]).getString();
		return ts.getUserAgArch().getAgName().equals(name);
	}
	
}
