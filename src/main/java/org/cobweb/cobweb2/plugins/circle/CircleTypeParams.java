package org.cobweb.cobweb2.plugins.circle;

import org.cobweb.io.ConfDisplayName;
import org.cobweb.io.ConfXMLTag;
import org.cobweb.io.ParameterSerializable;
import org.cobweb.util.MutatableInt;

public class CircleTypeParams implements ParameterSerializable{

    private static final long serialVersionUID = 4935757387466603476L;

    /*
     * The radius of this circle
     */
    @ConfDisplayName("Circle radius")
    @ConfXMLTag("circleRadius")
    public MutatableInt circleRadius = new MutatableInt(1);

    public CircleTypeParams() {
    }
}
