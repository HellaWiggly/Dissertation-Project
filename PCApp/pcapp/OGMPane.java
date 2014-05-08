/*
 * OGMPane.java
 * Author: Matt Adshead
 * Date: 21 April 2014
 * 
 * Option pane subclass for mapping functions.
 *  
 * This is a GUI class for a variety of option pane component.
 * Option panes are JPanels dynamically inserted into the main
 * GUI based on which function is selected in the function dropdown.
 * This option pane is for mapping functions. This applies to the
 * draw map, simple map, and Bayesian map functions.
 */

package mattadshead.swansea3.dissertation.pcapp;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class OGMPane extends VariablePane {

	//Symbolic constant declaration.
	private final String 	SIZE_LABEL = "Cell Size (px):",
							RATIO_LABEL = "Cell Area (mm):",
							HEIGHT_LABEL = "Map Height:",
							WIDTH_LABEL = "Map Width:",
							RATE_LABEL = "Sample Rate:",
							DEFAULT_VAL = "default";
	private final int		NUM_VAR = 5,
							DEFAULT_CELL_SIZE = 5,
							DEFAULT_RATE = 1;

	
	/*
	 * Class constructor.
	 * Parameters: 
	 * None
	 * Returns:
	 * None
	 */
	public OGMPane() {
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
		if (sizeField.getText().equalsIgnoreCase("default")) {
			variables[0] = Integer.toString(DEFAULT_CELL_SIZE);
		} else {
			variables[0] = sizeField.getText();
		}
		variables[1] = ratioField.getText();
		variables[2] = heightField.getText();
		variables[3] = widthField.getText();
		if (rateField.getText().equalsIgnoreCase("default")) {
			variables[4] = Integer.toString(DEFAULT_RATE);
		} else {
			variables[4] = rateField.getText();
		}
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
		sizeLabel = new JLabel();
		ratioLabel = new JLabel();
		heightLabel = new JLabel();
		widthLabel = new JLabel();
		rateLabel = new JLabel();
		sizeField = new JTextField();
		ratioField = new JTextField();
		heightField = new JTextField();
		widthField = new JTextField();
		rateField = new JTextField();
		
		sizeLabel.setText(SIZE_LABEL);

        ratioLabel.setText(RATIO_LABEL);

        heightLabel.setText(HEIGHT_LABEL);

        widthLabel.setText(WIDTH_LABEL);
        
        rateLabel.setText(RATE_LABEL);

        rateField.setText(DEFAULT_VAL);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(this);
        this.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(rateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sizeLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ratioLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(heightLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(widthLabel, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sizeField, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                    .addComponent(ratioField)
                    .addComponent(heightField)
                    .addComponent(widthField)
                    .addComponent(rateField))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sizeLabel)
                    .addComponent(sizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ratioLabel)
                    .addComponent(ratioField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(heightLabel)
                    .addComponent(heightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(widthLabel)
                    .addComponent(widthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rateLabel)
                    .addComponent(rateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(267, Short.MAX_VALUE))
        );
        /*
		 * End - Automatically generated WYSIWYG GUI code.
		 */
        sizeField.setText(DEFAULT_VAL);
	}

	//GUI component declarations.
	private JTextField sizeField;
    private JTextField ratioField;
    private JTextField heightField;
    private JTextField widthField;
    private JTextField rateField;
    private JLabel sizeLabel;
    private JLabel ratioLabel;
    private JLabel heightLabel;
    private JLabel widthLabel;
    private JLabel rateLabel;
}