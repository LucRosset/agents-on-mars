package jia;

import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

public class agent_server_id extends DefaultInternalAction {

	private static final long serialVersionUID = 4384102918958466437L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String agentName = ((StringTerm) terms[0]).getString();
		agentName = agentName.replace("marcian", "");
		agentName = WorldModel.myTeam.toLowerCase() + agentName;
		return un.unifies(terms[1], ASSyntax.createString(agentName));
	}
}
