package org.mainolove.project.java.tool.yhqtool.yhqtool.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class JButtonTableCellEditor extends DefaultCellEditor {

	private JButton button;

	private JTable table;


	public void setTable(JTable table) {
		this.table = table;
	}

	public JButtonTableCellEditor(JCheckBox checkBox) {
		super(checkBox);
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				ButtonClick();
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table1, Object value, boolean isSelected, int row, int column) {
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}

		// button.setText(table1.getValueAt(row, column));
		button.setText("pp");
		return button;

	}

	@Override
	public Object getCellEditorValue() {
		return button.getText();
	}

	protected void ButtonClick() {
		System.out.println(table.getSelectedColumn() + " and  " + table.getSelectedRow());
	}

}
