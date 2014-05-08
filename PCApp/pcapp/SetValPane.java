/*
 * SetValPane.java
 * Author: Matt Adshead
 * Date: 14 March 2014
 * 
 * Option pane subclass for set map value function.
 *  
 * This is a GUI class for a variety of option pane component.
 * Option panes are JPanels dynamically inserted into the main
 * GUI based on which function is selected in the function dropdown.
 * This option pane is for the set map value function.
 */

package mattadshead.swansea3.dissertation.pcapp;

@SuppressWarnings("serial")
public class SetValPane extends VariablePane {

	//Symbolic constant declaration.
	private final String 	X_LABEL = "X Index: ",
							Y_LABEL = "Y Index: ";
	private final int		NUM_VAR = 2;
	
	/*
	 * Class constructor.
	 * Parameters: 
	 * None
	 * Returns:
	 * None
	 */
	public SetValPane() {
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
		variables[0] = xField.getText();
		variables[1] = yField.getText();
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
        xLabel = new javax.swing.JLabel();
        xField = new javax.swing.JTextField();
        yLabel = new javax.swing.JLabel();
        yField = new javax.swing.JTextField();
        
        xLabel.setText(X_LABEL);

        yLabel.setText(Y_LABEL);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(this);
        this.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xLabel)
                    .addComponent(yLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xField)
                    .addComponent(yField))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xLabel)
                    .addComponent(xField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yLabel)
                    .addComponent(yField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(369, Short.MAX_VALUE))
        );
        /*
		 * End - Automatically generated WYSIWYG GUI code.
		 */
	}

	//GUI component declarations.
	private javax.swing.JTextField xField;
    private javax.swing.JTextField yField;
    private javax.swing.JLabel xLabel;
    private javax.swing.JLabel yLabel;
}