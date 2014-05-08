/*
 * Model.java
 * Author: Matt Adshead
 * Date: 6 Dec 2013
 * 
 * Model object for PC Application.
 *  
 * This object houses all data in the system.
 * It is the Model portion of the Model View Controller design pattern.
 */

package mattadshead.swansea3.dissertation.pcapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTInfo;
import mattadshead.swansea3.dissertation.structures.DataScan;
import mattadshead.swansea3.dissertation.structures.OGM;
import mattadshead.swansea3.dissertation.structures.Visualisation;

public class Model {
	//File path for saving sessions to file.
	public final String DEFAULT_SESSION_PATH = "savedSession.txt";
	
	//Address of NXT for Bluetooth communication.
	public final String NXT_ADDRESS = "00:16:53:10:16:49";
	
	//Pointer to view object.
	private View view;
	
	//leJOS NXTComm object represents a communication link to an NXT.
	private NXTComm nxtComm;
	//leJOS NXTInfo object contains information about linked NXT.
	private NXTInfo nxtInfo;
	
	//Data output stream for sending data to NXT.
	private DataOutputStream outStream;
	//Data input stream for receiving data from NXT.
	private DataInputStream inStream;
	
	//Current visualisation object.
	private Visualisation activeVis;
	
	
	/*
	 * Class constructor.
	 * Parameters:
	 * view_p - Pointer to view object for instantiation.
	 * Returns:
	 * None
	 */
	public Model(View view_p) {
		view = view_p;
	}
	
	/*
	 * Mutator method for view pointer.
	 * Parameters:
	 * view_p - Pointer to view object.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setView(View view_p) {
		view = view_p;
		return true;
	}
	
	/*
	 * Access method for view pointer.
	 * Parameters:
	 * None
	 * Returns:
	 * Pointer to view object.
	 */
	public View getView() {
		return(view);
	}
	
	/*
	 * Mutator method for NXTComm object.
	 * Parameters:
	 * nxtComm_p - New nxtComm object.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setNXTComm(NXTComm nxtComm_p) {
		nxtComm = nxtComm_p;
		return true;
	}
	
	/*
	 * Access method for NXTComm object.
	 * Parameters:
	 * None
	 * Returns:
	 * NXTComm object.
	 */
	public NXTComm getNXTComm() {
		return(nxtComm);
	}
	
	/*
	 * Mutator method for NXTInfo object.
	 * Parameters:
	 * nxtInfo_p - New nxtInfo object.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setNXTInfo(NXTInfo nxtInfo_p) {
		nxtInfo = nxtInfo_p;
		return true;
	}
	
	/*
	 * Access method for NXTInfo object.
	 * Parameters:
	 * None
	 * Returns:
	 * NXTInfo object.
	 */
	public NXTInfo getNXTInfo() {
		return(nxtInfo);
	}
	
	/*
	 * Mutator method for data output stream.
	 * Parameters:
	 * dos_p - New data output stream object.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setOutStream(DataOutputStream dos_p) {
		outStream = dos_p;
		return true;
	}
	
	/*
	 * Access method for data output stream.
	 * Parameters:
	 * None
	 * Returns:
	 * Data output stream object.
	 */
	public DataOutputStream getOutStream() {
		return(outStream);
	}
	
	/*
	 * Mutator method for data input stream.
	 * Parameters:
	 * dis_p - New data input stream object.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setInStream(DataInputStream dis_p) {
		inStream = dis_p;
		return true;
	}
	
	/*
	 * Access method for data input stream.
	 * Parameters:
	 * None
	 * Returns:
	 * Data input stream object.
	 */
	public DataInputStream getInStream() {
		return(inStream);
	}
	
	public Visualisation getVisualisation() {
		return(activeVis);
	}
	
	public boolean setVisualisation(Visualisation vis_p) {
		activeVis = vis_p;
		view.insertVisualisation(activeVis);
		return true;
	}
}