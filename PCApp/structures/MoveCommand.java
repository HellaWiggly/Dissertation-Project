/*
 * MoveCommand.java
 * Author: Matt Adshead
 * Date: 14 March 2014
 * 
 * Data structure class representing a single move command.
 * 
 * This data structure class represents a move command issued to the robot in the system.
 * It contains the values for the rotation and travel parts of the command as well as the
 * relative pose object values returned by the NXT following execution which encode the new
 * position of the robot following the execution and are the odometry data used by the system
 * to update the robot state after movement. The pose object is initialised at the start of
 * mapping so the odometry represents the position of the robot relative to the starting point
 * of the mapping session and not the starting point of the individual move command.
 */

package mattadshead.swansea3.dissertation.structures;

public class MoveCommand {
	
	//Rotation and travel command values.
	private int rotateCmd, travelCmd;
	
	//Robot pose after command execution according to odometry.
	private double newX, newY, newHeading;
	
	/*
	 * Class constructor.
	 * Parameters:
	 * rotate_p - Rotate command value.
	 * travel_p - Travel command value.
	 * Returns:
	 * None
	 */
	public MoveCommand(int rotate_p, int travel_p) {
		rotateCmd = rotate_p;
		travelCmd = travel_p;
	}
	
	/*
	 * Accessor method for rotate command.
	 * Parameters:
	 * None
	 * Returns:
	 * Integer representing rotation command.
	 */
	public int getRotateCommand() {
		return rotateCmd;
	}
	
	/*
	 * Mutator method for rotate command.
	 * Parameters:
	 * rotate_p - New rotate command value.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setRotateCommand(int rotate_p) {
		rotateCmd = rotate_p;
		return true;
	}
	
	/*
	 * Accessor method for travel command.
	 * Parameters:
	 * None
	 * Returns:
	 * Integer representing travel command.
	 */
	public int getTravelCommand() {
		return travelCmd;
	}
	
	/*
	 * Mutator method for travel command.
	 * Parameters:
	 * travel_p - New travel command value.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setTravelCommand(int travel_p) {
		travelCmd = travel_p;
		return true;
	}
	
	/*
	 * Accessor method for new X odometry value.
	 * Parameters:
	 * None
	 * Returns:
	 * Double representing new X odometry value.
	 */
	public double getNewX() {
		return newX;
	}

	/*
	 * Mutator method for new X odometry value.
	 * Parameters:
	 * newX_p - New X odometry value.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setNewX(double newX_p) {
		newX = newX_p;
		return true;
	}
	
	/*
	 * Accessor method for new Y odometry value.
	 * Parameters:
	 * None
	 * Returns:
	 * Double representing new Y odometry value.
	 */
	public double getNewY() {
		return newY;
	}
	
	/*
	 * Mutator method for new Y odometry value.
	 * Parameters:
	 * newY_p - New Y odometry value.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setNewY(double newY_p) {
		newY = newY_p;
		return true;
	}
	
	/*
	 * Accessor method for heading odometry value.
	 * Parameters:
	 * None
	 * Returns:
	 * Boolean representing heading odometry value.
	 */
	public double getNewHeading() {
		return newHeading;
	}

	/*
	 * Mutator method for heading odometry value.
	 * Parameters:
	 * heading_p - Heading odometry value.
	 * Returns:
	 * Boolean representing operation success.
	 */
	public boolean setNewHeading(double newH_p) {
		newHeading = newH_p;
		return true;
	}
}
