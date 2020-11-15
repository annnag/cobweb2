package org.cobweb.cobweb2.plugins.circle;

import org.cobweb.cobweb2.Simulation;
import org.cobweb.cobweb2.core.Agent;
import org.cobweb.cobweb2.core.Direction;
import org.cobweb.cobweb2.core.Location;
import org.cobweb.cobweb2.core.Rotation;
import org.cobweb.cobweb2.core.SimulationTimeSpace;
import org.cobweb.cobweb2.core.Topology;
import org.cobweb.cobweb2.impl.ComplexAgent;
import org.cobweb.cobweb2.impl.ComplexEnvironment;
import org.cobweb.cobweb2.plugins.AgentState;
import org.cobweb.cobweb2.plugins.EnvironmentMutator;
import org.cobweb.cobweb2.plugins.MoveMutator;
import org.cobweb.cobweb2.plugins.gravity.GravityParams;

public class CircleMutator implements MoveMutator, EnvironmentMutator {

    public CircleMutator(){
    }

    public void setParams(CircleParams params){

    }

    @Override
    public void loadNew() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean overrideMove(Agent agent) {
        return false;
    }

    @Override
    public <T extends AgentState> boolean acceptsState(Class<T> type, T value) {
        return false;
    }
}
