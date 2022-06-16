package dataminer.diary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dataminer.diary.ui.DiaryGUI;

public class CacheHandler {
	
	private static Logger logger =  LogManager.getLogger( CacheHandler.class );

	File _diaryFile;
	List<EntryType> _entryList;
	
	public File getDiaryFile() {
		return _diaryFile;
	}

	public void setDiaryFile(File diaryFile) {
		this._diaryFile = diaryFile;
	}

	public List<EntryType> getEntryList() {
		Collections.sort(_entryList);
		
		return _entryList;
	}

	public void setEntryList(List<EntryType> _entryList) {
		this._entryList = _entryList;
	}
	
	public List<EntryType> unmarshal()
	{
		try {

			if (_diaryFile != null)
			{
				JAXBContext jaxbContext = JAXBContext.newInstance(Diary.class);

				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Diary diary = (Diary) jaxbUnmarshaller.unmarshal(getDiaryFile());

				setEntryList(diary.getEntry());

			}

		} catch (JAXBException e) {

			e.printStackTrace();
		}
		
		return _entryList;
	}
	
	public void marshal()
	{
		Diary diary = new Diary();
		diary.setEntry(_entryList);
		
		marshal(diary);
	}
	
	public void marshal(File file)
	{
		logger.debug(" marshal to file " + _entryList.size() ) ;
		Diary diary = new Diary();
		
		diary.setEntry(_entryList);
		
		marshal(diary,file);
	}
	
	public void marshal(Diary diary)
	{
		File f1 = getDiaryFile() ; 
		logger.debug( " File: " + f1.toString() );
		marshal(diary,getDiaryFile());
	}
	
	public void marshal(Diary diary, File file)
	{

        try {            
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(diary.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
           // marshaller.marshal(diary, System.out);
            OutputStream os = new FileOutputStream( file );
            marshaller.marshal( diary, os );

        } catch (FileNotFoundException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
	}
	
	public List<EntryType> populateEntryList()
	{
		setDiaryFile(new File(DiaryGUI.initialFileNamePath));
		
		unmarshal();
		
		return getEntryList();
	}
	
	public List<EntryType> populateEntryList(String fileNamePath )
	{
		
		setDiaryFile(new File(fileNamePath));
		unmarshal();
		
		return getEntryList();
		
	}
	
	
	public static void main(String[] args) {
 	
		CacheHandler cacheHandler = new CacheHandler();
		List<EntryType> entryList = cacheHandler.populateEntryList(DiaryGUI.initialFileNamePath);
		
		if (entryList != null)
		{
			for (EntryType e : entryList)
			{ 
				
				BigInteger year = BigInteger.valueOf(2016);
				if (year.equals(e.getYear()))
					logger.info("DAY:" + e.getDay() + "MON:" + e.getMonth() + "YEAR:" + e.getYear() + " >>>" + e.getValue());

			}

			for (EntryType e : entryList)
			{ 
				String cin = "CIN";
				if (e.getValue() != null && e.getValue().contains(cin))
					logger.info("DAY:" + e.getDay() + "MON:" + e.getMonth() + "YEAR:" + e.getYear() + " >>CIN>>" + e.getValue());

			}

		}
 
	}
	
}