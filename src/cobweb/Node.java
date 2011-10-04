package cobweb;


public class Node {
	private Environment.Location tile;

	public int distance;

	public boolean visited;

	public Node(int distance, Environment.Location tile) {
		this.distance = distance;
		this.tile = tile;
		this.visited = false;
	}

	public Environment.Location getLocation() {
		return this.tile;
	}

	public boolean isVisited() {
		return this.visited;
	}

	public void setVisited() {
		this.visited = true;
	}
}