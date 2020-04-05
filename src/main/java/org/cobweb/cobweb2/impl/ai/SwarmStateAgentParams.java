package org.cobweb.cobweb2.impl.ai;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cobweb.cobweb2.SimulationConfig;
import org.cobweb.cobweb2.impl.SimulationParams;
import org.cobweb.cobweb2.plugins.swarm.SwarmAgentParams;
import org.cobweb.io.ConfDisplayFormat;
import org.cobweb.io.ConfDisplayName;
import org.cobweb.io.ConfMap;
import org.cobweb.io.ConfXMLTag;
import org.cobweb.io.ParameterSerializable;

public class SwarmStateAgentParams implements ParameterSerializable {

	private static final long serialVersionUID = -6295295048720208502L;

	/**
	 * Random seed used to initialize the behaviour array.
	 */
	@ConfDisplayName("AI Random Seed")
	@ConfXMLTag("RandomSeed")
	public long randomSeed = 42;

	@ConfDisplayName("Mutation Rate")
	@ConfXMLTag("MutationRate")
	public float mutationRate = 0.05f;

	/**
	 * Size of agent's memory in bits.
	 */
	@ConfDisplayName("Memory bits")
	@ConfXMLTag("MemoryBits")
	public int memoryBits = 2;

	/**
	 * Size of communication message in bits.
	 */
	@ConfDisplayName("Communication bits")
	@ConfXMLTag("CommunicationBits")
	public int communicationBits = 2;

	@ConfDisplayName("Individuality")
	@ConfXMLTag("Individuality")
	public float individuality = 0.25f;

	//ConfDisplayFormat("%2$s bias")
	@ConfDisplayFormat("%2$s")
	@ConfXMLTag("StateSize")
	@ConfMap(entryName = "State", keyName = "Name", valueClass = Float.class)
	public Map<String, Float> stateSizes = new LinkedHashMap<String, Float>();

	static final String STATE_NAME_SWARM_BENEFIT = "Swarm %d Benefit"; // from SwarmParams of the swarm plugin
	public SwarmAgentParams[] swarmAgentParams = null;
	public int agent_type = 0;

	public SwarmStateAgentParams(SimulationParams simParam, int agent_type) {
		this.agent_type = agent_type;
		resize(simParam);
	}

	public void resize(SimulationParams simParam) {
		List<String> validParams = simParam.getPluginParameters();
		List<String> keySet = new ArrayList<>(stateSizes.keySet());

		// Remove invalid states
		for (String k : keySet) {

			//System.out.println("SwarmStateAgentParams simParam: " + k);

			if (!validParams.contains(k))
				stateSizes.remove(k);
		}

		//for(int i = 0; i < agent_type_count; i++)
		//agent_type_swarm_benefit_arr[i] = params.stateSizes.get(String.format(SwarmStateAgentParams.STATE_NAME_SWARM_BENEFIT, i + 1));

		//simParam

		// Add new states
		for (String k : validParams) {
			if (!stateSizes.containsKey(k))
			{
				if(k.startsWith("Swarm"))
				{
					if(k.endsWith("Benefit"))
					{
						if(k.equals(String.format(SwarmStateAgentParams.STATE_NAME_SWARM_BENEFIT, agent_type + 1)))
							stateSizes.put(k, 0.5f);
						else
							stateSizes.put(k, -0.5f);

					} // if(k.endsWith("Benefit"))
					else
						stateSizes.put(k, 0f);

				} // if(k.startsWith("Swarm"))
				else
					stateSizes.put(k, 0f);
			}
		}

		//System.out.println("simParam: " + simParam);
		// This is a HACK
		SimulationConfig simConfig = ((SimulationConfig) simParam);
		swarmAgentParams = simConfig.swarmParams.agentParams;
		//System.out.println("swarmAgentParams.length: " + swarmAgentParams.length);
	}

}
