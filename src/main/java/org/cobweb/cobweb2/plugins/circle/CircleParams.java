package org.cobweb.cobweb2.plugins.circle;

import org.cobweb.cobweb2.core.AgentFoodCountable;
import org.cobweb.cobweb2.plugins.PerTypeParam;
import org.cobweb.io.ConfDisplayName;
import org.cobweb.io.ConfList;
import org.cobweb.io.ConfXMLTag;

import java.util.Arrays;

public class CircleParams implements PerTypeParam<CircleTypeParams> {
    @ConfDisplayName("Enable circular motion")
    @ConfXMLTag("circleEnabled")
    public boolean circleEnabled = false;

    @ConfDisplayName("Enable clockwise motion")
    @ConfXMLTag("clockwise")
    public boolean clockwise = false;

    //@ConfDisplayName("Circle Radius")
    //@ConfXMLTag("circleRadius")
    //public int circleRadius = 0;

    @ConfXMLTag("CircleParams")
    @ConfList(indexName = "Circle", startAtOne = true)
    public CircleTypeParams[] circleParams = new CircleTypeParams[0];

    public CircleParams(AgentFoodCountable initialSize) {
        resize(initialSize);
    }

    @Override
    public void resize(AgentFoodCountable envParams) {
        CircleTypeParams[] n = Arrays.copyOf(circleParams, envParams.getAgentTypes());

        for (int i = circleParams.length; i < envParams.getAgentTypes(); i++) {
            n[i] = new CircleTypeParams();
        }
        circleParams = n;
    }

    @Override
    public CircleTypeParams[] getPerTypeParams() {
        return circleParams;
    }

    private static final long serialVersionUID = 1L;
}
