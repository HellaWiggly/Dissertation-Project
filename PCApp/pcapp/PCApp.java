/*
 * PCApp.java
 * Author: Matt Adshead
 * Date: 6 Dec 2013
 * 
 * Static runner class for PC application.
 *  
 * This object creates the MVC objects to start up the program.
 */

package mattadshead.swansea3.dissertation.pcapp;

import java.io.IOException;

import lejos.pc.comm.NXTCommException;
import mattadshead.swansea3.dissertation.structures.OGM;

public class PCApp {

	//Model object.
	private static Model model;
	//View object.
	private static View view;
	//Controller object.
	private static Controller controller;
	
	public static void main(String[] args) throws NXTCommException {
		//Create model, view, and controller objects.
		view = new View(null);
		model = new Model(view);
		controller = new Controller(model);
		view.setController(controller);
	}
}