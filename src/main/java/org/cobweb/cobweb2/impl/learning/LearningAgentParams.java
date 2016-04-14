package org.cobweb.cobweb2.impl.learning;

import org.cobweb.io.ConfDisplayName;
import org.cobweb.io.ConfXMLTag;
import org.cobweb.io.ParameterSerializable;


public class LearningAgentParams implements ParameterSerializable {

	@ConfXMLTag("Learns")
	@ConfDisplayName("Agent learns")
	public boolean shouldLearn = false;

	@ConfXMLTag("LearnFromDifferentOthers")
	@ConfDisplayName("Learn from dissimilar agents")
	public boolean learnFromDifferentOthers = false;

	@ConfXMLTag("LearnFromOthers")
	@ConfDisplayName("Learn from other agents")
	public boolean learnFromOthers = false;

	@ConfXMLTag("BroadcastPleasure")
	@ConfDisplayName("Affection for broadcasting")
	public float broadcastPleasure = 0.1f;

	@ConfXMLTag("FoodPleasure")
	@ConfDisplayName("Affection for eating food")
	public float foodPleasure = 0.5f;

	@ConfXMLTag("AteAgentPleasure")
	@ConfDisplayName("Affection for eating agents")
	public float ateAgentPleasure = 0.2f;

	@ConfXMLTag("EatAgentEmotionalThreshold")
	@ConfDisplayName("Minimum affection to eat agent")
	public float eatAgentEmotionalThreshold = 0.1f;

	@ConfXMLTag("SparedEmotion")
	@ConfDisplayName("Emotional value when spared")
	public float sparedEmotion = 0.8f;

	@ConfXMLTag("ChildrenLove")
	@ConfDisplayName("Affection for children")
	public float emotionForChildren = 0;

	@ConfXMLTag("NumMemories")
	@ConfDisplayName("Memories to remember")
	public int numMemories = 4;

	@ConfXMLTag("PartnerLove")
	@ConfDisplayName("Affection for partner")
	public float loveForPartner = -0.1f;

	@Override
	public LearningAgentParams clone() {
		try {
			return (LearningAgentParams) super.clone();
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static final long serialVersionUID = 6152370881108746535L;
}