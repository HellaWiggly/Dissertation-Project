package mattadshead.swansea3.dissertation.structures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import lejos.robotics.navigation.Pose;

public class OGM extends Visualisation {
	//Constants.
	private final double	MATRIX_INIT = 0;
	private final String 	VIS_TYPE = "Occupancy Grid Map";
	
	/*
	 * d1, d2, and d3 constants used in determining weight of updates.
	 * (d1*2) represents the length in mm of the range over which the weight
	 * increases linearly from -s() to +s().
	 * (d2-d1) represents the length in mm of the range over which the weight
	 * remains constant at +s().
	 * (d3-d1) represents the length in mm of the range over which the weight
	 * decreases linearly from +s() to the prior value.
	 */
	private final double	D1 = 5,
							D2 = 35,
							D3 = 37;
	
	/*
	 * CONE_FACTOR constant used in the sensor distribution equation.
	 * coneFactor affects the distribution value as the angle between
	 * the update and the measurement increases. The closer coneFactor is
	 * to 0 the less effect the angle has on the sensor distribution value.
	 * At coneFactor = 2 the effective range of distribution is +/- 1 degree.
	 * Any values outside this range will have negligible s() return values.
	 */
	private double CONE_FACTOR = 2;
	
	//Measurement and move history, in case needed.
	private ArrayList<DataScan> scanList;
	private ArrayList<MoveCommand> commandList;
	
	//List of lines for rendering robot path.
	private ArrayList<Line2D.Double> lineList = new ArrayList<Line2D.Double>();
	
	//Map state matrix.
	private double[][] matrix;
	
	//Origin point and current bot position.
	private Point2D.Double 	originPoint,
							botPos;
	//Bot heading in degrees.
	private double botOrientation;
	
	//Size of cells in pixels.
	private int cellSize;
	//Area represented by cells in mm. 
	private double mapRatio;
	//Height and width of map in cells.
	private int mapHeight;
	private int mapWidth;
	
	/*
	 * Class constructor.
	 * Parameters:
	 * cellSize_p - Drawn width and height of cells in pixels.
	 * ratio_p - Height and width of real world area represented by a cell.
	 * height_p - Height of the map in cells.
	 * width_p - Width of the map in cells.
	 * Returns:
	 * None
	 */
	public OGM(int cellSize_p, double ratio_p, int height_p, int width_p) {
		super();
		visType = VIS_TYPE;
		setBackground(Color.BLACK);
		
		cellSize  = cellSize_p;
		mapRatio = ratio_p;
		mapHeight = height_p;
		mapWidth = width_p;
		
		matrix = new double[mapWidth][mapHeight];
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				matrix[i][j] = MATRIX_INIT;
			}
		}
		
		originPoint = new Point2D.Double(((mapWidth * mapRatio)/2), ((mapHeight * mapRatio)/2));
		botPos = originPoint;
		botOrientation = 0;
		scanList = new ArrayList<DataScan>();
		commandList = new ArrayList<MoveCommand>();
	}
	
	/*
	 * Method to get corresponding cell given a real world point.
	 * Parameters:
	 * realPoint_p - Real world point.
	 * Returns:
	 * Point object representing cell indices.
	 */
	public Point getPointCell(Point2D.Double realPoint_p) {
		double realX = realPoint_p.getX();
		double realY = realPoint_p.getY();
		int xCell = (int) Math.ceil((realX / mapRatio));
		int yCell = (int) Math.ceil((realY / mapRatio));
		return(new Point(xCell, yCell));
	}
	
	/*
	 * Method to get real world coordinates of centre of give cell.
	 * Parameters:
	 * cell_p - Cell indices.
	 * Returns:
	 * Point object representing real world point for cell centre.
	 */
	public Point2D.Double getCellCentre(Point cell_p) {
		double realX = (cell_p.getX() * mapRatio) - (mapRatio/2);
		double realY = (cell_p.getY() * mapRatio) - (mapRatio/2);
		return(new Point2D.Double(realX, realY));
	}
	
	/*
	 * Method to get measurement point given range and heading.
	 * Parameters:
	 * range_p - Range value.
	 * heading_p - Heading value.
	 * Returns:
	 * Point representing measurement point.
	 */
	public Point2D.Double getMeasurementPoint(int range_p, double heading_p) {
		double x = (range_p * (Math.sin(Math.toRadians(heading_p))));
		double y = (range_p * (Math.cos(Math.toRadians(heading_p))));
		x += botPos.getX();
		y += botPos.getY();
		Point2D.Double coord = new Point2D.Double(x, y);
		return coord;
	}
	
	/*
	 * Method to calculate theta between two lines, bot to reading and bot to cell.
	 * For use in inverse sensor model.
	 * Parameters:
	 * reading_p - Reading real world point.
	 * cell_p - Cell centre real world point.
	 * botPos_p - Bot position real world point.
	 * Returns:
	 * Double representing theta between lines.
	 */
	public double getTheta(Point2D.Double reading_p, Point2D.Double cell_p,
							Point2D.Double botPos_p) {
		Line2D.Double line1 = new Line2D.Double(botPos_p, cell_p);
		Line2D.Double line2 = new Line2D.Double(botPos_p, reading_p);
		double angle1 = Math.toDegrees(
							Math.atan2(	line1.getY1() - line1.getY2(),
										line1.getX1() - line1.getX2()));
		double angle2 = Math.toDegrees(
							Math.atan2(	line2.getY1() - line2.getY2(),
										line2.getX1() - line2.getX2()));
		double diffAngle = Math.abs(angle1 - angle2);
		if (diffAngle > 180) {
			diffAngle -= 360;
		}
		if (diffAngle < -180) {
			diffAngle += 360;
		}		
		return (diffAngle);
	}
	
	/*
	 * Method to get distance between two points.
	 * Parameters:
	 * point1_p - First point for calculation.
	 * point2_p - Second point for calculation.
	 * Returns:
	 * Double representing distance between two points.
	 */
	public double getDistance(Point2D.Double point1_p, Point2D.Double point2_p) {
		double xDiff = point2_p.getX() - point1_p.getX();
		double yDiff = point2_p.getY() - point1_p.getY();
		return (Math.sqrt((xDiff*xDiff) + (yDiff*yDiff)));
	}
	
	/*
	 * Method encoding the sensor intensity function.
	 * Parameters:
	 * z_p - z from equation.
	 * theta_p - theta from equation.
	 * Returns:
	 * Double representing intensity value.
	 */
	public double getIntensity(double z_p, double theta_p) {
		double term1 = Math.exp(-(Math.pow((CONE_FACTOR * theta_p), 2)));
		double term2 = 1 - ((z_p)/900);
		double term3 = 0.4;
		return (term1 * term2 * term3);
	}
	
	/*
	 * Method encoding the occupancy by distance equation.
	 * Parameters:
	 * z_p - z from equation.
	 * d_p - d from equation.
	 * theta_p - theta from equation.
	 * Returns:
	 * Double representing inverse sensor model return value.
	 */
	public double inverseSensorModel(double z_p, double d_p, double theta_p) {
		double sVal = getIntensity(z_p, theta_p);
		double prior = 0.5;
		double addTerm;
		if (d_p < (z_p - D1)) {
			addTerm = -sVal;
		} else if (d_p < (z_p + D1)) {
			addTerm = (-sVal) + ((sVal/D1) * (d_p - z_p + D1));
		} else if (d_p < (z_p + D2)) {
			addTerm = sVal;
		} else if (d_p < (z_p + D3)) {
			addTerm = sVal - ((sVal/(D3 - D2)) * (d_p - z_p - D2));
		} else {
			addTerm = 0;
		}
		return (prior + addTerm);
	}
	
	public boolean updateCell(Point2D.Double measurement_p, Point cell_p,
								double theta_p, double z_p, double d_p) {
		//Get inverse sensor model value.
		double weight = inverseSensorModel(z_p, d_p, theta_p);
		if (weight != 0) {
			//Perform recursive update.
			double oldVal = getOccupancy(cell_p);
			double invSen = Math.log(weight/(1-weight));
			double newVal = invSen + oldVal;
			//Set cell value.
			setOccupancy(cell_p, newVal);
		}
		return true;
	}
	
	public boolean updateMap(DataScan scan_p) {
		//Add scan to scan history.
		scanList.add(scan_p);
		//Get variables.
		int[] values = scan_p.getValues();
		int rate = scan_p.getSRate();
		double angle = scan_p.getHeading();
		//Iterate through map cells.
		for (int x = 1; x <= mapWidth; x++) {
			for (int y = 1; y <= mapHeight; y++) {
				//Initialise update cell.
				Point cell = new Point(x, y);
				//Get cell centre point.
				Point2D.Double cellCenter = getCellCentre(cell); 
				//Iterate through scan readings.
				for (int val : values) {
					//Calculate measurement point.
					Point2D.Double mPoint = getMeasurementPoint(val, angle);
					//Calculate inverse sensor model parameters.
					double theta = getTheta(mPoint, cellCenter, botPos);
					double d = getDistance(botPos, cellCenter);
					double z = getDistance(botPos, mPoint);				
					//If value not max then update normally.
					if (val < 790) {
						updateCell(mPoint, cell, theta, z, d);
					} else {
						//If value max then update free space only.
						if (d < 800) {
							updateCell(mPoint, cell, theta, 800, d);
						}
					}
					//Increment angle.
					angle += rate;
					if (angle >= 360) {
						angle -= 360;
					}
				}
			}
		}
		return true;
	}
	
	public void updateBotState(Pose pose_p) {
		double x = originPoint.getX() + pose_p.getX();
		double y = originPoint.getY() + pose_p.getY();
		Point2D.Double botPoint = new Point2D.Double(x, y);
		setBotPosition(botPoint);
		setBotOrientation((double)pose_p.getHeading());
	}
	
	public boolean simpleMap(DataScan scan_p) throws IndexOutOfBoundsException {
		//Add scan to map scan history.
		scanList.add(scan_p);
		
		//Get initial variables from scan.
		int[] values = scan_p.getValues();
		int rate = scan_p.getSRate();
		double angle = scan_p.getHeading();
		
		//Iterate through scan readings.
		for (int val : values) {
			//Discard maximum range values.
			if (val < 790) {
				//Calculate coordinates of measurement.
				Point2D.Double mPoint = getMeasurementPoint(val, angle);
				//Get grid cell of measurement.
				Point cell = getPointCell(mPoint);
				//Set cell to occupied.
				double value = Math.log(1/(1-0.9));
				setOccupancy(cell, value);
			}
			//Increment angle value.
			angle += rate;
			if (angle >= 360) {
				angle -= 360;
			}
		}
		return true;
	}
	
	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawGrid(g2);
    }
	
	public void drawGrid(Graphics2D g) {
		//Initialise counter variables and component height and width.
		int heightCounter = 0, widthCounter = 0;
		int height = this.getHeight();
		int width = this.getWidth();
		
		//Calculate point for the top left of the map so that map is centred.
		int topLeftXPoint = (width - (mapWidth*cellSize)) / 2;
		int topLeftYPoint = (height - (mapHeight*cellSize)) / 2;
		
		//Set counters to bottom left corner point.
		widthCounter = topLeftXPoint;
		heightCounter = topLeftYPoint + ((mapHeight-1)*cellSize);
		
		//Iterate through cells.
		for (int j = 0; j < mapHeight; j++) {
			for (int i = 0; i < mapWidth; i++) {
				//Get matrix probability value.
				double probVal = (Math.exp(matrix[i][j]))/(1 + Math.exp(matrix[i][j]));
				//Determine colour value.
				int val = (int)(255 - (Math.floor(probVal * 255)));
				//Set paint colour to greyscale colour value.
				g.setPaint(new Color(val, val, val));
				//Define cell rectangle and draw cell.
				Rectangle2D.Double cell =
						new Rectangle2D.Double(widthCounter, heightCounter, cellSize, cellSize);
				g.fill(cell);
				//Increment width counter.
				widthCounter += cellSize;
			}
			//Reset width counter and decrement height counter.
			widthCounter = topLeftXPoint;
			heightCounter -= cellSize;
		}
		
		//Set paint colour to robot icon colour.
		g.setPaint(Color.BLUE);
		//Define robot icon size and location.
		int botIconSize = cellSize;
		int botXPx = (int)Math.floor(botPos.getX()/(mapRatio/cellSize));
		int botYPx = (int)Math.floor(botPos.getY()/(mapRatio/cellSize));
		int botIconX = topLeftXPoint + botXPx - (botIconSize/2);
		int botIconY = (topLeftYPoint + (mapHeight*cellSize)) - botYPx - (botIconSize/2);
		//Define robot icon shape and draw icon.
		Ellipse2D.Double botIcon =
				new Ellipse2D.Double(botIconX, botIconY, botIconSize, botIconSize);
		g.draw(botIcon);
		
		//Set paint colour to path line colour.
		g.setColor(Color.RED);
		//Iterate through path lines.
		for (Line2D.Double l : lineList) {
			//Get line coordinates.
			double x1 = l.getX1();
			double x2 = l.getX2();
			double y1 = l.getY1();
			double y2 = l.getY2();
			//Convert real coordinates to pixel coordinates.
			x1 = topLeftXPoint + Math.floor(x1/(mapRatio/cellSize));
			x2 = topLeftXPoint + Math.floor(x2/(mapRatio/cellSize));
			y1 = topLeftYPoint + (mapHeight*cellSize) - Math.floor(y1/(mapRatio/cellSize));
			y2 = topLeftYPoint + (mapHeight*cellSize) - Math.floor(y2/(mapRatio/cellSize));
			//Draw line.
			g.draw(new Line2D.Double(x1, y1, x2, y2));
		}
	}
	
	public void addLine(Point2D.Double point1_p, Point2D.Double point2_p) {
		lineList.add(new Line2D.Double(point1_p, point2_p));
	}
	
	public void removeLine(int index_p) {
		lineList.remove(index_p);
	}
	
	public ArrayList<MoveCommand> getCommandList() {
		return(commandList);
	}
	
	public MoveCommand getCommand(int index_p) {
		return(commandList.get(index_p));
	}
	
	public boolean setCommandList(ArrayList<MoveCommand> commandList_p) {
		commandList = commandList_p;
		return true;
	}
	
	public boolean setCommand(int index_p, MoveCommand moveCommand_p) {
		commandList.set(index_p, moveCommand_p);
		return true;
	}
	
	public boolean addCommand(MoveCommand command_p) {
		commandList.add(command_p);
		return true;
	}
	
	public ArrayList<DataScan> getScanList() {
		return(scanList);
	}
	
	public DataScan getScan(int index_p) {
		return(scanList.get(index_p));
	}
	
	public boolean setScanList(ArrayList<DataScan> scanList_p) {
		scanList = scanList_p;
		return true;
	}
	
	public boolean setScan(int index_p, DataScan scan_p) {
		scanList.set(index_p, scan_p);
		return true;
	}
	
	public boolean addScan(DataScan scan_p) {
		scanList.add(scan_p);
		return true;
	}
	
	public double[][] getMatrix() {
		return matrix;
	}
	
	public boolean setMatrix(double[][] matrix_p) {
		matrix = matrix_p;
		return true;
	}
	
	public double getRatio() {
		return(mapRatio);
	}
	
	public boolean setRatio(double ratio_p) {
		mapRatio = ratio_p;
		return true;
	}
	
	public int getCellSize() {
		return(cellSize);
	}
	
	public boolean setCellSize(int size_p) {
		cellSize = size_p;
		return true;
	}
	
	public boolean setMapSize(int height_p, int width_p) {
		mapHeight = height_p;
		mapWidth = width_p;
		return true;
	}
	
	public boolean setMapHeight(int height_p) {
		mapHeight = height_p;
		return true;
	}
	
	public boolean setMapWidth(int width_p) {
		mapWidth = width_p;
		return true;
	}
	
	public int getMapHeight() {
		return(mapHeight);
	}
	
	public int getMapWidth() {
		return(mapWidth);
	}
	
	public boolean setOccupancy(Point cell, double value) {
		if (((cell.getX() % 1) == 0) && ((cell.getY() % 1) == 0)) {
			int x = ((int)cell.getX()-1);
			int y = ((int)cell.getY()-1);
			matrix[x][y] = value;
			this.repaint();
			return true;
		} else {
			System.out.println(
					"OGM > setOccupancy - Error: Double precision point when expecting integer.");
			return false;
		}	
	}
	
	public double getOccupancy(Point cell) {
		if (((cell.getX() % 1) == 0) && ((cell.getY() % 1) == 0)) {
			int x = ((int)cell.getX()-1);
			int y = ((int)cell.getY()-1);
			return(matrix[x][y]);
		} else {
			System.out.println(
					"OGM > setOccupancy - Error: Double precision point when expecting integer.");
			return -1;
		}
	}
	
	public Point2D.Double getBotPosition() {
		return botPos;
	}
	
	public boolean setBotPosition(Point2D.Double botPos_p) {
		botPos = botPos_p;
		return true;
	}
	
	public double getBotOrientation() {
		return botOrientation;
	}
	
	public boolean setBotOrientation(double orientation_p) {
		botOrientation = orientation_p;
		if (botOrientation >= 360) {
			botOrientation -= 360;
		}
		return true;
	}
	
	public boolean incBotOrientation(int increment_p) {
		botOrientation += increment_p;
		if (botOrientation >= 360) {
			botOrientation -= 360;
		}
		return true;
	}
	
	public double[] getDConstants() {
		double[] d = {D1, D2, D3};
		return(d);
	}

}