/**
 *
 */
package org.cobweb.cobweb2.impl.ai;

import org.cobweb.cobweb2.core.AgentFoodCountable;
import org.cobweb.cobweb2.core.Controller;
import org.cobweb.cobweb2.core.SimulationInternals;
import org.cobweb.cobweb2.impl.ControllerParams;
import org.cobweb.cobweb2.impl.SimulationParams;
import org.cobweb.cobweb2.plugins.PerAgentParams;

/**
 * Parameters for GeneticController
 */
public class SwarmControllerParams extends PerAgentParams<SwarmStateAgentParams> implements ControllerParams {

	private final transient SimulationParams simParam;

	public static int agent_type_count = 0;

	public SwarmControllerParams(SimulationParams simParams) {
		super(SwarmStateAgentParams.class);
		this.simParam = simParams;
		agent_type_count = simParam.getAgentTypes();
		resize(simParams);
	}

	@Override
	protected SwarmStateAgentParams newAgentParam()
	{
		return null;
	}

	@Override
	protected SwarmStateAgentParams newAgentParam(int agent_type) {
		return new SwarmStateAgentParams(simParam, agent_type);
	}

	@Override
	public Controller createController(SimulationInternals sim, int agent_type) {
		SwarmController controller = new SwarmController(sim, agentParams[agent_type], agent_type_count);
		return controller;
	}

	@Override
	public void resize(AgentFoodCountable envParams) {
		super.resize(envParams);
		for(SwarmStateAgentParams i : agentParams) {
			i.resize(simParam);
		}
	}

	private static final long serialVersionUID = 2L;
}
