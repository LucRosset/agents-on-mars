package arch;

import jason.asSyntax.Literal;
import jason.infra.centralised.CentralisedEnvironment;

import java.util.List;
import java.util.logging.Level;

import c4jason.CAgentArch;
import env.MarsEnv;

/**
 * Common architecture for the agents.
 * 
 * @author mafranko
 */
public class MarcianArch extends CAgentArch {

	protected MarsEnv env = null;

	public MarcianArch() {
		super();
		env = MarsEnv.getInstance();
	}

	@Override
    public List<Literal> perceive() {
        List<Literal> cartagoPercepts = super.perceive();
        List<Literal> eisPercepts = env.getPercepts(getAgName());

        List<Literal> percepts = null;
        if (null != cartagoPercepts) {
        	percepts = cartagoPercepts;
        }
        if (null != eisPercepts) {
        	if (null == percepts) {
        		percepts = eisPercepts;
        	} else {
        		percepts.addAll(eisPercepts);
        	}
        }

        if (logger.isLoggable(Level.FINE) && percepts != null) logger.fine("percepts: " + percepts);
        return percepts;
    }
}
