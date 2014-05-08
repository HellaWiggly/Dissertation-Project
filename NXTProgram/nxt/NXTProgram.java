/*
 * EnVisNXTRunner.java
 * Author: Matt Adshead
 * Date: 6 Dec 2013
 * 
 * Sequential NXT application. This static class contains the methods to move
 * and collect data using the robot. The class when run awaits Bluetooth instructions
 * and executes them in a loop terminated by a terminate signal.
 */

package mattadshead.swansea3.dissertation.nxt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;

public class NXTProgram {
	private static final double WHEEL_DIAM = 54.0, TRACK_WIDTH = 113.0;
	private static final int 	TRAVEL_SPEED = 150, ROTATE_SPEED = 100,
								SENSOR_ROTATE_SPEED = 20, SENSOR_RESET_SPEED = 90;
	
	private static NXTRegulatedMotor sensorMotor;
	private static OpticalDistanceSensor sensor;
	private static DifferentialPilot pilot;
	private static OdometryPoseProvider poseTracker;
	private static NXTConnection commLink;
	private static DataInputStream inStream;
	private static DataOutputStream outStream;
	private static boolean terminate;
	
	/*
	 * Main method, initialises hardware driver classes and starts execution loop.
	 * Parameters:
	 * args - Command line arguments, not used in implementation.
	 * Returns:
	 * None
	 */
	public static void main(String[] args) {
		//Initialise and power down sensor.
		sensor = new OpticalDistanceSensor(SensorPort.S4);
		sensor.powerOff();
		
		//Initialise sensor rotation motor.
		sensorMotor = Motor.C;
		sensorMotor.setSpeed(SENSOR_ROTATE_SPEED);
		
		//Initialise pilot for driving wheels.
		pilot = new DifferentialPilot(WHEEL_DIAM, TRACK_WIDTH, Motor.B, Motor.A);
		pilot.setTravelSpeed(TRAVEL_SPEED);
		pilot.setRotateSpeed(ROTATE_SPEED);
		//Initialise pose tracker and link to pilot.
		poseTracker = new OdometryPoseProvider(pilot);
		
		//Create bluetooth link.
		setupConnection();
		
		//Loop and wait for commands from PC app.
		terminate = false;
		try {
			do {
				//Wait for new data on input stream.
				while (inStream.available() == 0);
				//When data available, get function identifier.
				int functionSelect = inStream.readInt();
				//Execute function based on identifier.
				switch (functionSelect) {
					case 0 : 	terminate = true;
								break;
					case 1 :	outStream.writeBoolean(move());
								outStream.flush();
								break;
					case 2 :	outStream.writeBoolean(scan());
								outStream.flush();
								break;
					default:	terminate = true;
								break;
				}
			} while (!terminate);
		} catch (IOException e) {
			System.out.println("Error getting instructions...");
			commError(e);
		} finally {
			//Close bluetooth link.
			closeConnection();
		}
	}
	
	/*
	 * Method to set up the bluetooth link with the PC app.
	 * Parameters:
	 * None
	 * Returns:
	 * Boolean representing operation success.
	 */
	public static boolean setupConnection() {
		System.out.println("Waiting for bluetooth connection...");
		//Connect to PC application.
		commLink = Bluetooth.waitForConnection();
		
		System.out.println("Opening streams.");
		//Open data input and output streams to PC for data transfer.
		inStream = commLink.openDataInputStream();
		outStream = commLink.openDataOutputStream();
		return true;
	}
	
	/*
	 * Method to close existing bluetooth link to PC app.
	 * Parameters:
	 * None
	 * Returns:
	 * Boolean representing operation success.
	 */
	public static boolean closeConnection() {
		try {	
			//Close streams and connection.
			inStream.close();
			outStream.close();
			commLink.close();
		} catch (IOException e) {
			System.out.println("Error closing connection...");
			commError(e);
			return false;
		}
		return true;
	}
	
	/*
	 * Method to print error stack trace for communication errors.
	 * Parameters:
	 * e_p - IOException exception object for the error.
	 * Returns:
	 * None
	 */
	public static void commError(IOException e_p) {
		//Print error for IOException on Bluetooth communications.
		System.out.println("Communication Error!");
		System.out.println(e_p);
		//Wait for button press before continuing.
		Button.ENTER.waitForPressAndRelease();
	}
	
	/*
	 * Method to reset the pose tracker pose object.
	 * Parameters:
	 * None
	 * Returns:
	 * Boolean representing operation success.
	 */
	public static boolean clearPose() {
		Pose pose = new Pose(0, 0, 0);
		poseTracker.setPose(pose);
		return true;
	}
	
	/*
	 * Method to recieve and execute a move command and send back
	 * odometry data using pose tracker.
	 * Parameters:
	 * None
	 * Returns:
	 * Boolean representing operation success.
	 */
	public static boolean move() {
		try{
			//Send initial pose to PC app.
			Pose firstPose = poseTracker.getPose();
			firstPose.dumpObject(outStream);
			outStream.flush();
			
			//Get parameter values.
			int rotate = inStream.readInt();
			int travel = inStream.readInt();
			
			//Execute commands.
			if (rotate > 0) {
				pilot.rotate(-rotate);
				pilot.stop();
			}
			if (travel > 0) {
				pilot.travel(travel);
				pilot.stop();
			}
			pilot.stop();
			
			//Send new pose to PC app.
			Pose newPose = poseTracker.getPose();
			newPose.dumpObject(outStream);
			outStream.flush();
			
			return true;
		} catch (IOException e) {
			System.out.println("Error in move method...");
			commError(e);
			pilot.stop();
			return false;
		}
	}
	
	/*
	 * Method to collect a scan using the sensor and send it
	 * back to the PC app.
	 * Parameters:
	 * None
	 * Returns:
	 * Boolean representing operation success.
	 */
	public static boolean scan() {
		//Turn on infra-red sensor.
		sensor.powerOn();
		
		try {
			//Receive parameters from PC application.
			int sampleRate = inStream.readInt();
			
			//Calculate array size from sample rate.
			int arraySize = 360 / sampleRate;
			
			//Set motor speed to 90 degrees per second.
			sensorMotor.setSpeed(SENSOR_ROTATE_SPEED);
			
			//Reset motor to 0.
			sensorMotor.rotateTo(0);
			
			//Get and discard erroneous first value.
			int dump = sensor.getDistance();
			
			for (int j = 0; j < arraySize; j++) {
				//Get sensor value from infra-red sensor.
				int sensorValue = sensor.getDistance();
				//Write value to output stream.
				outStream.writeInt(sensorValue);
				//Flush output stream. Necessary to ensure transmit.
				outStream.flush();
				//Rotate motor by sample rate degrees.
				sensorMotor.rotate(sampleRate);
				//Stop motor and resist external movement.
				sensorMotor.stop();
			}
			
			sensorMotor.setSpeed(SENSOR_RESET_SPEED);
			//Reset motor position to avoid cable tangling.
			sensorMotor.rotate(-360);
			//Stop motor and resist external movement.
			sensorMotor.stop();
			
			//Turn off infra-red sensor.
			sensor.powerOff();
			return true;
		} catch (IOException e) {
			System.out.println("Error transmitting scan...");
			commError(e);
			//Turn off infra-red sensor.
			sensor.powerOff();
			return false;
		}
	}
}
