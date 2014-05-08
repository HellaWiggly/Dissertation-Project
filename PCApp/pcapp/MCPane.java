/*
 * MCPane.java
 * Author: Matt Adshead
 * Date: 14 March 2014
 * 
 * Option pane subclass for move command function.
 *  
 * This is a GUI class for a variety of option pane component.
 * Option panes are JPanels dynamically inserted into the main
 * GUI based on which function is selected in the function dropdown.
 * This option pane is for the move command function.
 */

package mattadshead.swansea3.dissertation.pcapp;

import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MCPane extends VariablePane {

	//Symbolic constant declaration.
	private final String 	NUM_LABEL = "Rotate (Degrees): ",
							RATE_LABEL = "Move (mm): ";
	private final int		NUM_VAR = 2;
	
	/*
	 * Class constructor.
	 * Parameters: 
	 * None
	 * Returns:
	 * None
	 */
	public MCPane() {
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
		variables[0] = rotateField.getText();
		variables[1] = travelField.getText();
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
        rotateLabel = new javax.swing.JLabel();
        rotateField = new javax.swing.JTextField();
        travelLabel = new javax.swing.JLabel();
        travelField = new javax.swing.JTextField();
        
        rotateLabel.setText(NUM_LABEL);

        travelLabel.setText(RATE_LABEL);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(this);
        this.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rotateLabel)
                    .addComponent(travelLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rotateField)
                    .addComponent(travelField))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rotateLabel)
                    .addComponent(rotateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(travelLabel)
                    .addComponent(travelField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(369, Short.MAX_VALUE))
        );
        /*
		 * End - Automatically generated WYSIWYG GUI code.
		 */
	}

	//GUI component declarations.
	private JTextField rotateField;
    private JTextField travelField;
    private JLabel rotateLabel;
    private JLabel travelLabel;
}
