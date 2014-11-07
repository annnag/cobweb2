package production;

import java.awt.Color;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cobweb.Environment;
import cobweb.Environment.Location;
import cwcore.ComplexAgent;
import cwcore.ComplexEnvironment;
import cwcore.state.StateParameter;
import cwcore.state.StatePlugin;

public class ProductionMapper implements StatePlugin {

	private ComplexEnvironment e;
	private float[][] vals;
	private float maxValue;

	public ProductionMapper(Environment e) {
		this(500, e);
	}

	public ProductionMapper(int numProducts, Environment e) {
		this.e = (ComplexEnvironment) e;
		vals = new float[this.e.getWidth()][this.e.getHeight()];
		params = new LinkedList<StateParameter>();
		params.add(new ProductHunt());
	}

	void addProduct(Product p, Environment.Location loc) {
		float newMax = 0;
		for (int x = 0; x < vals.length; x++) {
			for (int y = 0; y < vals[x].length; y++) {
				float value = getDifAtLoc(p, loc, e.getLocation(x, y));
				vals[x][y] += value;
				if (vals[x][y] > newMax) {
					newMax = vals[x][y];
				}
			}
		}
		maxValue = newMax;
	}

	void remProduct(Product p) {
		Location loc = p.loc;
		e.dropArray[loc.v[0]][loc.v[1]] = null;
		loc.setFlag(ComplexEnvironment.FLAG_DROP, false);

		float newMax = 0;
		for (int x = 0; x < vals.length; x++) {
			for (int y = 0; y < vals[x].length; y++) {
				float value = getDifAtLoc(p, p.getLocation(), e.getLocation(x, y));
				vals[x][y] -= value;
				if (vals[x][y] > newMax) {
					newMax = vals[x][y];
				}				
			}
		}
		maxValue = newMax;
	}

	private float getDifAtLoc(Product source, Location loc, Location loc2) {
		float val = source.getValue();
		val /= Math.max(1, loc.distanceSquare(loc2));
		return val;
	}

	/**
	 * @param loc - the location whose "Productivity value" we are querying
	 * @return the total "Productivity value" of the parameter Location.
	 * 
	 *         It is most efficient to place products on tiles that have prod.
	 *         vals. that indicate that a sufficient number of products are
	 *         nearby in order to attract agent's business, but not enough are
	 *         around so that there is too much competition. Therefore:
	 * 
	 *         -An agent's probability of dropping a product on a tile with low
	 *         (~0) prod. val. should be low. (but not non-existant because then
	 *         initially agents would never drop products) -An agent's
	 *         probability of dropping a product on a tile with a very high
	 *         prod. val. should be infinitesimal. -An agent should have a high
	 *         chance of dropping a product on a tile with a moderate prob. val.
	 * 
	 */
	public float getValueAtLocation(Environment.Location loc) {
		return vals[loc.v[0]][loc.v[1]];
	}

	public float getValueAtLocation(int x, int y) {
		Environment.Location loc = e.getLocation(x, y);
		return getValueAtLocation(loc);
	}

	Color[][] getTileColors(int x, int y) {
		Color[][] ret = new Color[x][y];
		float lockedMax = maxValue;

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				float val = getValueAtLocation(i, j);
				int amount = 255 - (int) ((Math.min(val, lockedMax) / lockedMax) * 255f);
				// FIXME: threading bug when speed set to max simulation speed, amount ends up out of range
				if (amount > 255) {
					amount = 255;
				}

				ret[i][j] = new Color(amount / 2 + 127, amount, 255);
			}
		}
		return ret;
	}

	Disp display = null;

	public Disp createDialog() {
		display = new Disp(this, this.e.getWidth(), this.e.getHeight());
		return display;
	}

	public class ProductHunt implements StateParameter {

		@Override
		public String getName() {
			return "ProdHunt";
		}

		@Override
		public double getValue(ComplexAgent agent) {
			Location here = agent.getPosition();
			Location ahead = here.getAdjacent(agent.getFacing());
			if (ahead == null || !ahead.isValid()) {
				return 0;
			}

			float a = getValueAtLocation(here);
			float b = getValueAtLocation(ahead);

			float max = Math.max(a, b);
			if (max == 0)
				return 0;

			return b / max;
		}

	}

	private List<StateParameter> params;

	@Override
	public List<StateParameter> getParameters() {
		return params;
	}

	Set<Product> products = new LinkedHashSet<Product>();

	public Product createProduct(float value, ComplexAgent owner) {
		Location loc = owner.getPosition();
		Product prod = new Product(value, owner, loc, this);
		addProduct(prod, loc);

		e.setDrop(loc, prod);
		products.add(prod);

		return prod;
	}

}