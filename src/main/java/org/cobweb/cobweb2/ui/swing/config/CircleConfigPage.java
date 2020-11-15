package org.cobweb.cobweb2.ui.swing.config;

import org.cobweb.cobweb2.SimulationConfig;
import org.cobweb.cobweb2.plugins.circle.CircleParams;
import org.cobweb.cobweb2.plugins.circle.CircleTypeParams;
import org.cobweb.swingutil.ColorLookup;

public class CircleConfigPage extends TwoTableConfigPage<CircleParams, CircleTypeParams> {

    public CircleConfigPage(CircleParams params, ColorLookup agentColors) {
        super(CircleParams.class, params, "Circle Parameters", agentColors, "Value", null, "Circle Type Parameters", SimulationConfig.agentLabel);
        setMainPanelHeight(100);
    }
}
