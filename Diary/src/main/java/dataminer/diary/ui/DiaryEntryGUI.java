package dataminer.diary.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

import dataminer.diary.CacheHandler;
import dataminer.diary.DateUtils;
import dataminer.diary.EntryType;
import dataminer.diary.dyndb.EntryMigration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DiaryEntryGUI extends JPanel {
	
	private static Logger logger =  LogManager.getLogger( DiaryEntryGUI.class );

	private JTextField textField;
	private JXDatePicker datePicker;
	private CacheHandler _ch = null;
	private DiaryTable _dt;
	public CacheHandler getCacheHandler() {
		return _ch;
	}
	
	/**
	 * Create the panel.
	 */
	public DiaryEntryGUI(DiaryTable dt , CacheHandler ch) {
		
		
		//setPreferredSize(new Dimension(640, 480));
		_dt = dt;
		_ch = ch;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		datePicker = new JXDatePicker();
		datePicker.setDate(Calendar.getInstance().getTime());
		GridBagConstraints gbc_dateField = new GridBagConstraints();
		gbc_dateField.gridwidth = 3;
		gbc_dateField.insets = new Insets(0, 0, 5, 5);
		gbc_dateField.fill = GridBagConstraints.VERTICAL;
		gbc_dateField.gridx = 2;
		gbc_dateField.gridy = 2;
		add(datePicker, gbc_dateField);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 3;
		gbc_textField.gridheight = 3;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 3;
		add(textField, gbc_textField);
		textField.setColumns(30);
		
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				

		//		DiaryEntryGUI.this.getParent().setVisible(false);
		//		DiaryEntryGUI.this.getRootPane().setVisible(false);
				textField.setText("");
				datePicker.setDate(Calendar.getInstance().getTime());
				Window w = SwingUtilities.getWindowAncestor(DiaryEntryGUI.this);
				w.setVisible(false);
			}
		});
		
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridwidth = 2;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 6;
		add(cancelButton, gbc_btnNewButton);
		
		JButton btnSave = new JButton("Save");
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.insets = new Insets(0, 0, 5, 5);
		gbc_btnSave.gridx = 4;
		gbc_btnSave.gridy = 6;
		add(btnSave, gbc_btnSave);

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				logger.debug(" Need to save new entry ");

				List<EntryType> entries = _ch.getEntryList();
				System.out.println(" entries " + entries.size());
				
				EntryType entry = new EntryType();
				Date selected = datePicker.getDate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(selected);
				entry.setDay(BigInteger.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
				entry.setMonth(DateUtils.mapMonth((cal.get(Calendar.MONTH))));
				entry.setYear(BigInteger.valueOf(cal.get(Calendar.YEAR)));
				entry.setValue(textField.getText());
				
				entries.add(entry);
								
				Collections.sort(_ch.getEntryList());
				_dt.populateTable(_ch.getEntryList());
				
				// Needs to be handled by the CacheHandler...
				EntryMigration em = new EntryMigration();
				DynamoDbClient ddb = em.initializeDDBClient();
			    DynamoDbEnhancedClient edb = em.initializeEnhancedClient(ddb);
			    em.putRecord(edb, DiaryGUI.userName, entry);
			    
				// JUN 2022 - dont bother to unmarshal TEST
				// _ch.marshal(); // writes to default file
				
				logger.debug(" entries " + entries.size());
				

				// so it will update the table WHEN the date is AFTER the others
				// but it will not appear IF IT IS NOT!
				textField.setText("");
				datePicker.setDate(Calendar.getInstance().getTime());
				
				Window w = SwingUtilities.getWindowAncestor(DiaryEntryGUI.this);
				w.setVisible(false);
				
			}
            
          });
		
		
		
		
	}

}
