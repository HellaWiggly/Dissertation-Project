To run the application you will need an NXT with leJOS NXJ firmware installed.
This can be found here:
http://www.lejos.org/nxj-downloads.php

You will also need a copy of eclipse with the leJOS plug-ins installed.

A setup tutorial for installing leJOS and configuring eclipse can be found here:
http://www.bartneck.de/2008/03/04/java-lego-nxt-eclipse-tutorial/

Also at these places on the official website:
http://www.lejos.org/nxt/nxj/tutorial/Preliminaries/GettingStartedWindows.htm
http://www.lejos.org/nxt/nxj/tutorial/Preliminaries/UsingEclipse.htm

Load the PCApp project into eclipse as a leJOS NXT project,
and the NXTProgram project into eclipse as a leJOS PC project.

The difference between these and how to set them up is explained in the
'Using Eclipse' tutorial above.

As detailed in the tutorial use the leJOS NXT project to compile and upload
NXTProgram.java to the NXT brick.

You can then run the PC application by running PCApp.java in the leJOS PC project
just like you would a normal java application in Eclipse.

You must start the NXTProgram.java file running on the robot in order to connect
to it using the PC application.

If the NXT is running NXTProgram then the connection is automatically established
when you choose and confirm a function on the PC application.

Currently errors might occur if you execute certain functions one after the other.
If this happens you should reboot the NXT and the PC application in between functions.

You can reboot the NXT by executing 'Close Connection' in the PC app.
If this doesn't work you can reboot it manually by holding in the confirm
and cancel buttons on the brick at the same time.

Updates to the project fixing these issues so that rebooting between functions is not
necessary should be added soon.

For the code to work with your NXT robot the robot needs to meet the following criterea:

- Have two motor powered wheels with the left connected to port B and the right to port A.
- Adjust the constant variables in the NXTProgram.java file to match the track width
  and wheel diameter of your robot.
- Have the motor for sensor rotation connected to port C.
- Have a range sensor compatible with the OpticalDistanceSensor leJOS class connected
  to sensor port 4.

To connect to the NXT using the PC application you will need to find the NXT MAC address
and insert it in the constant variable in Model.java.