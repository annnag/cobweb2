/**
 *
 */
package org.cobweb.cobweb2.impl;

import org.cobweb.io.ConfDisplayName;
import org.cobweb.io.ConfXMLTag;
import org.cobweb.io.ParameterSerializable;

/**
 * Parameters for the ComplexEnvironment
 */
public class ComplexEnvironmentParams implements ParameterSerializable {

	/**
	 * Width of the grid.
	 */
	@ConfDisplayName("Width")
	@ConfXMLTag("Width")
	public int width = 80;

	/**
	 * Height of the grid.
	 */
	@ConfDisplayName("Height")
	@ConfXMLTag("Height")
	public int height = 80;

	/**
	 * Enables the grid to wrap around globe style.
	 */
	@ConfDisplayName("Wrap(Globe-style)")
	@ConfXMLTag("wrap")
	public boolean wrapMap = true;

	/**
	 * Enables the grid to wrap around at the horizontal edges.
	 */
	@ConfDisplayName("Wrap horizontal")
	@ConfXMLTag("wrapX")
	public boolean wrapMapX = false;

	/**
	 * Enables the grid to wrap around at the vertical edges.
	 */
	@ConfDisplayName("Wrap vertical")
	@ConfXMLTag("wrapY")
	public boolean wrapMapY = false;

	/**
	 * Number of stones to randomly place
	 */
	@ConfDisplayName("Random stones")
	@ConfXMLTag("randomStones")
	public int initialStones = 10;


	private static final long serialVersionUID = 2L;
}