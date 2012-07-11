package arch;

import jason.RevisionFailedException;
import jason.asSemantics.ActionExec;
import jason.asSemantics.Message;
import jason.asSyntax.Literal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

import c4jason.CAgentArch;
import cartago.Op;
import env.MarsEnv;
import env.Percept;

/**
 * Common architecture for the agents.
 * 
 * @author mafranko
 */
public class MarcianArch extends CAgentArch {

	private MarsEnv env;
	private WorldModel model;

	private Logger logger;

	public MarcianArch() {
		super();
		logger = Logger.getLogger("MarcianArch");
		env = MarsEnv.getInstance();
		model = new WorldModel();
	}

	@Override
    public List<Literal> perceive() {
        super.perceive();
        List<Literal> eisPercepts = env.getPercepts(getAgName());
//        if (!eisPercepts.isEmpty()) {
//        	logger.info("[" + getAgName() + "] Percepts: " + eisPercepts);
//        }
        eisPercepts = model.update(eisPercepts);
        for (Literal percept : eisPercepts) {
        	try {
        		// broadcast only new percepts
        		String  p = percept.getFunctor();
        		if (p.equals("visibleEdge") || p.equals("visibleEntity")
        				|| p.equals("visibleVertex") || p.equals("probedVertex")
        				|| p.equals("surveyedEdge")) {
//        			if (null == getTS().getAg().getBB().contains(percept)) {
           			 	Message m = new Message("tell", null, null, percept);
           			 	broadcast(m);
//        			}
        		} else if (p.equals("position")) {
        			if (null == getTS().getAg().getBB().contains(percept)) {
        				Message m = new Message("tell", null, null,
        						Percept.coworkerPosition + "(" + getAgName() + "," +
        								percept.getTerm(0).toString() + ")");
        				broadcast(m);

        				// add percept to the base
        				getTS().getAg().addBel(percept);
        			}
        		} else if (p.equals("role")) {
        				Message m = new Message("tell", null, null,
        						Percept.coworkerRole + "(" + getAgName() + "," +
        								percept.getTerm(0).toString() + ")");
        				broadcast(m);

        				// add percept to the base
        				getTS().getAg().addBel(percept);
        		} else {
        			// TODO maybe not all percepts need to be added to the base
            		// add percept to the base
    				getTS().getAg().addBel(percept);
        		}
			} catch (RevisionFailedException e) {
				// e.printStackTrace();
				logger.warning("Error when adding percepts from eis to the belief base.");
			} catch (Exception e) {
				// e.printStackTrace();
				logger.warning("Error on perceive.");
			}
        }

        /*
		 * THE METHOD MUST RETURN NULL:
		 * since the percept semantics is different (event vs. state),
		 * all the the percepts from the env must be managed here, not by the BUF.
		 * 
		 * see CAgentArch.java
		 */
        return null;
    }

	@Override
	public void act(ActionExec actionExec, List<ActionExec> feedback) {
		String action = actionExec.getActionTerm().getFunctor();
		if (action.equals("skip") || action.equals("goto") || action.equals("probe")
				|| action.equals("survey") || action.equals("buy") || action.equals("recharge")
				|| action.equals("attack") || action.equals("repair") || action.equals("parry")) {
			boolean result = env.executeAction(this.getAgName(), actionExec.getActionTerm());
			actionExec.setResult(result);
			if (result) {
				Op op = new Op(action);
				notifyActionSuccess(op, actionExec.getActionTerm(), actionExec);
			} else {
				notifyActionFailure(actionExec, null, "Failled to performe the action: " + action);
			}
		} else {
			super.act(actionExec, feedback);
		}
	}

	@Override
    public void checkMail() {
		super.checkMail();
		List<Literal> percepts = convertMessageQueueToLiteralList(getTS().getC().getMailBox());
		model.update(percepts);

		Iterator<Message> im = getTS().getC().getMailBox().iterator();
		while (im.hasNext()) {
			Message message  = im.next();
			Literal  percept = Literal.parseLiteral(message.getPropCont().toString());
			String  p = percept.getFunctor();

//            String  ms = message.getPropCont().toString();
//            logger.info("[" + getAgName() + "] receved mail: " + ms);

			if (p.equals("visibleEdge") || p.equals("visibleEntity")
    				|| p.equals("visibleVertex") || p.equals("probedVertex")
    				|| p.equals("surveyedEdge") || p.equals("saboteur")) {
				im.remove();
			}
		}

	}

	private List<Literal> convertMessageQueueToLiteralList(Queue<Message> messages) {
		List<Literal> literals = new ArrayList<Literal>();
		for (Message message : messages) {
			Literal  p = Literal.parseLiteral(message.getPropCont().toString());
			literals.add(p);
		}
		return literals;
	}

	public WorldModel getModel() {
		return this.model;
	}
}
