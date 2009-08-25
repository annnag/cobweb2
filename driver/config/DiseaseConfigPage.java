/**
 * 
 */
package driver.config;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import disease.DiseaseParams;

/**
 * Configuration page for Disease
 */
public class DiseaseConfigPage implements ConfigPage {

	JPanel myPanel;

	DiseaseParams[] params;

	private JTable confTable;

	/**
	 * Sets up the disease configuration page from the simulation parameters
	 * 
	 * @param params the agent specific disease parameters
	 */
	public DiseaseConfigPage(DiseaseParams[] params) {
		this.params = params;

		ConfigTableModel ctm = new ConfigTableModel(params, "Agent");
		confTable = new MixedValueJTable();
		confTable.setRowHeight(20);
		confTable.setModel(ctm);
		JScrollPane sp = new JScrollPane(confTable);

		GUI.makeGroupPanel(sp, "Disease Parameters");
		GUI.colorHeaders(confTable, true);
		confTable.getColumnModel().getColumn(0).setPreferredWidth(150);

		myPanel = new JPanel(new BorderLayout());
		myPanel.add(sp);

	}

	public JPanel getPanel() {
		return myPanel;
	}

	public void validateUI() throws IllegalArgumentException {
		GUI.updateTable(confTable);
	}

}
