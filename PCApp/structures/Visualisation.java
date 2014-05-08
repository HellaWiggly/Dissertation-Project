package mattadshead.swansea3.dissertation.structures;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Visualisation extends JPanel {
	
	protected String visType;
	
	public Visualisation() {
		super();
	}
	
	public String getVisType() {
		return(visType);
	}
	
	public boolean setVisType(String type) {
		visType = type;
		return true;
	}

}