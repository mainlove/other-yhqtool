package org.mainolove.project.java.tool.yhqtool.yhqtool.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.mainolove.project.java.tool.yhqtool.yhqtool.model.Page;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.StockCondition;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.StockList;
import org.mainolove.project.java.tool.yhqtool.yhqtool.model.StockRecord;
import org.mainolove.project.java.tool.yhqtool.yhqtool.service.ViewApi;

public class View {

	public final static Object[] columnNames = { "No", "柜号", "品名", "名字", "入库", "实际", "入库时间", "原因", "通知日期", "已出库", "仓库",
			"费用", "备注", "操作", "隐藏" };
	private final static Object[] columnNameshow = { "No[0]", "单号[1]", "品名[2]", "名字[3]", "入库[4]", "实际[5]", "入库时间[6]",
			"原因[7]", "通知日期[8]", "已出库[9]", "仓库[10]", "费用[11]", "备注[12]", "操作[13]", "隐藏[14]" };

	private final static List<Integer> integerColumn = new ArrayList<Integer>() {
		{
			add(1);
			add(4);
			add(5);
			add(9);
		}
	};

	public static String lastName = "";
	private ViewApi api;
	private StockCondition stockCondition = new StockCondition();

	private JPanel formJPanel;

	private JTable recordTable;
	private JScrollPane recordPane;

	private JPanel pagePanel;

	private JLabel idJLabel = new JLabel("柜号");
	private JLabel nameJLabel = new JLabel("名字");
	private JTextField idJTextField = new JTextField();
	private JTextField nameJTextField = new JTextField();
	private JLabel noticejLabel = new JLabel("通知时间小于");
	private JTextField noticeField = new JTextField();

	// private JLabel out_inJLabel = new JLabel("入出库之差小于");
	// private JTextField out_inField = new JTextField();

	private JLabel isAllOutJLabel = new JLabel("是否出完");
	private JComboBox isAllComboBox = new JComboBox(new String[] { "", "是", "否" });

	private JLabel feeJLabel = new JLabel("是否产生费用");
	private JComboBox feeComboBox = new JComboBox(new String[] { "", "是", "否" });

	// private JRadioButton feeJRadioButton = new JRadioButton();
	private JLabel depotJLabel = new JLabel("仓库");
	private JComboBox depotCheckbox;

	private JPanel buttonPanel;
	private JButton searchButton;
	private JButton addButton;
	private JButton exportButton;

	private JLabel pageLabel = new JLabel();
	private JButton prebutton = new JButton("上一页");
	private JButton nextbutton = new JButton("下一页");

	public View(ViewApi api) {
		this.api = api;

		StockList lists = api.getTableList(stockCondition, new Page(1));
		fillPageLabel(lists.getPage());
		Object[][] cellData = getDataFromModel(lists.getList());
		initRecordTable(cellData);
		initForm();
		initPage();
	}

	private void initRecordTable(Object[][] data) {

		DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {

			@Override
			public boolean isCellEditable(int row, int column) {

				if (column == 0 || column == 9) {
					return false;
				}
				return true;
			}

			@Override
			public void setValueAt(Object aValue, int row, int column) {

				if (integerColumn.contains(column) && aValue instanceof String) {

					Integer v = 0;
					try {
						v = Integer.valueOf((String) aValue);
					} catch (java.lang.NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "请输入数字");
						return;
					}
					super.setValueAt(v, row, column);
				} else if (11 == column && aValue instanceof String) {
					Float v = 0f;
					try {
						v = Float.valueOf((String) aValue);
					} catch (java.lang.NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "请输入数字");
						return;
					}
					super.setValueAt(v, row, column);
				} else {
					super.setValueAt(aValue, row, column);
				}
			}
		};

		tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getColumn() == -1 || e.getColumn() >= 13 || e.getColumn() == 9 || e.getColumn() == 0) {
					return;
				}
				if (e.getColumn() == 4) {
					recordTable.setValueAt(recordTable.getValueAt(e.getFirstRow(), 4), e.getFirstRow(), 5);

				}
				SaveButtonCellRendererAndEditor buttonCellRendererAndEditor = (SaveButtonCellRendererAndEditor) recordTable
						.getCellRenderer(e.getFirstRow(), 13);
				buttonCellRendererAndEditor.addRowButton(e.getFirstRow());
				recordTable.setValueAt("---", e.getFirstRow(), 13);

			}
		});

		recordTable = new JTable(tableModel);
		recordTable.setRowHeight(30);
		recordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		recordTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				int row = recordTable.rowAtPoint(e.getPoint());
				if (row >= 0) {
					recordTable.setRowSelectionInterval(row, row);
				}

				if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 2) {
					int r = recordTable.getSelectedRow();
					if (r > -1) {
						int ret = JOptionPane.showConfirmDialog(null, "是否确认 删除", " ", JOptionPane.YES_NO_OPTION);
						if (ret == JOptionPane.YES_OPTION) {
							api.removeTable(getModelFromData(r));
							doQuery(new Page(getCurPage()));
						}
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {
					int c = recordTable.columnAtPoint(e.getPoint());
					int r = recordTable.rowAtPoint(e.getPoint());
					if (c == 9) {
						Integer key = (Integer) recordTable.getValueAt(r, 0);
						if (key == null) {
							JOptionPane.showMessageDialog(null, "请先保存 再出库");
						} else {
							String detail = (String) recordTable.getValueAt(r, 14);
							OutStockDialog dialog = new OutStockDialog(key, detail, r);
							dialog.setVisible(true);
						}
					}
				}
			}
		});
		// http://www.chka.de/swing/table/faq.html
		// recordTable.setAutoCreateColumnsFromModel(true);
		constructRenderAndEditor();
	}

	private void constructRenderAndEditor() {

		recordTable.getColumnModel().getColumn(14).setMinWidth(0);
		recordTable.getColumnModel().getColumn(14).setMaxWidth(0);
		recordTable.getColumnModel().getColumn(14).setPreferredWidth(0);
		recordTable.getColumnModel().getColumn(0).setMinWidth(0);
		recordTable.getColumnModel().getColumn(0).setMaxWidth(0);
		recordTable.getColumnModel().getColumn(0).setPreferredWidth(0);

		recordTable.setDefaultRenderer(Object.class, new ColorRenderer());

		SaveButtonCellRendererAndEditor buttonCellRendererAndEditor = new SaveButtonCellRendererAndEditor(
				new JCheckBox());
		recordTable.getColumnModel().getColumn(13).setCellRenderer(buttonCellRendererAndEditor);
		recordTable.getColumnModel().getColumn(13).setCellEditor(buttonCellRendererAndEditor);

		recordTable.getColumnModel().getColumn(6).setCellRenderer(new DateTableCellRenderer());
		DateCellEditor inDateCellEditor = new DateCellEditor(new JCheckBox());
		inDateCellEditor.setClickCountToStart(1);
		recordTable.getColumnModel().getColumn(6).setCellEditor(inDateCellEditor);

		recordTable.getColumnModel().getColumn(8).setCellRenderer(new DateTableCellRenderer());
		DateCellEditor noticeDateCellEditor = new DateCellEditor(new JCheckBox());
		noticeDateCellEditor.setClickCountToStart(1);
		recordTable.getColumnModel().getColumn(8).setCellEditor(noticeDateCellEditor);

		JComboBox box10 = new JComboBox(api.getAllDepotNames());
		recordTable.getColumnModel().getColumn(10).setCellEditor(new DefaultCellEditor(box10));

		JComboBox box7 = new JComboBox(api.getInReasons());
		recordTable.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(box7));

	}

	private void initForm() {

		idJLabel.setPreferredSize(new Dimension(30, 30));
		nameJLabel.setPreferredSize(new Dimension(30, 30));
		idJTextField.setPreferredSize(new Dimension(150, 30));
		nameJTextField.setPreferredSize(new Dimension(150, 30));

		noticejLabel.setPreferredSize(new Dimension(80, 30));
		noticeField.setPreferredSize(new Dimension(150, 30));
		noticeField.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {

				final JDialog dlg = new JDialog();

				getDateDialog(dlg, noticeField, new CallBack() {

					@Override
					public void callback(Date dateStr) {
						dlg.setVisible(false);
						dlg.dispose();
					}
				});
			}
		});

		// out_inJLabel.setPreferredSize(new Dimension(100, 30));
		// out_inField.setPreferredSize(new Dimension(50, 30));
		depotCheckbox = new JComboBox(api.getAllDepotNames());

		addButton = new JButton("新增");
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tableModel = (DefaultTableModel) recordTable.getModel();
				tableModel.insertRow(0, getNewEmptyData());

				SaveButtonCellRendererAndEditor buttonCellRendererAndEditor = (SaveButtonCellRendererAndEditor) recordTable
						.getCellRenderer(0, 13);
				buttonCellRendererAndEditor.updateRowButtonADD(0);
				recordTable.setRowSelectionInterval(0, 0);
				recordTable.setValueAt("dd", 0, 3);
			}
		});

		searchButton = new JButton("查询");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetStockCondition();
				doQuery(new Page(1));
			}
		});

		exportButton = new JButton("导出当前查询");
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				api.doExport(stockCondition);
			}
		});
	}

	private void doQuery(Page page) {

		StockList lists = api.getTableList(stockCondition, page);
		fillPageLabel(lists.getPage());
		Object[][] cellData = getDataFromModel(lists.getList());
		DefaultTableModel defaultTableModel = (DefaultTableModel) recordTable.getModel();
		defaultTableModel.setDataVector(cellData, columnNames);
		constructRenderAndEditor();
		defaultTableModel.fireTableDataChanged();
		setPageButtonStatus();
	}

	private void setPageButtonStatus() {
		if (getCurPage() > 1) {
			prebutton.setEnabled(true);
		} else {
			prebutton.setEnabled(false);
		}
		if (getCurPage() >= getTotalPage()) {
			nextbutton.setEnabled(false);
		} else {
			nextbutton.setEnabled(true);
		}
	}

	private void initPage() {

		// prebutton.setEnabled(false);
		setPageButtonStatus();
		prebutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int p = getCurPage();
				doQuery(new Page(p - 1));
			}
		});

		nextbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int p = getCurPage();
				doQuery(new Page(p + 1));
			}
		});

	}

	private int getCurPage() {
		String[] s = pageLabel.getText().split(" ");
		return Integer.valueOf(s[1]);
	}

	private int getTotalPage() {
		String[] s = pageLabel.getText().split(" ");
		return Integer.valueOf(s[4]);
	}

	private Object[] getNewEmptyData() {
		StockRecord emptyRecord = new StockRecord();
		emptyRecord.setCustomer(lastName);
		List<StockRecord> rs = new ArrayList<StockRecord>();
		rs.add(emptyRecord);
		return getDataFromModel(rs)[0];

	}

	private Object[][] getDataFromModel(List<StockRecord> list) {

		Object[][] cellData = new Object[list.size()][columnNames.length];
		for (int i = 0; i < list.size(); i++) {
			StockRecord r = list.get(i);

			int j = 0;
			cellData[i][j++] = r.getKey();
			cellData[i][j++] = r.getId();
			cellData[i][j++] = r.getBreed();
			cellData[i][j++] = r.getCustomer();
			cellData[i][j++] = r.getInStock();
			cellData[i][j++] = r.getInRealStock();
			cellData[i][j++] = r.getInDate();
			cellData[i][j++] = r.getInReason();
			cellData[i][j++] = r.getNoticeDate();
			cellData[i][j++] = r.getOutStock();
			cellData[i][j++] = api.getDepotName(r.getDepot());
			cellData[i][j++] = r.getFee();
			cellData[i][j++] = r.getRemark();
			j++;
			cellData[i][j++] = objectsToStgring(r.getOutDetail());
		}
		return cellData;
	}

	private StockRecord getModelFromData(Integer r) {

		StockRecord record = new StockRecord();
		int i = 0;
		record.setKey((Integer) (recordTable.getValueAt(r, i++)));
		record.setId((Integer) recordTable.getValueAt(r, i++));
		record.setBreed((String) recordTable.getValueAt(r, i++));
		record.setCustomer((String) recordTable.getValueAt(r, i++));
		record.setInStock((Integer) recordTable.getValueAt(r, i++));
		record.setInRealStock((Integer) recordTable.getValueAt(r, i++));
		record.setInDate((Date) recordTable.getValueAt(r, i++));
		record.setInReason((String) recordTable.getValueAt(r, i++));
		record.setNoticeDate((Date) recordTable.getValueAt(r, i++));
		record.setOutStock((Integer) recordTable.getValueAt(r, i++));
		record.setDepot(api.getDepotValue((String) recordTable.getValueAt(r, i++)));
		record.setFee((Float) recordTable.getValueAt(r, i++));
		record.setRemark((String) recordTable.getValueAt(r, i++));

		return record;
	}

	private void resetStockCondition() {

		stockCondition = new StockCondition();
		stockCondition.setId(idJTextField.getText());
		stockCondition.setCustomer(nameJTextField.getText());
		stockCondition.setNoticeMaxDate(noticeField.getText());
		stockCondition.setIsAllOut(fillCombox(isAllComboBox));
		stockCondition.setIsfee(fillCombox(feeComboBox));
		stockCondition.setDepot(api.getDepotValue((String) depotCheckbox.getSelectedItem()));
	}

	private Boolean fillCombox(JComboBox box) {
		if (box.getSelectedIndex() == 0) {
			return null;
		} else if (box.getSelectedIndex() == 1) {
			return true;
		} else if (box.getSelectedIndex() == 2) {
			return false;
		} else {
			return null;
		}

	}

	private void fillPageLabel(Page page) {
		pageLabel.setText("当前第 " + page.getCurPage() + " 页 一共 " + page.getTotalPage() + " 页  一共 " + page.getTotalSize()
				+ " 条数据");
	}

	private void getDateDialog(JDialog dlg, final Object component, CallBack callBack) {
		CalendarPanel p = new CalendarPanel(component, "yyyy-MM-dd", dlg);
		dlg.setSize(251, 270);
		dlg.setResizable(false);
		dlg.setLocationRelativeTo(null);
		p.setCallBack(callBack);
		p.initCalendarPanel();
		dlg.setModal(true);
		dlg.add(p);
		dlg.setVisible(true);
		dlg.setPreferredSize(new Dimension(251, 245));
	}

	public JPanel getView() {

		formJPanel = new JPanel();
		FlowLayout layoutForm = new FlowLayout(FlowLayout.CENTER);
		formJPanel.setLayout(layoutForm);
		formJPanel.add(idJLabel);
		formJPanel.add(idJTextField);
		formJPanel.add(nameJLabel);
		formJPanel.add(nameJTextField);
		formJPanel.add(noticejLabel);
		formJPanel.add(noticeField);
		formJPanel.add(isAllOutJLabel);
		formJPanel.add(isAllComboBox);
		formJPanel.add(feeJLabel);
		formJPanel.add(feeComboBox);
		formJPanel.add(depotJLabel);
		formJPanel.add(depotCheckbox);

		buttonPanel = new JPanel();
		buttonPanel.add(searchButton);
		buttonPanel.add(addButton);
		buttonPanel.add(exportButton);

		recordPane = new JScrollPane(recordTable);

		pagePanel = new JPanel();
		FlowLayout pageLayout = new FlowLayout(FlowLayout.CENTER);
		pagePanel.setLayout(pageLayout);
		pagePanel.add(pageLabel);
		pagePanel.add(prebutton);
		pagePanel.add(nextbutton);

		JPanel mainPanel = new JPanel();

		Box horizontalBox = Box.createVerticalBox();
		horizontalBox.add(formJPanel);
		horizontalBox.add(buttonPanel);
		horizontalBox.add(recordPane);
		horizontalBox.add(pagePanel);

		mainPanel.add(horizontalBox);
		BoxLayout boxLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		// FlowLayout boxLayout = new FlowLayout();
		mainPanel.setLayout(boxLayout);

		// mainPanel.add(formJPanel);
		// mainPanel.add(recordPane);
		// mainPanel.add(pagePanel);

		return mainPanel;
	}

	class SaveButtonCellRendererAndEditor extends DefaultCellEditor implements TableCellRenderer {

		private Set<Integer> savelist = new HashSet<Integer>();

		private JButton button = new JButton("保存");
		{
			button.setVisible(false);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// System.out.println(recordTable.getSelectedColumn() +
					// " and  " + recordTable.getSelectedRow());

					int r = recordTable.getSelectedRow();
					StockRecord stockRecord = getModelFromData(r);
					api.saveTable(stockRecord);

					SaveButtonCellRendererAndEditor buttonCellRendererAndEditor = (SaveButtonCellRendererAndEditor) recordTable
							.getCellRenderer(r, 13);
					buttonCellRendererAndEditor.moveRowButton(r);

					if (recordTable.getValueAt(r, 0) == null) {
						// doQuery(new Page(1));
						recordTable.setValueAt(stockRecord.getKey(), r, 0);
					}

				}
			});

		}

		public void addRowButton(Integer row) {
			savelist.add(row);
		}

		public void updateRowButtonADD(Integer i) {

			List<Integer> oldList = new ArrayList<Integer>();
			oldList.addAll(savelist);
			savelist.clear();
			savelist.add(i);
			for (Integer row : oldList) {
				if (row >= i) {
					savelist.add(++row);
				}
			}
		}

		protected void moveRowButton(Integer row) {
			savelist.remove(row);
		}

		public SaveButtonCellRendererAndEditor(JCheckBox checkBox) {
			super(checkBox);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			if (savelist.contains(row)) {
				return button;
			}
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (savelist.contains(row)) {
				return button;
			}
			return null;
		}
	}

	class DateTableCellRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			JTextField jLabel = new JTextField();
			if (isSelected) {
				jLabel.setForeground(table.getSelectionForeground());
				jLabel.setBackground(table.getSelectionBackground());
			} else {
				jLabel.setForeground(table.getForeground());
				jLabel.setBackground(getColor(row));
			}

			if (value != null) {
				jLabel.setText(value.toString());
				return jLabel;
			}
			return jLabel;
		}
	}

	class DateCellEditor extends DefaultCellEditor {
		public DateCellEditor(JCheckBox checkBox) {
			super(checkBox);
		}

		private Date curdate;

		@Override
		public Object getCellEditorValue() {
			return curdate;
		}

		@Override
		public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected,
				final int row, final int column) {

			final JLabel jLabel = new JLabel();
			if (value != null) {
				jLabel.setText(value.toString());
				curdate = (Date) value;
			} else {
				curdate = null;
			}

			jLabel.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						final JDialog dlg = new JDialog();
						getDateDialog(dlg, jLabel, new CallBack() {
							@Override
							public void callback(Date date) {
								curdate = date;
								dlg.setVisible(false);
								dlg.dispose();
							}
						});
					}
				}

			});
			return jLabel;
		}
	};

	class DeleteButtonCellRendererAndEditor extends DefaultCellEditor implements TableCellRenderer {

		private JButton button = new JButton("完结");

		{
			button.setVisible(true);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					int ret = JOptionPane.showConfirmDialog(null, "是否确认 完结", " ", JOptionPane.YES_NO_OPTION);
					if (ret == JOptionPane.YES_OPTION) {

						int r = recordTable.getSelectedRow();
						api.removeTable(getModelFromData(r));
						doQuery(new Page(getCurPage()));
					}
				}
			});

		}

		public DeleteButtonCellRendererAndEditor(JCheckBox checkBox) {
			super(checkBox);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			JButton sbutton = new JButton("完结");
			return sbutton;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			return button;

		}
	}

	class OutStockDialog extends JDialog {

		private JTextField outField = new JTextField();
		private JTextField dateField = new JTextField();
		private JButton button = new JButton("出库");
		private JLabel detailJLabel = new JLabel();

		private Integer key;
		private String detail;
		private JPanel contentPanel;

		public OutStockDialog(final Integer key, String detail, final int r) {
			this.key = key;
			this.detail = detail;

			setLocationRelativeTo(null);
			setSize(250, 280);

			outField.setPreferredSize(new Dimension(50, 30));
			dateField.setPreferredSize(new Dimension(80, 30));
			detailJLabel.setText(detail);

			contentPanel = new JPanel();

			contentPanel.add(detailJLabel);

			JPanel inputbuttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			inputbuttonPanel.add(outField);
			dateField.setText(new java.sql.Date(new Date().getTime()).toString());
			inputbuttonPanel.add(dateField);
			inputbuttonPanel.add(button);

			contentPanel.add(inputbuttonPanel);

			setContentPane(contentPanel);

			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					int ret = JOptionPane.showConfirmDialog(null, "是否确认 出库 " + outField.getText(), " ",
							JOptionPane.YES_NO_OPTION);
					if (ret == JOptionPane.YES_OPTION) {

						Integer out = Integer.valueOf(outField.getText());
						StockRecord s = api.addOutStock(key, out, dateField.getText());
						recordTable.setValueAt(s.getOutStock(), r, 9);
						recordTable.setValueAt(objectsToStgring(s.getOutDetail()), r, 14);
						setVisible(false);
						dispose();
					}
				}
			});
		}
	}

	class ColorRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {

			setText(value != null ? value.toString() : "");
			setValue(value);

			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(getColor(row));
			}

			return this;
		}
	}

	private Color getColor(int row) {
		Float v = (Float) recordTable.getValueAt(row, 11);

		if (v != null && v > 0) {
			return Color.gray;
		} else {
			return Color.white;
		}
	}

	private String objectsToStgring(Object[] object) {
		if (object == null) {
			return null;
		}
		StringBuilder s = new StringBuilder();
		s.append("<html><body>");
		for (Object o : object) {
			String[] ss = o.toString().split(";");
			s.append("<p>").append("出库：").append(ss[0]).append("&nbsp;&nbsp;&nbsp;&nbsp;备注：").append(ss[1])
					.append("</p>");
		}
		s.append("</body></html>");
		return s.toString();
	}

}
