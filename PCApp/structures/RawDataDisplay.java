package mattadshead.swansea3.dissertation.structures;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class RawDataDisplay extends Visualisation {
	
	private DataScan[] scans;
	
	public RawDataDisplay(DataScan[] scans_p) {
		super();
		visType = "Raw Data Display";
		scans = scans_p;
		addValues(scans);
		initComponents();
	}
	
	public RawDataDisplay(DataScan scan_p) {
		super();
		visType = "Raw Data Display";
		scans = new DataScan[1];
		scans[0] = scan_p;
		addValues(scans);
		initComponents();
	}
	
	public DataScan[] getScans() {
		return(scans);
	}
	
	public boolean setScans(DataScan[] scans_p) {
		scans = scans_p;
		return true;
	}
	
	public boolean addScan(DataScan scan_p) {
		DataScan[] newScans = new DataScan[scans.length];
		for (int i = 0; i < scans.length; i++) {
			newScans[i] = scans[i];
		}
		newScans[scans.length] = scan_p;
		scans = newScans;
		return true;
	}
	
	public void initComponents() {
		//Begin automatically generated GUI code.
		textArea.setColumns(20);
        textArea.setRows(5);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(textArea);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(this);
        setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane)
        );
        //End automatically generated GUI code.
	}
	
	public void addValues(DataScan[] scans_p) {
		textArea = new JTextArea();
		int degreeCounter = 0;
		for (DataScan ds : scans) {
			int sRate = ds.getSRate();
			int[] data = ds.getValues();
			for (int v : data) {
				textArea.append("Degrees: " + degreeCounter + " Value: " + v + "\n");
				degreeCounter += sRate;
			}
		}
	}
	
	public int numScans() {
		return(scans.length);
	}
	
	//GUI component declarations.
	private JTextArea textArea;
	private JScrollPane scrollPane;
}