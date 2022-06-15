package dataminer.diary.ui;

import java.awt.Color;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dataminer.diary.EntryType;
import dataminer.diary.DateUtils;

public class DiaryTable extends JTable {

	private static final int STRING_COL = 1;
    private static final int DATE_COL = 0;
    private final DiaryModel model = new DiaryModel();
    private JTable diaryTable;

    public DiaryTable(List<EntryType> eList)
    {
    	populateTable(eList);
    	
    	diaryTable = new JTable(model);
    	diaryTable.getColumn("Date").setMinWidth(140);
    	diaryTable.getColumn("Date").setMaxWidth(160);
    	
    	//diaryTable.getColumn("Date").setWidth(120);
    	//diaryTable.getColumnModel().getColumn(0).setPreferredWidth(150);
    	
    	diaryTable.getTableHeader().setOpaque(false);
    	diaryTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
    	
    }
    
    public JTable getDiaryTable() {
		return diaryTable;
	}

	public void setDiaryTable(JTable diaryTable) {
		this.diaryTable = diaryTable;
	}

	
    
    public DiaryModel getModel() {
    	
		return model;
	}

	
	private static class DiaryModel extends DefaultTableModel {

        private static final long serialVersionUID = -2106821031963077540L;
		
		private final String[] columnNames = {"Date", "Entry"};

        @Override
        public Class<?> getColumnClass(int col) {
            if (col == STRING_COL) {
                return String.class;
            } else if (col == DATE_COL) {
                return Date.class;
            }
            return super.getColumnClass(col);
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }
    }
	
	
	public void populateTable(List<EntryType> eList )
	{
		model.setRowCount(0); // so we need to clear the rows before adding New Rows!
		
		for (EntryType et : eList)
		{
			//System.out.println(" Adding " + Utils.formDate(et) + " >>> " + et.getValue());
			model.addRow(new Object[]{ DateUtils.formDate(et), et.getValue() } );
		}
		

	}
	
	
	
	
}
