//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.06 at 12:57:16 PM GMT 
//


package dataminer.diary;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * <p>Java class for EntryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EntryType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *       &lt;attribute name="day" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *       &lt;attribute name="month" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="year" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntryType", propOrder = {
    "value"
})
public class EntryType implements Comparable<EntryType> {

	
	private static Logger logger =  LogManager.getLogger( EntryType.class );

	
    @XmlValue
    protected String value;
    @XmlAttribute(name = "day")
    protected BigInteger day;
    @XmlAttribute(name = "month")
    protected String month;
    @XmlAttribute(name = "year")
    protected BigInteger year;

    public String toString()
    {
    	return "D:" + day +  " M:" + month + " Y:" + year;
    }
    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the day property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDay() {
        return day;
    }

    /**
     * Sets the value of the day property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDay(BigInteger value) {
        this.day = value;
    }

    /**
     * Gets the value of the month property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets the value of the month property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMonth(String value) {
        this.month = value;
    }

    /**
     * Gets the value of the year property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setYear(BigInteger value) {
        this.year = value;
    }

    
    
	@Override
	public int compareTo(EntryType e) {
		// TODO Auto-generated method stub
			int yearComp = this.year.compareTo(e.year);
			int dayComp = this.day.compareTo(e.day);
			int mon1  = DateUtils.mapMonth(getMonth());
			int mon2 = DateUtils.mapMonth(e.getMonth());
			
			int returnVal = 0;
			if (yearComp != 0)
				returnVal = yearComp;
			else // same Year
			{
				if (mon1 < mon2)
					returnVal = -1;
				else if (mon1 > mon2)
					returnVal = 1;
				else
				{
					// same mon
					returnVal =  dayComp;					
				}
						
			}
			
			logger.debug(" Comparing " + this.toString() + " vs " + e.toString() + "VAL" + returnVal);	
			
		 return returnVal;
	}

}
