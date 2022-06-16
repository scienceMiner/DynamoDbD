package dataminer.diary.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;
import java.util.regex.PatternSyntaxException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

import dataminer.diary.CacheHandler;
import dataminer.diary.DateUtils;
import dataminer.diary.dyndb.EntryMigration;

public class DiaryGUI {

	private static Logger logger =  LogManager.getLogger( DiaryGUI.class );

	private JFrame frmDiary;
	private JFrame frmEntry;
	private JButton btnExport;
	private JButton Find;
	/**
	 * @wbp.nonvisual location=-391,41
	 */
	//private final JScrollBar scrollBar = new JScrollBar();
	private Component verticalStrut;
	private JTextField textField;
	private JScrollPane scrollPane;
	private JTable table;
	private Component horizontalStrut_1;
	private JButton btnReset;
	private JButton btnSameDay;
	private JXDatePicker datePicker;
	private JButton btnNewEntry;
	private DiaryEntryGUI entryDialog; // = new DiaryEntryGUI();
	private JFileChooser fc = new JFileChooser();
	private CacheHandler ch = new CacheHandler();
	private EntryMigration em = new EntryMigration();
	
	public static String initialFileNamePath = "/Users/ethancollopy/dev/ERCMBSrc/Diary/src/main/resources/output2015.xml";
	public static String userName = "ucacerc";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DiaryGUI window = new DiaryGUI();
					window.frmDiary.setVisible(true);
					window.frmEntry.setVisible(false);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DiaryGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmDiary = new JFrame();
		frmEntry = new JFrame();
		
		// Feed the cache from Dynamo db
	    ch.setEntryList( em.scan() );
	    
	    DiaryTable diaryTable = new DiaryTable(ch.getEntryList());
	    
	    /*
	     *  If we are NOT using AWS Dynamo Db
	     */
	    //  DiaryTable diaryTable = new DiaryTable(ch.populateEntryList());
	    
		table = diaryTable.getDiaryTable();
		table.setFillsViewportHeight(true);
		table.setCellSelectionEnabled(true);
		
		entryDialog = new DiaryEntryGUI(diaryTable,ch);
			
		final TableRowSorter<TableModel> sorter =
                new TableRowSorter<TableModel>(diaryTable.getModel());
		table.setRowSorter(sorter);
		
		frmDiary.setTitle("Diary");
		frmDiary.setBounds(100, 100, 900, 284);
		frmDiary.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JPanel panel = new JPanel();
		frmDiary.getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 1, 59, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{10, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 4;
		gbc_verticalStrut.gridy = 0;
		panel.add(verticalStrut, gbc_verticalStrut);

		scrollPane = new JScrollPane(table);
		scrollPane.setViewportView(table);
			
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.gridheight = 11;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		panel.add(scrollPane, gbc_scrollPane);
		
		datePicker = new JXDatePicker ();
		  
		GridBagConstraints gbc_monthPanel = new GridBagConstraints();
		gbc_monthPanel.insets = new Insets(0, 0, 5, 5);
		gbc_monthPanel.fill = GridBagConstraints.BOTH;
		gbc_monthPanel.gridx = 1;
		gbc_monthPanel.gridy = 12;
		panel.add(datePicker, gbc_monthPanel);
		
		
		horizontalStrut_1 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
		gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut_1.gridx = 0;
		gbc_horizontalStrut_1.gridy = 4;
		panel.add(horizontalStrut_1, gbc_horizontalStrut_1);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 2;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 12;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		btnReset = new JButton("Reset");
		GridBagConstraints gbc_btnReset = new GridBagConstraints();
		gbc_btnReset.insets = new Insets(0, 0, 5, 5);
		gbc_btnReset.gridx = 5;
		gbc_btnReset.gridy = 12;
		panel.add(btnReset, gbc_btnReset);
		
		btnNewEntry = new JButton("New Entry");
		GridBagConstraints gbc_btnNewEntry = new GridBagConstraints();
		gbc_btnNewEntry.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewEntry.gridx = 1;
		gbc_btnNewEntry.gridy = 13;
		panel.add(btnNewEntry, gbc_btnNewEntry);
		
		btnExport = new JButton("Export");
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 3;
		gbc_btnNewButton_1.gridy = 13;
		panel.add(btnExport, gbc_btnNewButton_1);
		
		btnSameDay = new JButton("Same Day");
		GridBagConstraints gbc_btnSameDay = new GridBagConstraints();
		gbc_btnSameDay.insets = new Insets(0, 0, 5, 5);
		gbc_btnSameDay.gridx = 4;
		gbc_btnSameDay.gridy = 13;
		panel.add(btnSameDay, gbc_btnSameDay);
		
		Find = new JButton("Find Text");
		GridBagConstraints gbc_Find = new GridBagConstraints();
		gbc_Find.insets = new Insets(0, 0, 5, 5);
		gbc_Find.gridx = 5;
		gbc_Find.gridy = 13;
		panel.add(Find, gbc_Find);
		
		
		Find.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              String text = textField.getText();
              if (text.length() == 0) {
                sorter.setRowFilter(null);
                
                
              } else {
                try {
                  sorter.setRowFilter(
                      RowFilter.regexFilter(text));
                } catch (PatternSyntaxException pse) {
                  System.err.println("Bad regex pattern");
                }
              }
            }
          });
		
		datePicker.addActionListener(new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				logger.debug ("Selected: " + datePicker.getDate ());
				Calendar cal =  Calendar.getInstance();
				cal.setTime(datePicker.getDate());
				try {
					//   sorter.setRowFilter(
					//  		 RowFilter.dateFilter(ComparisonType.EQUAL,datePicker.getDate()));

					int mon = cal.get(Calendar.MONTH);
					int day = cal.get(Calendar.DAY_OF_MONTH);
					String text = DateUtils.formDayMonth(day,mon);
					//RowFilter.regexFilter(datePicker.getDate().toString()));
					sorter.setRowFilter(
							RowFilter.regexFilter(text));
				} catch (PatternSyntaxException pse) {
					System.err.println("Bad regex pattern");
				}
			}
		});
		
		btnExport.addActionListener(new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				String newline = "\n";
				
				int returnVal = fc.showSaveDialog(panel);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                //This is where a real application would save the file.
	                logger.debug("Saving: " + file.getName() + "." + newline);
	                ch.marshal(file);
	            } else {
	                logger.debug("Save command cancelled by user." + newline);
	            }
			}
		});
	  
	  
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
				sorter.setRowFilter(null);				           
			}
            
          });
		
		
		btnNewEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				entryDialog.setVisible(true);
				frmEntry.setVisible(true);
			
				logger.debug(" NEW ENTRY button clicked ");

				Window w = SwingUtilities.getWindowAncestor(entryDialog);
				w.setVisible(true);
			}
            
          });
		
		
		frmEntry.setTitle("Enter Information: ");
		frmEntry.setBounds(250, 150, 480, 180);
		frmEntry.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);	
		frmEntry.getContentPane().add(entryDialog, BorderLayout.CENTER);
		
	}

}
