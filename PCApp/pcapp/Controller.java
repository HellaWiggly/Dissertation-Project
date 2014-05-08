/*
 * Controller.java
 * Author: Matt Adshead
 * Date: 6 Dec 2013
 * 
 * Controller object for PC Application.
 *  
 * This object deals with all data manipulation functions in the system.
 * It is the Controller portion of the Model View Controller design pattern.
 */

package mattadshead.swansea3.dissertation.pcapp;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import lejos.robotics.navigation.Pose;
import mattadshead.swansea3.dissertation.structures.DataScan;
import mattadshead.swansea3.dissertation.structures.MoveCommand;
import mattadshead.swansea3.dissertation.structures.OGM;
import mattadshead.swansea3.dissertation.structures.RawDataDisplay;
import mattadshead.swansea3.dissertation.structures.Visualisation;

public class Controller {	
	//Pointer to model class for data access.
	private Model model;
	
	
	/*
	 * Class constructor.
	 * Parameters:
	 * model_p - Pointer to model object for instantiation.
	 * Returns:
	 * None
	 */
	public Controller(Model model_p) {
		model = model_p;
	}
	
	/*
	 * Method to recieve a pose object from the pose tracker on the NXT
	 * via the bluetooth connection.
	 * Parameters:
	 * None
	 * Returns:
	 * Pose object transmitted by the NXT via the bluetooth connection.
	 */
	public Pose recvPose() {
		DataInputStream isPtr = model.getInStream();
		try {
			//Load pose object from input stream.
			Pose pose = new Pose();
			pose.loadObject(isPtr);
			
			return pose;
		} catch (IOException e) {
			System.out.println("Controller > sendInt -"
					+ "Error: Data transfer failed.");
			return null;
		}
	}
	
	/*
	 * Method to send an integer to the NXT.
	 * Parameters:
	 * data - Integer to transmit.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean sendInt(int data) {
		DataOutputStream osPtr = model.getOutStream();
		try {
			osPtr.writeInt(data);
			//Flush is necessary when writing to output to ensure transfer.
			osPtr.flush();
			return true;
		} catch (IOException e) {
			System.out.println("Controller > sendInt -"
								+ "Error: Data transfer failed.");
			return false;
		}
	}
	
	/*
	 * Method to receive an integer from the NXT.
	 * Parameters:
	 * None
	 * Returns:
	 * Integer transmitted by NXT.
	 */
	public int recvInt() {
		DataInputStream isPtr = model.getInStream();
		try {
			return(isPtr.readInt());
		} catch (IOException e) {
			System.out.println("Controller > sendInt -"
								+ "Error: Data transfer failed.");
			return(0);
		}
	}
	
	/*
	 * Method to send a boolean to the NXT.
	 * Parameters:
	 * data - Boolean to transmit.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean sendBool(boolean data) {
		DataOutputStream osPtr = model.getOutStream();
		try {
			//Write boolean to output stream.
			osPtr.writeBoolean(data);
			//Flush data from buffer.
			osPtr.flush();
			
			return true;
		} catch (IOException e) {
			System.out.println("Controller > sendInt - Error: Data transfer failed.");
			return false;
		}
	}
	
	/*
	 * Method to receive a boolean from the NXT.
	 * Parameters:
	 * None
	 * Returns:
	 * Boolean transmitted by NXT.
	 */
	public boolean recvBool() {
		DataInputStream isPtr = model.getInStream();
		try {
			//Read boolean from input stream.
			return(isPtr.readBoolean());
		} catch (IOException e) {
			System.out.println("Controller > sendInt - Error: Data transfer failed.");
			return false;
		}
	}
	
	/*
	 * Method to execute simple mapping algorithm.
	 * Parameters:
	 * sampleRate_p - Integer representing sample rate for data scan.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean simpleMap(int sampleRate_p) throws NXTCommException {
		OGM map;
		//Ensure map exists.
		if (checkIfMapPresent()) {
			//Get map pointer.
			map = (OGM)model.getVisualisation();
			//Collect scan using NXT.
			DataScan scan = getScan(sampleRate_p, map.getBotPosition(), map.getBotOrientation());
			try {
				//Execute map algorithm using scan.
				map.simpleMap(scan);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Controller > createMap - Error: Reading out of bounds.");
			}
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Method to execute Bayesian mapping algorithm.
	 * Parameters:
	 * sampleRate_p - Integer representing sample rate for scans.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean bayesianMap(int sampleRate_p) throws NXTCommException {
		OGM map;
		//Check that map exists.
		if (checkIfMapPresent()) {
			//Get pointer to map.
			map = (OGM)model.getVisualisation();
			try {
				//Initialise exit flag.
				int exit = 1;
				do {
					//Get DataScan from robot.
					DataScan scan = getScan(sampleRate_p, map.getBotPosition(), map.getBotOrientation());
					
					//Update map using scan.
					map.updateMap(scan);
					
					//Ask user whether to continue.
					exit = JOptionPane.showConfirmDialog(
						    model.getView().getContentPane(),
						    "Continue mapping?",
						    "Input",
						    JOptionPane.YES_NO_OPTION);
					
					//If user picks continue...
					if (exit == 0) {
						//Request rotation command.
						String rInput = JOptionPane.showInputDialog(
										model.getView().getContentPane(),
										"Enter rotation degrees: ");
						int rotate = Integer.parseInt(rInput);
						
						//Request travel command.
						String tInput = JOptionPane.showInputDialog(
										model.getView().getContentPane(),
										"Enter travel distance: ");
						int travel = Integer.parseInt(tInput);
						
						//Create MoveCommand object.
						MoveCommand mc = new MoveCommand(rotate, travel);
						
						//Send move command to robot and get odometry pose.
						Pose newPose = moveCommand(mc);
						
						//Save start position.
						Point2D.Double p1 = map.getBotPosition();
						//Update bot state with odometry.
						map.updateBotState(newPose);
						//Add MoveCommand to command history.
						map.addCommand(mc);
						//Get new position.
						Point2D.Double p2 = map.getBotPosition();
						//Add bot path line to map.
						map.addLine(p1, p2);
					}
					//Loop while user selects continue.
				} while (exit == 0);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Controller > bayesianMap - Error: Reading out of bounds.");
			}
			return true;
		} else {
			System.out.println("Controller > bayesianMap - Error: No map found.");
			return false;
		}
	}
	
	/*
	 * Method to create session strings for writing to file.
	 * Parameters:
	 * scans_p - Scans from mapping session to store.
	 * comms_p - Movement commands from mapping session to store.
	 * Returns:
	 * String array containing lines to write to session text file.
	 */
	public String[] createSession(ArrayList<DataScan> scans_p, ArrayList<MoveCommand> comms_p) {
		//Initialise string array list.
		ArrayList<String> session = new ArrayList<String>();
		//Initialise counts for scans and commands.
		int maxScans = scans_p.size();
		int maxComms = comms_p.size();
		//Initialise counter variables for scans and commands.
		int scanCount = 0;
		int commCount = 0;
		do {
			//If there are still scans left.
			if (scanCount < maxScans) {
				//Get scan object.
				DataScan scan = scans_p.get(scanCount);
				//Get scan size and sample rate from scan object.
				int numVals = scan.size();
				int rate = scan.getSRate();
				//Add rate and num to array.
				session.add(Integer.toString(rate));
				session.add(Integer.toString(numVals));
				//Get values array from scan object.
				int[] vals = scan.getValues();
				//Add each value to array in order.
				for (int v : vals) {
					session.add(Integer.toString(v));
				}
				//Get position from scan object.
				Point2D.Double position = scan.getRelativePosition();
				//Add coordinates to array.
				session.add(Double.toString(position.getX()));
				session.add(Double.toString(position.getY()));
				//Get heading from scan object.
				double heading = scan.getHeading();
				//Add heading to array.
				session.add(Double.toString(heading));
				//Increment scan counter.
				scanCount++;
			}
			//If there are still commands left.
			if (commCount < maxComms) {
				//Get command object.
				MoveCommand comm = comms_p.get(commCount);
				//Get rotation command value and add to array.
				int rotate = comm.getRotateCommand();
				session.add(Integer.toString(rotate));
				//Get travel command value and add to array.
				int travel = comm.getTravelCommand();
				session.add(Integer.toString(travel));
				//Get odometry X coordinate and add to array.
				double newX = comm.getNewX();
				session.add(Double.toString(newX));
				//Get odometry Y coordinate and add to array.
				double newY = comm.getNewY();
				session.add(Double.toString(newY));
				//Get heading value and add to array.
				double newHeading = comm.getNewHeading();
				session.add(Double.toString(newHeading));
				//Increment command counter.
				commCount++;
			}
		//Loop while there are still objects to add.
		} while ((scanCount < maxScans) && (commCount < maxComms));
		//Initialise session array.
		int sessionLength = session.size();
		String[] sessionArray = new String[sessionLength];
		//Add each string to array.
		for (int i = 0; i < sessionLength; i++) {
			sessionArray[i] = session.get(i);
		}
		
		return sessionArray;
	}
	
	/*
	 * Method for loading session from file.
	 * Parameters:
	 * map_p - Map object to load session to.
	 * filepath_p - Path of file to load session from.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean loadSession(OGM map_p, String filepath_p) throws IOException {
		//Create buffered reader for file reading using file path.
		BufferedReader in = new BufferedReader(new FileReader(filepath_p));
		//Get first line.
		String line = in.readLine();
		//Loop until line is null, signalling end of file.
		while (line != null) {
			//Get sample rate value and number of readings in scan from file.
			int rate = Integer.parseInt(line);
			int numVals = Integer.parseInt(in.readLine());
			//Initialise values array.
			int[] vals = new int[numVals];
			//Get each value from file and add to values array.
			for (int i = 0; i < numVals; i++) {
				vals[i] = Integer.parseInt(in.readLine());
			}
			//Get coordinates from file and use to create point object.
			Point2D.Double position = new Point2D.Double(
					Double.parseDouble(in.readLine()),
					Double.parseDouble(in.readLine()));
			//Get heading value from file.
			double heading = Double.parseDouble(in.readLine());
			//Create data scan object using values, position and heading.
			DataScan scan = new DataScan(vals, position, heading);
			//Use scan to update map.
			map_p.updateMap(scan);
			//Add scan to map history.
			map_p.addScan(scan);
			//Get move command variables from file.
			int rotate = Integer.parseInt(in.readLine());
			int travel = Integer.parseInt(in.readLine());
			double newX = Double.parseDouble(in.readLine());
			double newY = Double.parseDouble(in.readLine());
			double newHeading = Double.parseDouble(in.readLine());
			//Use move command variables to create move command object.
			MoveCommand mc = new MoveCommand(rotate, travel);
			mc.setNewX(newX);
			mc.setNewY(newY);
			mc.setNewHeading(newHeading);
			//Use move command odometry values to create odometry pose object.
			Pose newPose = new Pose((float)newX, (float)newY, (float)newHeading);
			//Update bot state in map using odometry pose.
			map_p.updateBotState(newPose);
			//Add move command to map history.
			map_p.addCommand(mc);
			//Read next line for loop check.
			line = in.readLine();
		}
		//Close file reader.
		in.close();
		
		return true;
	}
	
	/*
	 * Method to write current mapping session to text file on hard drive.
	 * Parameters:
	 * None
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean saveSession() {
		OGM map;
		boolean returnBool = false;
		//Ensure map exists.
		if (checkIfMapPresent()) {
			//Get map pointer.
			map = (OGM)model.getVisualisation();
			//Get map history arrays.
			ArrayList<DataScan> scans = map.getScanList();
			ArrayList<MoveCommand> comms = map.getCommandList();
			//Create session strings to write to file.
			String[] session = createSession(scans, comms);
			//Initialise print writer for file I/O.
			PrintWriter pw = null;
	        try {
	        	//Create print writer for file I/O using default file path constant.
	            pw = new PrintWriter(new FileWriter(model.DEFAULT_SESSION_PATH));
	            //Write strings from array to file.
	            for (String s : session) {
	            	pw.println(s);
	            }
	            
	             returnBool = true;
	        } catch (IOException e) {
	        	System.out.println("Controller > saveSession - Error: Writing to file failed.");
	        	
	        	returnBool = false;
	        } finally {
	            //Close print writer.
	        	if (pw != null) {
	                pw.close();
	            }
	        }
		}
		return returnBool;
	}
	
	/*
	 * Method to get data scan from the NXT robot.
	 * Parameters:
	 * sampleRate_p - Sample rate for scan.
	 * botPosition_p - Current robot position.
	 * heading_p - Current robot heading.
	 * Returns:
	 * DataScan representing scan returned by robot over bluetooth connection.
	 */
	public DataScan getScan(int sampleRate_p, Point2D.Double botPosition_p, double heading_p) throws NXTCommException {
		//Ensure sample rate is valid.
		if (360 % sampleRate_p == 0) {
			//Calculate array size from sample rate.
			int arraySize = 360 / sampleRate_p;
			
			//Send function identifier.
			sendInt(2);
			//Send value for sample rate to NXT.
			sendInt(sampleRate_p);
			
			//Initialise data array.
			int[] data = new int[arraySize];
			//For each sample...
			for (int j = 0; j < arraySize; j++) {
				//Enter received value from NXT into array.
				data[j] = recvInt();
			}
			//Create data scan object using collected data and bot state variables.
			DataScan scan = new DataScan(data, botPosition_p, heading_p);
			
			//Get robot success boolean.
			Boolean success = recvBool();
			if (!success) {
				System.out.println("Controller > rawDataTransfer - Error: NXT reports failure following execution.");
			}
		
			return scan;
		} else {
			//Print error message for invalid sample rate.
			System.out.println("Controller > rawDataTransfer - Error: Invalid sample rate.");
			return null;
		}
	}
	
	/* 
	 * Method for the Raw Data Transfer function.
	 * Parameters:
	 * sampleRate - Number of degrees rotation between samples.
	 * numScans - Number of scans to perform.
	 * Returns:
	 * 2D array of integers containing raw values from each scan.
	 */
	public DataScan[] rawDataCollect(int sampleRate_p, int numScans_p) throws NXTCommException {
		//Ensure sample rate is valid.
		if (360 % sampleRate_p == 0) {
			//Calculate array size from sample rate.
			int arraySize = 360 / sampleRate_p;
			//Initialise array of scans.
			DataScan scans[] = new DataScan[numScans_p];
			
			//For each scan...
			for (int i = 0; i < numScans_p; i++) {
				Point2D.Double origin = new Point2D.Double(0.0, 0.0);
				scans[i] = getScan(sampleRate_p, origin, 0);
			}
			
			//Create visualisation.
			Visualisation vis = new RawDataDisplay(scans);
			//Set model visualisation.
			model.setVisualisation(vis);
			
			//Receive NXT error boolean.
			Boolean success = recvBool();
			if (!success) {
				System.out.println("Controller > rawDataTransfer - Error: NXT reports failure following execution.");
			}
			
			return scans;
		} else {
			//Print error message for invalid sample rate.
			System.out.println("Controller > rawDataTransfer - Error: Invalid sample rate.");
			return null;
		}
	}
	
	/*
	 * Method to execute a movement command on the robot.
	 * Parameters:
	 * command_p - Movement command for robot to execute.
	 * Returns:
	 * Pose object representing odometry pose returned by the robot.
	 */
	public Pose moveCommand(MoveCommand command_p) throws NXTCommException {
		/*
		 * A lot of conversion goes on in this method to make the leJOS Pose
		 * objects work with our map system. Ideally the map would just use
		 * the same system as the Pose objects do, such as using floats instead
		 * of doubles and such, but it was too late by the time I realised so
		 * these workarounds were a quick fix for this.
		 * Feel free to make this prettier if you want to, but for now it works.
		 */
		
		//Send function identifier.
		sendInt(1);
		
		Pose firstPose = recvPose();
		
		/*
		 * leJOS pose object uses inverted X and Y axis compared to our map so
		 * here we flip the X and Y to account for this.
		 */
		float fx = firstPose.getY();
		float fy = firstPose.getX();
		firstPose.setLocation(-fx, fy);
		
		Point2D.Double fp = new Point2D.Double(fx, fy);
		System.out.println("First Point: " + fp);
		
		/*
		 * leJOS pose object uses range -180 to 180 for heading,
		 * so here we convert to range 0 to 360.
		 */
		float fheading = firstPose.getHeading();
		if (fheading < 0) {
			fheading = 360 - Math.abs(fheading);
		}
		
		/*
		 * For some reason the heading is also inverted so here we flip that too.
		 */
		fheading = 360 - fheading;
		firstPose.setHeading(fheading);
		
		System.out.println("First heading: " + fheading);
		
		//Send variables.
		sendInt(command_p.getRotateCommand());
		sendInt(command_p.getTravelCommand());
		Pose newPose = recvPose();
		
		
		/*
		 * leJOS pose object uses inverted x and y axis compared to our map so
		 * here we flip the X and Y to account for this.
		 */
		float nx = newPose.getY();
		float ny = newPose.getX();
		newPose.setLocation(-nx, ny);
		
		Point2D.Double np = new Point2D.Double(nx, ny);
		System.out.println("New Point: " + np);
		
		//Set MoveCommand odometry.
		command_p.setNewX(nx);
		command_p.setNewY(ny);
		
		/*
		 * leJOS pose object uses range -180 to 180 for heading,
		 * so here we convert to range 0 to 360.
		 */
		float nheading = newPose.getHeading();
		if (nheading < 0) {
			nheading = 360 - Math.abs(nheading);
		}
		
		/*
		 * For some reason the heading is also inverted so here we flip that too.
		 */
		nheading = 360 - nheading;
		newPose.setHeading(nheading);
		
		//Set MoveCommand odometry.
		command_p.setNewHeading(nheading);
		
		//Print testing values.
		System.out.println("Rotation: " + (nheading - fheading));
		System.out.println("Distance Traveled: " + getDistance(fp, np));
		
		Boolean success = recvBool();
		if (!success) {
			System.out.println("Controller > moveCommand - Error: NXT reports failure following execution.");
		}
		return(newPose);
	}
	
	/*
	 * Method to create a OGM object and set as the current visualisation.
	 * Parameters:
	 * cellSize_p - Drawn width and height of cells in pixels.
	 * ratio_p - Height and width of real world area represented by a cell.
	 * height_p - Height of the map in cells.
	 * width_p - Width of the map in cells.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean drawMapGrid(int cellSize_p, double ratio_p, int height_p, int width_p) {
		//Create OGM object.
		Visualisation vis = new OGM(cellSize_p, ratio_p, height_p, width_p);
		//Set as active visualisation.
		model.setVisualisation(vis);
		return true;
	}
	
	/*
	 * Method to calculate distance between two real world points.
	 * Parameters:
	 * point1_p - First point for calculation.
	 * point2_p - Second point for calculation.
	 * Returns:
	 * Double representing distance between two points.
	 */
	public double getDistance(Point2D.Double point1_p, Point2D.Double point2_p) {
		//Get delta X and delta Y.
		double xDiff = point2_p.getX() - point1_p.getX();
		double yDiff = point2_p.getY() - point1_p.getY();
		//Calculate distance using pythagoras formula.
		return (Math.sqrt((xDiff*xDiff) + (yDiff*yDiff)));
	}
	
	/*
	 * Method to set up Bluetooth communication link to NXT.
	 * Parameters:
	 * nxtAddress - Address of NXT to link to.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean bluetoothSetup() throws NXTCommException {
		//Create NXTComm object for communication using Bluetooth protocol.
		model.setNXTComm(	NXTCommFactory.createNXTComm(
							NXTCommFactory.BLUETOOTH));
		NXTComm nxtCommPtr = model.getNXTComm();
		
		//Create NXTInfo object which uniquely identifies the NXT.
		model.setNXTInfo(new NXTInfo(
							NXTCommFactory.BLUETOOTH,
							"NXT",
							model.NXT_ADDRESS));
		NXTInfo nxtInfoPtr = model.getNXTInfo();
		
		//Open communication link to the NXT.
		nxtCommPtr.open(nxtInfoPtr);
		
		//Create data input and output streams to the NXT.
		model.setInStream(new DataInputStream(nxtCommPtr.getInputStream()));
		model.setOutStream(new DataOutputStream(nxtCommPtr.getOutputStream()));
		
		return true;
	}
	
	/*
	 * Method to clean up and shut down existing Bluetooth link.
	 * Parameters:
	 * None
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean bluetoothShutdown() {
		try {
			//Create pointers to input and output streams and comm object.
			DataInputStream isPtr = model.getInStream();
			DataOutputStream osPtr = model.getOutStream();
			NXTComm nxtCommPtr = model.getNXTComm();
			
			//If not null close all.
			if (isPtr != null) isPtr.close();
			if (osPtr != null) osPtr.close();
			if (nxtCommPtr != null) nxtCommPtr.close();
			
			return true;
		} catch (IOException e) {
			//Print error for IOException on close calls.
			System.out.println("Controller > bluetoothSetup -"
								+ "Error: Bluetooth shutdown failed.");
			return false;
		}
	}
	
	/*
	 * Method to terminate operations. Sends terminate code to NXT to finish NXT program and
	 * shuts down the bluetooth connection.
	 * Parameters:
	 * None
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean closeConnection() {
		sendInt(0);
		bluetoothShutdown();
		return true;
	}
	
	/*
	 * Method to check if there is a map in the model currently.
	 * Parameters:
	 * None
	 * Returns:
	 * Boolean representing presence of map.
	 */
	public boolean checkIfMapPresent() {
		if (model.getVisualisation() instanceof Visualisation) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Method to check if NXT is connected to PC app.
	 * Parameters:
	 * None
	 * Returns:
	 * Boolean representing presence of connection.
	 */
	public boolean checkIfNXTConnected() {
		if ((model.getNXTComm() != null) && (model.getNXTInfo() != null)) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Method for set map value function. Sets a map cell to (close to) 1.
	 * (Cannot set to 1 because of log odds form, would result in zero devision.)
	 * Parameters:
	 * cell_p - Cell index to set.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean testSetMapValue(Point cell_p) {
		if (checkIfMapPresent()) {
			//Get map pointer.
			OGM ogm = (OGM)model.getVisualisation();
			//Calculate log odds value.
			double value = Math.log(1/(1-0.999));
			//Set cell occupancy to value.
			ogm.setOccupancy(cell_p, value);
			
			return true;
		}
		
		return false;
	}
	
	/*
	 * Mutator method for model pointer.
	 * Parameters:
	 * model_p - Pointer to model object.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setModel(Model model_p) {
		model = model_p;
		return true;
	}
	
	/*
	 * Access method for model pointer.
	 * Parameters:
	 * None
	 * Returns:
	 * Pointer to model object.
	 */
	public Model getModel() {
		return(model);
	}
}
