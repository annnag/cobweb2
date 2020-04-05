package org.cobweb.cobweb2.plugins;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.cobweb.cobweb2.core.AgentFoodCountable;
import org.cobweb.io.ConfList;
import org.cobweb.io.ConfXMLTag;
import org.cobweb.io.ParameterSerializable;


public abstract class PerAgentParams<T extends ParameterSerializable> implements PerTypeParam<T> {

	@ConfXMLTag("AgentParams")
	@ConfList(indexName = "Agend", startAtOne = true)
	//ConfList(indexName = SimulationConfig.agentLabel, startAtOne = true)
	public T[] agentParams;

	private Class<T> agentParamClass;

	int agent_type = 0;

	/**
	 * Creates agent param array of size 0. Call resize() to set required size after.
	 */
	@SuppressWarnings("unchecked")
	public PerAgentParams(Class<T> agentParamClass) {
		this.agentParamClass = agentParamClass;
		agentParams = (T[]) Array.newInstance(this.agentParamClass, 0);
	}

	/**
	 * Create agent param array and resize to initial size.
	 * Requires that newAgentParam() does not depend on any new members of the subclass!
	 */
	public PerAgentParams(Class<T> agentparClass, AgentFoodCountable initialSize) {
		this(agentparClass);
		resize(initialSize);
	}

	@Override
	public void resize(AgentFoodCountable envParams) {
		T[] n = Arrays.copyOf(agentParams, envParams.getAgentTypes());

		for (int i = agentParams.length; i < envParams.getAgentTypes(); i++) {
			// 20200226: fchoong - For backwards compatibility
			n[i] = newAgentParam();

			if(n[i] == null)
				n[i] = newAgentParam(i);
		}
		agentParams = n;
	}

	protected abstract T newAgentParam();

	protected T newAgentParam(int agent_type)
	{
		return null;
	}

	//agent_type

	@Override
	public T[] getPerTypeParams() {
		return agentParams;
	}

	private static final long serialVersionUID = 1L;
}
