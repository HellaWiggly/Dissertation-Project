/*
 * VariablePane.java
 * Author: Matt Adshead
 * Date: 14 March 2014
 * 
 * Super-class option pane object.
 *  
 * This is a GUI class for a variety of option pane component.
 * Option panes are JPanels dynamically inserted into the main
 * GUI based on which function is selected in the function dropdown.
 * This is the super-class option pane object VariablePane,
 * the class is abstract not instantiated.
 */

package mattadshead.swansea3.dissertation.pcapp;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class VariablePane extends JPanel {
	
	/*
	 * Class constructor.
	 * Parameters:
	 * None
	 * Returns:
	 * None
	 */
	public VariablePane() {
		super();
	}
	
	/*
	 * Super-class method to get variable field values as integers.
	 * Uses the subclass getVariableStrings method to get an array of strings
	 * then converts all the strings to integers.
	 * Parameters:
	 * None
	 * Returns:
	 * Array of integers representing values from variable fields.
	 */
	public int[] getVariableIntegers() {
		String[] strings = getVariableStrings();
		int[] integers = new int[strings.length];
		int ptr = 0;
		for (String value : strings) {
			integers[ptr] = Integer.parseInt(value);
			ptr++;
		}
		return integers;
	}
	
	/*
	 * Abstract sub-class method to get variable field values as strings.
	 * 
	 * Because each panel sub-class has a different number of variables
	 * but we want to be able to get the variables using a super-class method
	 * I implement a get strings method for each sub-class and then
	 * convert the strings to integers and return them in the super-class method.
	 * At the time it seemed like a good idea, but in hindsight this could probably
	 * be done in a more elegant fashion.
	 * 
	 * Parameters:
	 * None
	 * Returns:
	 * Array of strings representing values from variable fields.
	 */
	public abstract String[] getVariableStrings();
}
