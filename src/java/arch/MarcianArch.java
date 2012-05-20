package arch;

import jason.RevisionFailedException;
import jason.asSyntax.Literal;

import java.util.List;
import java.util.logging.Logger;

import c4jason.CAgentArch;
import env.MarsEnv;

/**
 * Common architecture for the agents.
 * 
 * @author mafranko
 */
public class MarcianArch extends CAgentArch {

	protected MarsEnv env = null;

	protected Logger logger;

	public MarcianArch() {
		super();
		env = MarsEnv.getInstance();
	}

	@Override
    public List<Literal> perceive() {
        super.perceive();
        List<Literal> eisPercepts = env.getPercepts(getAgName());
        for (Literal percept : eisPercepts) {
        	try {
				getTS().getAg().addBel(percept);
			} catch (RevisionFailedException e) {
				// e.printStackTrace();
				logger.warning("Error when adding percepts from eis to the belief base.");
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
}
