/*
 * LoadPane.java
 * Author: Matt Adshead
 * Date: 21 April 2014
 * 
 * Option pane subclass for load session function.
 *  
 * This is a GUI class for a variety of option pane component.
 * Option panes are JPanels dynamically inserted into the main
 * GUI based on which function is selected in the function dropdown.
 * This option pane is for the load session function.
 */

package mattadshead.swansea3.dissertation.pcapp;

@SuppressWarnings("serial")
public class LoadPane extends VariablePane {

	//Symbolic constant declaration.
	private final int		NUM_VAR = 1;
	
	/*
	 * Class constructor.
	 * Parameters: 
	 * None
	 * Returns:
	 * None
	 */
	public LoadPane() {
		//Call superclass constructor.
		super();		
		//Initialise and draw GUI components.
		initComponents();
	}
	
	/*
	 * Method to return contents of variable fields.
	 * Parameters:
	 * None
	 * Returns:
	 * Array of strings containing variable values as strings.
	 */
	public String[] getVariableStrings() {
		String[] variables = new String[NUM_VAR];
		variables[0] = pathFld.getText();
		return(variables);
	}
	
	/*
	 * Method to initialise GUI components and properties.
	 * Parameters:
	 * None
	 * Returns:
	 * None
	 */
	public void initComponents() {
		/*
		 * Begin - Automatically generated WYSIWYG GUI code.
		 */		
        pathFld = new javax.swing.JTextField();

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(this);
        this.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pathFld)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pathFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(391, Short.MAX_VALUE))
        );
        /*
		 * End - Automatically generated WYSIWYG GUI code.
		 */
	}

	//GUI component declarations.
	private javax.swing.JTextField pathFld;
}