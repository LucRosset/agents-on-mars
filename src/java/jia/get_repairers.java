package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

import java.util.List;

import model.Entity;
import arch.MarcianArch;
import arch.WorldModel;

public class get_repairers extends DefaultInternalAction {

	private static final long serialVersionUID = -3080118459421331948L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		ListTerm repairers = new ListTermImpl();
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		List<Entity> agents = model.getCoworkersByRole("repairer");
		for (Entity agent : agents) {
			repairers.add(ASSyntax.createString(agent.getName()));
		}
		return un.unifies(terms[0], repairers);
	}
}
