/*
 * DataScan.java
 * Author: Matt Adshead
 * Date: 13 Nov 2013
 * 
 * Object containing data values for one 360 degree scan with the sensor.
 *  
 * This object class contains the raw sensor values collected during one 360
 * degree sensor scan. It consists of:
 * - Unique ID for this scan. (Integer)
 * - Value for sample rate, representing how many degrees are between each sample. (Integer)
 * - Array of data containing raw sensor values. (Double[])
 */

package mattadshead.swansea3.dissertation.structures;

import java.awt.Point;
import java.awt.geom.Point2D;

public class DataScan {
	//Symbolic constant for default sample rate value.
	private final int DEFAULT_SAMPLE_RATE = 1;
	
	//Sample rate in degrees for scan.
	private int sampleRate;
	//Array containing data values.
	private int[] scanValues;
	
	//Robot state at time of scan collection.
	private Point2D.Double relPos;
	private double heading;
	
	/*
	 * Class constructor for ID only.
	 * Parameters:
	 * scanID_p - Unique scan ID.
	 * Returns:
	 * None
	 */
	public DataScan(Point2D.Double relPos_p, double heading_p) {
		sampleRate = DEFAULT_SAMPLE_RATE;
		scanValues = null;
		relPos = relPos_p;
		heading = heading_p;
	}
	
	/*
	 * Class constructor for ID and sample rate.
	 * Parameters:
	 * scanID_p - Unique scan ID.
	 * sampleRate_p - Sample rate in degrees for scan.
	 * Returns:
	 * None
	 */
	public DataScan(int sampleRate_p, Point2D.Double relPos_p, double heading_p) {
		if ((360 % sampleRate_p) == 0) {
			sampleRate = sampleRate_p;
			scanValues = new int[360 / sampleRate];
			scanValues = null;
			relPos = relPos_p;
			heading = heading_p;
		} else {
			System.out.println("DataScan Constructor - Error: Invalid sample rate.");
		}
	}
	
	/*
	 * Class constructor for ID and data array.
	 * Parameters:
	 * scanID_p - Unique scan ID.
	 * scanValues_p - Array of data values.
	 * Returns:
	 * None
	 */
	public DataScan(int[] scanValues_p, Point2D.Double relPos_p, double heading_p) {
		int valueNum = scanValues_p.length;
		if (360 % valueNum == 0) {
			sampleRate = 360 / valueNum;
			scanValues = new int[valueNum];
			for (int i = 0; i < valueNum; i++) {
				scanValues[i] = scanValues_p[i];
			}
			relPos = relPos_p;
			heading = heading_p;
		} else {
			System.out.println("DataScan Constructor - Error: Invalid array size.");
		}
	}
	
	/*
	 * Access method for sample rate.
	 * Parameters:
	 * None
	 * Returns:
	 * Integer representing sample rate.
	 */
	public int getSRate() {
		return(sampleRate);
	}
	
	/*
	 * Mutator method for sample rate.
	 * Parameters:
	 * scanID_p - New sample rate value.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setSRate(int sampleRate_p) {
		if (scanValues == null) {
			sampleRate = sampleRate_p;
			return true;
		} else {
			System.out.println("DataScan > setSRate - Error: Value array already initialised.");
			return false;
		}
	}
	
	/*
	 * Access method for data array.
	 * Parameters:
	 * None
	 * Returns:
	 * Integer array containing data values.
	 */
	public int[] getValues() {
		return(scanValues);
	}
	
	/*
	 * Mutator method for values array.
	 * Parameters:
	 * scanID_p - New values array.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setValues(int[] scanValues_p) {
		if ((sampleRate > 0) && (scanValues_p.length == (360 / sampleRate))) {
			scanValues = scanValues_p;
			return true;
		} else {
			System.out.println("DataScan > setValues - Error: Sample rate does not match array size.");
			return false;
		}
	}
	
	/*
	 * Access method for individual data array value.
	 * Parameters:
	 * None
	 * Returns:
	 * Integer representing data array value.
	 */
	public int getValue(int index) {
		if ((!(scanValues==null)) && (index >= 0) && (index < (360 / sampleRate))) {
			return(scanValues[index]);
		} else {
			if (!(scanValues==null)) {
				System.out.println("DataScan > setValue - Error: Index out of range.");
			} else {
				System.out.println("DataScan > setValue - Error: Array not initialised.");
			}
			return 0;
		}
	}
	
	/*
	 * Mutator method for individual array value.
	 * Parameters:
	 * scanID_p - New value.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setValue(int index, int value_p) {
		if ((!(scanValues==null)) && (index >= 0) && (index < (360 / sampleRate))) {
			scanValues[index] = value_p;
			return true;
		} else {
			if (!(scanValues==null)) {
				System.out.println("DataScan > setValue - Error: Index out of range.");
			} else {
				System.out.println("DataScan > setValue - Error: Array not initialised.");
			}
			return false;
		}
	}
	
	/*
	 * Method to get size of scan array.
	 * Parameters:
	 * None
	 * Returns:
	 * Integer representing size of scan array.
	 */
	public int size() {
		return(scanValues.length);
	}
	
	/*
	 * Accessor method for scan's relative robot position.
	 * Parameters:
	 * None
	 * Returns:
	 * Point object representing relative robot position.
	 */
	public Point2D.Double getRelativePosition() {
		return(relPos);
	}
	
	/*
	 * Mutator method for scan's relative robot position.
	 * Parameters:
	 * relPos_p - Point representing position to set.
	 * Returns:
	 * None
	 */
	public boolean setRelativePosition(Point2D.Double relPos_p) {
		relPos = relPos_p;
		return true;
	}
	
	/*
	 * Accessor method for robot heading.
	 * Parameters:
	 * None
	 * Returns:
	 * Double representing scan robot heading.
	 */
	public double getHeading() {
		return(heading);
	}
	
	/*
	 * Mutator method for relative robot position.
	 * Parameters:
	 * heading_p - New heading for scan.
	 * Returns:
	 * None
	 */
	public boolean setHeading(double heading_p) {
		heading = heading_p;
		return true;
	}
}