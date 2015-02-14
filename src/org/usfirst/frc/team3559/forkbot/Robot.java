
package org.usfirst.frc.team3559.forkbot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private RobotDrive chassis;
	Joystick gamepad;
	Talon left_motor, right_motor, winch;
	DigitalInput lsbr, lsbl, lstr, lstl; //lsbr=Limit Switch Bottom Right, lsbl=Limit Switch Bottom Left, lstr=Limit Switch Top Right, lstl=Limit Switch Top Left
	public double sens = 0.1;
	public double maxO = 0.4;
	
	
    public void robotInit() {
    	left_motor = new Talon(8);
    	right_motor = new Talon(9);
    	chassis = new RobotDrive(left_motor, right_motor); //Motors
		chassis.setSensitivity(sens);
		chassis.setMaxOutput(maxO); //Maximum output limit "Governor Chip"
		gamepad = new Joystick(1);
		lsbr = new DigitalInput(0); //Limit Switch Bottom Right
		lsbl = new DigitalInput(1); //Limit Switch Bottom Left
		lstr = new DigitalInput(2); //Limit Switch Top Right
		lstl = new DigitalInput(3); //Limit Switch Top Left
		winch = new Talon(0);
		LiveWindow.addActuator("Talon", 8, left_motor);
		LiveWindow.addActuator("Talon", 9, right_motor);
		LiveWindow.addActuator("Winch Motor", 0, winch);
		CameraServer server = CameraServer.getInstance(); //Camera initialization
		server.setQuality(20);
		server.startAutomaticCapture("cam0");

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	chassis.setSafetyEnabled(false);
    	chassis.drive(-0.5,  -0.5);
    	Timer.delay(0.5);
    	chassis.drive(0.0, 0.0);
    }

    /**
     * This function is called periodically during operator control
     */
    public void driving() {
    	chassis.tankDrive(-gamepad.getY(), -gamepad.getRawAxis(5));
    	Timer.delay(0.01);	
    }
    public void lift(){
    	while (gamepad.getRawButton(5) == true && lsbr.get() && lsbl.get()){
			winch.set(-0.5);
	}
    	while (gamepad.getRawButton(6) == true && lstr.get() && lstl.get()){
		winch.set(0.5);
	}
}
    public void forks(){
    	
    }
    
    public void teleopPeriodic() {
    	while (isOperatorControl() && isEnabled()) {
			winch.set(0.0);
			lift();
			forks();
			driving();
			chassis.tankDrive(-gamepad.getY(), -gamepad.getRawAxis(5));
			//Timer.delay(0.01);
			//while (gamepad.getRawButton(5) == true && lsbr.get() && lsbl.get()){
					//winch.set(-0.5);
			//}
			//while (gamepad.getRawButton(6) == true && lstr.get() && lstl.get()){
				//winch.set(0.5);
			//}
    	}
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
}
