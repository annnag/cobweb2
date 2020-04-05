/**
 *
 */
package org.cobweb.cobweb2.ui.swing.config;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.cobweb.io.ParameterChoice;

public class MixedValueJTable extends JTable {

	protected Hashtable<Integer, TableCellEditor> rowModel = new Hashtable<Integer, TableCellEditor>();

	private static class CobwebSelectionEditor extends DefaultCellEditor {

		private static final long serialVersionUID = 3458173499957389679L;

		private CobwebSelectionEditor(Set<ParameterChoice> options) {
			super(new JComboBox<ParameterChoice>(options.toArray(new ParameterChoice[0])));
		}

		@Override
		public void cancelCellEditing() {
			super.cancelCellEditing();
		}

		@Override
		public Object getCellEditorValue() {
			return super.getCellEditorValue();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			return super.getTableCellEditorComponent(table, value, isSelected, row, column);
		}

		@Override
		public boolean stopCellEditing() {
			return super.stopCellEditing();
		}

	}

	private static class EnumSelectionEditor extends DefaultCellEditor {

		private EnumSelectionEditor(Class<?> type) {
			super(new JComboBox<>(type.getEnumConstants()));
		}

		@Override
		public void cancelCellEditing() {
			super.cancelCellEditing();
		}

		@Override
		public Object getCellEditorValue() {
			return super.getCellEditorValue();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			return super.getTableCellEditorComponent(table, value, isSelected, row, column);
		}

		@Override
		public boolean stopCellEditing() {
			return super.stopCellEditing();
		}

	}

	private static class SelectAllCellEditor extends DefaultCellEditor {

		private JTextField textField = null;

		public SelectAllCellEditor(JTextField textField) {
			super(textField);
			this.textField = textField;
		}

		@Override
		public void cancelCellEditing() {
			super.cancelCellEditing();
		}

		@Override
		public Object getCellEditorValue() {
			return super.getCellEditorValue();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			//textField.selectAll();
			return super.getTableCellEditorComponent(table, value, isSelected, row, column);
		}

		@Override
		public boolean stopCellEditing() {
			return super.stopCellEditing();
		}

		JTextField getTextField()
		{
			return textField;
		}

	}

	private final class PerciseDecimalTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = -1919381757017436295L;

		private DecimalFormat formater = new DecimalFormat();

		PerciseDecimalTableCellRenderer(){
			super.setHorizontalAlignment(RIGHT);
		}


		@Override
		public Component getTableCellRendererComponent
		(JTable tbl, Object value, boolean selected, boolean focused, int row, int col){
			value = formater.format(value);
			return super.getTableCellRendererComponent(tbl, value, selected, focused, row, col);
		}
	}

	private static class FocusTextField extends JTextField
	{
		protected boolean fg_all_selected = false;

		public FocusTextField(String text)
		{
			super(text);
			//System.out.println("FocusTextField created with: " + text);
			addFocusListener(
					new FocusListener() {

						@Override
						public void focusGained(FocusEvent fe) {
							//System.out.println("Focus gained. fg_all_selected: " + fg_all_selected);

							if(!fg_all_selected)
							{
								fg_all_selected = true;
								FocusTextField.this.selectAll();
								//System.out.println("FocusTextField.this.selectAll()");

							}

						}

						@Override
						public void focusLost(FocusEvent fe) {
							//System.out.println("Focus lost.");
							FocusTextField.this.select(0, 0);
							FocusTextField.this.fireActionPerformed();
							fg_all_selected = false;
						}}
					);

			addKeyListener(
					new KeyListener() {

						@Override
						public void keyPressed(KeyEvent arg0) {
							//System.out.println("Key typed.");

						}

						@Override
						public void keyReleased(KeyEvent arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void keyTyped(KeyEvent arg0) {
							// TODO Auto-generated method stub

						}}
					);

		}



		public void setAllSelected(boolean fg_all_selected)
		{
			this.fg_all_selected = fg_all_selected;
		}

		public boolean getAllSelected()
		{
			return fg_all_selected;
		}

		@Override
		public Document getDocument()
		{
			return super.getDocument();
		}

		@Override
		protected Document createDefaultModel() {
			//System.out.println("createDefaultModel");
			return new TestDocument();
		}

		static class TestDocument extends PlainDocument {

			@Override
			public void insertString(int offs, String str, AttributeSet a)
					throws BadLocationException {

				//System.out.println("insertString: " + str + " offs: " + offs);

				if (str == null) {
					return;
				}
				char[] upper = str.toCharArray();
				for (int i = 0; i < upper.length; i++) {
					upper[i] = upper[i];
				}
				super.insertString(offs, new String(upper), a);
			}
		}
	}

	private static final long serialVersionUID = -9106510371599896107L;

	public ConfigTableModel configModel;

	public MixedValueJTable(ConfigTableModel model) {
		super();
		this.getTableHeader().setReorderingAllowed(false);
		this.configModel = model;
		setModel(model);

		addKeyListener(
				new KeyListener() {

					@Override
					public void keyPressed(KeyEvent arg0) {
						//System.out.println("JTable Key typed." + arg0.getKeyChar());
						doSelectAll();
					}

					@Override
					public void keyReleased(KeyEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void keyTyped(KeyEvent arg0) {
						// TODO Auto-generated method stub

					}}
				);

	}

	public void doSelectAll()
	{
		int row = getSelectedRow();
		int col = getSelectedColumn();

		//getCellEditor(row, col).shouldSelectCell(arg0);
		Object editorObj = getCellEditor(row, col);

		//System.out.println("doSelectAll() editorObj: " + editorObj.toString());

		if(editorObj instanceof SelectAllCellEditor)
		{
			SelectAllCellEditor selectAllCellEditor = (SelectAllCellEditor) editorObj;
			FocusTextField jTextField = (FocusTextField) selectAllCellEditor.getTextField();
			//selectAllCellEditor.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row)
			if(!jTextField.hasFocus())
			{
				//selectAllCellEditor.getTextField().selectAll();
				//System.out.println("selectAll()");
				//jTextField.requestFocus();
				//jTextField.transferFocus();
				//jTextField.requestFocusInWindow();
				//System.out.println("Focus requested.");

				if (editCellAt(row, col))
				{
					Component editor = getEditorComponent();

					if(!jTextField.getAllSelected())
					{
						editor.requestFocusInWindow();


						jTextField.setAllSelected(true);
						((JTextComponent)editor).selectAll();


						//System.out.println("((JTextComponent)editor).selectAll()");
						//*/
					}
					//else



				}

			}
		}

	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		//TableColumn tableColumn = getColumnModel().getColumn(column);
		//TableCellEditor editor = tableColumn.getCellEditor();

		TableCellEditor editor = rowModel.get(row);

		//System.out.println("row: " + row + " column: " + column + " Class: " + getValueAt(row, column) + " tableColumn.getCellEditor(): " + tableColumn.getCellEditor());
		//System.out.println("row: " + row + " column: " + column + " Class: " + getValueAt(row, column));

		//if(editor != null)
		//throw new RuntimeException("stop");

		if (getValueAt(row, column) instanceof ParameterChoice) {
			editor = new CobwebSelectionEditor(configModel.getRowOptions(row));
			//System.out.println("CobwebSelectionEditor");
			//tableColumn.setCellEditor(editor);
			rowModel.put(row, editor);
		}
		else if (getValueAt(row, column).getClass().isEnum()) {
			editor = new EnumSelectionEditor(getValueAt(row, column).getClass());
			//System.out.println("EnumSelectionEditor");
			//tableColumn.setCellEditor(editor);
			rowModel.put(row, editor);
		}
		else if (getValueAt(row, column) instanceof Boolean) {
			editor = getDefaultEditor(getValueAt(row, column).getClass());
			//tableColumn.setCellEditor(editor);
			rowModel.put(row, editor);
		}
		else if (editor == null && getValueAt(row, column) != null) {
			//editor = getDefaultEditor(getValueAt(row, column).getClass());

			// Select all text when the focus is on the field
			editor = new SelectAllCellEditor(new FocusTextField(getValueAt(row, column).toString()));
			//System.out.println("SelectAllCellEditor Created");
			//tableColumn.setCellEditor(editor);
			rowModel.put(row, editor);
		}
		else if (editor == null) {
			editor = getDefaultEditor(getColumnClass(column));
			//editor = new SelectAllCellEditor(new FocusTextField(getValueAt(row, column).toString()));
			//System.out.println("DefaultEditor");
			//tableColumn.setCellEditor(editor);
			rowModel.put(row, editor);
		}
		return editor;
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		TableColumn tableColumn = getColumnModel().getColumn(column);
		TableCellRenderer renderer = tableColumn.getCellRenderer();
		if (renderer == null && getValueAt(row, column) != null) {
			if (getValueAt(row, column) instanceof Double ||
					getValueAt(row, column) instanceof Float ){
				renderer = new PerciseDecimalTableCellRenderer();
			} else {
				renderer = getDefaultRenderer(getValueAt(row, column).getClass());
			}
		}
		if (renderer == null) {
			renderer = getDefaultRenderer(getColumnClass(column));
		}
		return renderer;

	}

	//  Place cell in edit mode when it 'gains focus'

	@Override
	public void changeSelection(
			int row, int column, boolean toggle, boolean extend)
	{
		super.changeSelection(row, column, toggle, extend);

		//int row = getSelectedRow();
		//int col = getSelectedColumn();

		//getCellEditor(row, col).shouldSelectCell(arg0);
		TableCellEditor editorObj = getCellEditor(row, column);

		//System.out.println("changeSelection() editorObj: " + editorObj.toString());

		if(editorObj instanceof SelectAllCellEditor)
		{
			SelectAllCellEditor selectAllCellEditor = (SelectAllCellEditor) editorObj;
			FocusTextField jTextField = (FocusTextField) selectAllCellEditor.getTextField();
			jTextField.setAllSelected(false);
		} // if(editorObj instanceof SelectAllCellEditor)
		else if(editorObj instanceof SelectAllCellEditor)
		{

		}

		//editorObj.stopCellEditing();


		//doSelectAll();
		/*if (editCellAt(row, column))
		{
			Component editor = getEditorComponent();
			editor.requestFocusInWindow();
			//          ((JTextComponent)editor).selectAll();
		}*/
	}
}