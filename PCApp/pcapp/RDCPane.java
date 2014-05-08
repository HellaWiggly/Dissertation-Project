/*
 * RDCPane.java
 * Author: Matt Adshead
 * Date: 14 March 2014
 * 
 * Option pane subclass for raw data collection function.
 *  
 * This is a GUI class for a variety of option pane component.
 * Option panes are JPanels dynamically inserted into the main
 * GUI based on which function is selected in the function dropdown.
 * This option pane is for the raw data collection function.
 */

package mattadshead.swansea3.dissertation.pcapp;

@SuppressWarnings("serial")
public class RDCPane extends VariablePane {

	//Symbolic constant declaration.
	private final String 	NUM_LABEL = "Number of Tests: ",
							RATE_LABEL = "Sample Rate: ";
	private final int		NUM_VAR = 2;
	
	/*
	 * Class constructor.
	 * Parameters: 
	 * None
	 * Returns:
	 * None
	 */
	public RDCPane() {
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
		variables[0] = numField.getText();
		variables[1] = rateField.getText();
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
        numLabel = new javax.swing.JLabel();
        numField = new javax.swing.JTextField();
        rateLabel = new javax.swing.JLabel();
        rateField = new javax.swing.JTextField();
        
        numLabel.setText(NUM_LABEL);

        rateLabel.setText(RATE_LABEL);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(this);
        this.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numLabel)
                    .addComponent(rateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numField)
                    .addComponent(rateField))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numLabel)
                    .addComponent(numField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rateLabel)
                    .addComponent(rateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(369, Short.MAX_VALUE))
        );
        /*
		 * End - Automatically generated WYSIWYG GUI code.
		 */
	}

	//GUI component declarations.
	private javax.swing.JTextField numField;
    private javax.swing.JTextField rateField;
    private javax.swing.JLabel numLabel;
    private javax.swing.JLabel rateLabel;
}