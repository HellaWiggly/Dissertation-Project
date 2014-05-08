/*
 * BlankPane.java
 * Author: Matt Adshead
 * Date: 14 March 2014
 * 
 * Empty option pane subclass for functions with no variables.
 *  
 * This is a GUI class for a variety of option pane component.
 * Option panes are JPanels dynamically inserted into the main
 * GUI based on which function is selected in the function dropdown.
 * This option pane is a blank option pane inserted when a function
 * with no specified variables is selected.
 */

package mattadshead.swansea3.dissertation.pcapp;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BlankPane extends VariablePane {
	
	/*
	 * Class constructor.
	 * Parameters:
	 * None
	 * Returns:
	 * None
	 */
	public BlankPane() {
		super();
	}
	
	/*
	 * Method to return associated variable values from variable fields.
	 * Parameters:
	 * None
	 * Returns:
	 * String array containing variable values.
	 */
	public String[] getVariableStrings() {
		return null;
	};
}
