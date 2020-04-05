package org.cobweb.cobweb2.impl.ai;

import org.cobweb.cobweb2.Simulation;
import org.cobweb.cobweb2.core.Agent;
import org.cobweb.cobweb2.core.Controller;
import org.cobweb.cobweb2.core.ControllerInput;
import org.cobweb.cobweb2.core.ControllerListener;
import org.cobweb.cobweb2.core.Direction;
import org.cobweb.cobweb2.core.LocationDirection;
import org.cobweb.cobweb2.core.SimulationInternals;
import org.cobweb.cobweb2.core.Topology;
import org.cobweb.cobweb2.impl.ComplexAgent;
import org.cobweb.cobweb2.plugins.vision.SeeInfo;
import org.cobweb.cobweb2.plugins.vision.VisionState;
import org.cobweb.util.BitField;

/**
 * This class contains methods that set up the parameters for agents
 * that are used to influence the actions of the agents.
 *
 * @author David Fu
 *
 */
public class SwarmController implements Controller {

	private final static int TURN_LEFT = 0;
	private final static int TURN_RIGHT = 1;
	private final static int MOVE_STRAIGHT = 2;
	private final static int NONE = 3;

	public BehaviorArray ga;

	private static final int INPUT_BITS = 8;

	private static final int OUTPUT_BITS = 2;

	private static final int ENERGY_THRESHOLD = 160;

	private final SwarmStateAgentParams params;

	private SimulationInternals simulation;

	private int agent_type_count = 0;
	private float[] agent_type_swarm_benefit_arr;

	public SwarmController(SimulationInternals sim, SwarmStateAgentParams params, int agent_type_count) {
		this.simulation = sim;
		this.params = params;
		int[] outputArray = { OUTPUT_BITS, params.memoryBits, params.communicationBits, 1 };

		int inputSize = INPUT_BITS + params.memoryBits + params.communicationBits;

		/* Skipped. Not relevant to our implementation of Swarm.
		for (int ss : this.params.stateSizes.values()) {
			inputSize += Math.abs(ss); // We accept negative numbers ;)
		}
		 */

		this.agent_type_count = agent_type_count;

		agent_type_swarm_benefit_arr = new float[agent_type_count];

		for (String key : this.params.stateSizes.keySet())
		{
			if(!key.startsWith("Swarm"))
			{
				inputSize += params.stateSizes.get(key);
				//System.out.print("Skipped ");
			} // if(!key.startsWith("Swarm"))

			/*
			if(key.startsWith("Swarm"))
			{
				System.out.print("Skipped ");
			} // if(key.startsWith("Swarm"))
			else
			{
				inputSize += params.stateSizes.get(key);
				System.out.print("Added ");
			} // else
			 */

			//System.out.println("key: " + key + " val: " + params.stateSizes.get(key));
		}

		ga = new BehaviorArray(inputSize, outputArray);
		ga.randomInit(this.params.randomSeed);

		for(int i = 0; i < agent_type_count; i++)
			agent_type_swarm_benefit_arr[i] = params.stateSizes.get(String.format(SwarmStateAgentParams.STATE_NAME_SWARM_BENEFIT, i + 1));

		/*
		Set<String> keySet = params.stateSizes.keySet();
		Object[] key_arr = keySet.toArray();

		for(int i = 0; i < key_arr.length; i++)
		{
			System.out.println("key: " + key_arr[i] + " val: " + params.stateSizes.get(key_arr[i]));
		} //
		 */


	}

	protected SwarmController(SwarmController parent) {
		simulation = parent.simulation;
		params = parent.params;
		ga = parent.ga
				.copy(params.mutationRate, simulation.getRandom());

		agent_type_count = parent.agent_type_count;

		for(int i = 0; i < agent_type_count; i++)
			agent_type_swarm_benefit_arr[i] = params.stateSizes.get(String.format(SwarmStateAgentParams.STATE_NAME_SWARM_BENEFIT, i + 1));
	}

	protected SwarmController(SwarmController parent1, SwarmController parent2) {
		simulation = parent1.simulation;
		params = parent1.params;
		ga = BehaviorArray
				.splice(parent1.ga, parent2.ga, simulation.getRandom())
				.copy(params.mutationRate, simulation.getRandom());

		agent_type_count = parent1.agent_type_count;
		agent_type_swarm_benefit_arr = parent1.agent_type_swarm_benefit_arr;

		//for(int i = 0; i < agent_type_count; i++)
		//agent_type_swarm_benefit_arr[i] = params.stateSizes.get(String.format(STATE_NAME_SWARM_BENEFIT, i + 1));
	}

	/**
	 * Given the agent's energy, get the amount of energy to add to the array.
	 * @param energy The agent's energy.
	 */
	private static int getEnergy(int energy) {
		final int maxEnergy = 3;

		if(energy > ENERGY_THRESHOLD) {
			return maxEnergy;
		} else {
			return (int) ((double) energy / (ENERGY_THRESHOLD) * 4.0);
		}
	}

	public class GCInput implements ControllerInput {
		public GCInput(BitField inputCode) {
			this.inputCode = inputCode;
		}
		public BitField inputCode;

		@Override
		public void mutate(float adjustmentStrength) {
			ga.mutateOutput(inputCode.intValue(), adjustmentStrength, simulation.getRandom());
		}
	}

	/**
	 * Converts the parameters of the agent into a behavior (turn left or right,
	 * step).
	 *
	 * @see BehaviorArray
	 * @see ComplexAgent#turnLeft()
	 * @see ComplexAgent#turnRight()
	 * @see ComplexAgent#step()
	 */
	@Override
	public void controlAgent(Agent baseAgent, ControllerListener inputCallback) {
		ComplexAgent theAgent = (ComplexAgent) baseAgent;

		BitField inputCode = getInputArray(theAgent); // NOTES: get "DNA"

		// if(true) throw new RuntimeException("mutated Output!!");
		//System.out.println("before actionCode: " + ga.getOutput(inputCode.intValue())[0]);
		//int before_control = ga.getOutput(inputCode.intValue())[0];

		inputCallback.beforeControl(theAgent, new GCInput(inputCode));

		int[] outputArray = ga.getOutput(inputCode.intValue());  // NOTES: determine action based on "DNA"

		int actionCode = outputArray[0];
		theAgent.setMemoryBuffer(dequantize(outputArray[1], params.memoryBits));
		theAgent.setCommOutbox(dequantize(outputArray[2], params.communicationBits));
		//whether to breed
		theAgent.setShouldReproduceAsex(outputArray[3] != 0);

		//if(before_control != actionCode)
		//System.out.println("before_control: " + before_control + " actionCode: " + actionCode);
		//System.out.println("inputCode: " + inputCode.intValue() + " actionCode: " + actionCode + " loc: " + theAgent.getPosition().toString());

		if(!overrideMove(theAgent))
		{
			if (simulation.getRandom().nextFloat() > params.individuality) // stronger individuals will be influenced by more by their genetics
				actionCode = simulation.getRandom().nextInt(4);

			// the original moves from GeneticController
			switch (actionCode) {
				case TURN_LEFT:
					theAgent.turnLeft();
					break;
				case TURN_RIGHT:
					theAgent.turnRight();
					break;
				case MOVE_STRAIGHT:
				case NONE:
					theAgent.step();
					break;
			}
		} // if(!overrideMove(theAgent))
	}

	// Adapted from PersonalityMutator
	public boolean overrideMove(Agent ag) {

		if(simulation.getRandom().nextFloat() <= 0.5 * params.individuality) // stronger individuals are more likely to go lone wolf
			return false;

		//System.out.println("PersonalityMutator.overrideMove(Agent ag)");

		Simulation simulation = (Simulation) this.simulation;
		ComplexAgent agent = (ComplexAgent) ag;

		/*
		PersonalityState state = agent.getState(PersonalityState.class);
		if (state == null) {
			System.out.println("PersonalityMutator state == null");
			return false;
		}

		// Shouldn't use this if the agents don't have personalities or their openness or neuroticism
		// isn't high enough to warrant any special moves


        if (!state.agentParams.personalitiesEnabled ||
                (state.agentParams.openness < 0.25 && state.agentParams.neuroticism < 0.25)) {
            return false;
        }


		if (!state.agentParams.personalitiesEnabled ||
				(simulation.getRandom().nextFloat() > state.agentParams.openness &&
						simulation.getRandom().nextFloat() > state.agentParams.neuroticism)) {
			System.out.println("PersonalityMutator fail: " + state.agentParams.personalitiesEnabled);
			return false;
		}
		 */

		//System.out.println("SwarmController find the closest agent");

		// Now find the closest agent
		Agent closest = simulation.theEnvironment.getClosestAgent(agent);

		if(closest == null)		// Nothing to do.
			return false;

		LocationDirection l1 = agent.getPosition();
		LocationDirection l2 = closest.getPosition();
		//LocationDirection l2 = new LocationDirection(new Location(40, 40), Topology.NORTH);

		int agent_type = agent.getType();
		int closet_agent_type = closest.getType();
		//int closet_agent_type = 0;

		//System.out.println("agent.getType() " + agent.getPosition() + ": " + agent_type + ", closest.getType() " + closest.getPosition() + ": " + closet_agent_type + " agent_type_swarm_benefit_arr: " + agent_type_swarm_benefit_arr);

		float closest_agent_type_benefit = agent_type_swarm_benefit_arr[closet_agent_type]; // 0 indexed

		//System.out.println("closest_agent_type_benefit: " + closest_agent_type_benefit);

		//boolean fg_do_something = false;

		if (simulation.getRandom().nextFloat() <= Math.abs(closest_agent_type_benefit))
			return false;

		boolean fg_do_avoid = false;

		// if the benefit is negative, we avoid the agent.
		if(closest_agent_type_benefit < 0)
			fg_do_avoid = true;

		double distance = simulation.getTopology().getDistance(l1, l2);

		/*
		for (int i = 0; i < params.swarmAgentParams[agent_type].effects.length; i++) {
			PairwiseEffect effect = params.swarmAgentParams[agent_type].effects[i];
			System.out.println("effect radius " + i + " : " + effect.radius);
		}
		 */

		//System.out.println("params.swarmAgentParams[" + agent_type + "].effects[" + closet_agent_type + "].radius: " + params.swarmAgentParams[agent_type].effects[closet_agent_type].radius);
		float radius = params.swarmAgentParams[agent_type].effects[closet_agent_type].radius;

		if (radius > 0 && distance > radius) // Not part of swarm
			return false;

		//if (distance < 2) {
		if (distance <= 1 && simulation.getRandom().nextFloat() <= params.individuality)
		{

			if(fg_do_avoid)
			{
				if (simulation.getRandom().nextFloat() <= 0.75)
					agent.step();
				else if (simulation.getRandom().nextFloat() <= 0.5)
					agent.turnRight();
				else
					agent.turnLeft();
			}
			else
				agent.step();

		} // if (distance <= 1 && simulation.getRandom().nextFloat() <= 0.125)
		else
		{
			// If the direction of the agent is not facing the closest agent, make it turn
			Direction agentToClosest = simulation.getTopology().getDirectionBetween4way(l1, l2);
			Direction agentDirection = l1.direction;

			// If the agent is already heading in the right direction, then keep on going
			if (agentToClosest.equals(agent.getPosition().direction) || agent.stop_count > 2)
			{
				if(fg_do_avoid)
					if (simulation.getRandom().nextFloat() > 0.5)
						agent.turnRight();
					else
						agent.turnLeft();
				else
					agent.step();

			}            // Otherwise turn the agent towards the direction to the other agent
			else if ((agentToClosest.equals(Topology.NORTH) && agentDirection.equals(Topology.EAST)) ||
					(agentToClosest.equals(Topology.NORTH) && agentDirection.equals(Topology.SOUTH)) ||
					(agentToClosest.equals(Topology.EAST) && agentDirection.equals(Topology.SOUTH)) ||
					(agentToClosest.equals(Topology.SOUTH) && agentDirection.equals(Topology.WEST)) ||
					(agentToClosest.equals(Topology.SOUTH) && agentDirection.equals(Topology.NORTH)) ||
					(agentToClosest.equals(Topology.WEST) && agentDirection.equals(Topology.NORTH))) {

				if(fg_do_avoid)
					agent.turnRight();
				else
					agent.turnLeft();


				/*
				if(fg_do_avoid)
					agent.turnLeft();
				else
					agent.turnRight();
				 */

			} else if ((agentToClosest.equals(Topology.NORTH) && agentDirection.equals(Topology.WEST)) ||
					(agentToClosest.equals(Topology.EAST) && agentDirection.equals(Topology.NORTH)) ||
					(agentToClosest.equals(Topology.EAST) && agentDirection.equals(Topology.WEST)) ||
					(agentToClosest.equals(Topology.SOUTH) && agentDirection.equals(Topology.EAST)) ||
					(agentToClosest.equals(Topology.WEST) && agentDirection.equals(Topology.SOUTH)) ||
					(agentToClosest.equals(Topology.WEST) && agentDirection.equals(Topology.EAST))) {

				if(fg_do_avoid)
					agent.turnLeft();
				else
					agent.turnRight();

			}
			else
			{
				//if(agentToClosest.equals(Topology.NONE))
				//throw new RuntimeException("Topology.NONE");
				return false;
			} // else
		} // else

		return true;
	}


	/**
	 * Given an agent, read all information for it and compound it into an array.
	 * @param theAgent The agent.
	 * @return The input array.
	 */
	private BitField getInputArray(ComplexAgent theAgent) {
		BitField inputCode = new BitField();

		//add the energy info to the array
		inputCode.add(getEnergy(theAgent.getEnergy()), 2);
		//System.out.print("E: " + getEnergy(theAgent.getEnergy()));

		//add the direction the agent is facing to the array
		inputCode.add(simulation.getTopology()
				.getRotationBetween(Topology.NORTH, theAgent.getPosition().direction)
				.ordinal(), 2);

		//System.out.print(", D: " + simulation.getTopology().getRotationBetween(Topology.NORTH, theAgent.getPosition().direction).ordinal());

		//add the viewing info to the array

		SeeInfo get = theAgent.getState(VisionState.class).distanceLook();
		inputCode.add(get.getType(), 2);
		inputCode.add(get.getDist(), 2);
		//System.out.print(", V: " + get.getType() + ", " + get.getDist());

		//add the memory buffer to the array
		inputCode.add(
				quantize(theAgent.getMemoryBuffer(), params.memoryBits),
				params.memoryBits);
		//System.out.print(", M: " + quantize(theAgent.getMemoryBuffer(), params.memoryBits));

		//add the communications to the array
		inputCode.add(
				quantize(theAgent.getCommInbox(), params.communicationBits),
				params.communicationBits);
		//System.out.print(", C: " + quantize(theAgent.getCommInbox(), params.communicationBits));

		/* Skipped. Not relevant to our implementation of Swarm.
		for (Entry<String, Float> ss : params.stateSizes.entrySet()) {
			StateParameter sp = simulation.getStateParameter(ss.getKey());
			double value = sp.getValue(theAgent);
			float size = ss.getValue();
			inputCode.add(quantize(value, size), size);
			System.out.print(", S: " + quantize(value, size));
		}
		 */

		//System.out.println();

		return inputCode;
	}

	private static int quantize(double val, int bits) {
		int max = (1 << bits) - 1;
		double doubleVal = val * max;
		int intVal = (int) Math.round(doubleVal);
		return intVal;
	}

	private static double dequantize(int val, int bits) {
		int max = (1<< bits) -1;
		double doubleVal = (double) val / max;
		return doubleVal;
	}

	@Override
	public SwarmController createChildAsexual() {
		SwarmController child = new SwarmController(this);
		return child;
	}

	@Override
	public SwarmController createChildSexual(Controller parent2) {
		if (!(parent2 instanceof SwarmController)) {
			throw new RuntimeException("Parent's controller type must match the child's");
		}
		SwarmController p2 = (SwarmController) parent2;

		SwarmController child = new SwarmController(this, p2);
		return child;
	}

	/** return the measure of similiarity between this agent and the 'other'
	 ranging from 0.0 to 1.0 (identical)

	 */
	public double similarity(SwarmController other) {
		return ga.similarity(other.ga);
	}

}