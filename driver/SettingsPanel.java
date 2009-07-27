package driver;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class SettingsPanel extends JPanel  {


	/**
	 *
	 */
	private static final long serialVersionUID = 7049471695197763906L;



	public SettingsPanel() {
		super();
	}

	public SettingsPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public SettingsPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public SettingsPanel(LayoutManager layout) {
		super(layout);
	}

	public abstract void bindToParser(Parser p);

	//public abstract void writeToParser(Parser p);
}