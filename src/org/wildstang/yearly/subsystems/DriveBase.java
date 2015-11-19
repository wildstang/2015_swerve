package org.wildstang.yearly.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.Input;
import org.wildstang.framework.io.inputs.AnalogInput;
import org.wildstang.framework.io.inputs.DigitalInput;
import org.wildstang.framework.io.outputs.AnalogOutput;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.crio.outputs.WsSolenoid;
import org.wildstang.hardware.crio.outputs.WsTalon;
import org.wildstang.hardware.crio.outputs.WsVictor;
import org.wildstang.yearly.robot.WSInputs;
import org.wildstang.yearly.robot.WSOutputs;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveBase implements Subsystem
{
   double leftX;
   double leftY;
   double rightX;
   double magnitude;
   double rotMag;
   double encodeAngle;
   final double DEADBAND = 0.0;
  /* Constructor should not take args to insure that it can be instantiated via reflection. */
   public DriveBase()
   {

   }

   @Override
   public void inputUpdate(Input source)
   {

      if (source.getName() == WSInputs.DRV_THROTTLE.getName())
      {
         leftY = ((AnalogInput) source).getValue();
      }
      if (source.getName() == WSInputs.DRV_LEFT_X.getName())
      {
         leftX = ((AnalogInput) source).getValue();
      }
      if (source.getName() == WSInputs.DRV_HEADING.getName())
      {
         rightX = ((AnalogInput) source).getValue();
      }
//      if (source.getName().equals(WSInputs.ENCODER.getName()))
//      {
//         encodeAngle = ((AnalogInput)source).getValue();
//      }
      

   }

   @Override
   public void init()
   {
      Core.getInputManager().getInput(WSInputs.DRV_THROTTLE.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_LEFT_X.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_HEADING.getName()).addInputListener(this);
      
   }

   @Override
   public void selfTest()
   {
      // TODO Auto-generated method stub

   }

   @Override
   public void update()
   {
	 //magnitude - how powerful drive motors are running (-1 to 1)
	   //rotMag - how powerful the rotational motors are running (-1 to 1)
	   
	   //leftX - driver Left Joystick X value (-1 to 1)
	   //leftY - driver Left Joystick Y Value (-1 to 1)
	   
	   //encodeAngle - encoder angle readout (0 to 359.9)
	   //DEADBAND - final double that will be the max disparity between desired angle and the actual angle of the swerve modules
	   
	   if (leftX == 0 && leftY == 0) { //if no controller input
		   magnitude = 0;
		   rotMag = 0;
	   } else {
		   double desiredAngle = getAngle(leftX, leftY);
		   rotMag = getRotMag(encodeAngle, desiredAngle);
		   double unscaledMagnitude = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2)); // here we get the raw magnitude from Pythagorean Theorem
		   if (Math.abs(leftX) >= Math.abs(leftY)) { // This little algorithm should scale it by dividing the current raw magnitude by the maximum raw magnitude.
			   magnitude = unscaledMagnitude * Math.sin(desiredAngle);
		   } else {
			   magnitude = unscaledMagnitude * Math.cos(desiredAngle);
		   }
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_URR.getName())).setValue(rotMag);
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_ULR.getName())).setValue(rotMag);
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_LRR.getName())).setValue(rotMag);
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_LLR.getName())).setValue(rotMag);
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_URD.getName())).setValue(magnitude);
//		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_ULD.getName())).setValue(magnitude);
		   ((WsTalon)Core.getOutputManager().getOutput(WSOutputs.VICTOR_ULD.getName())).setValue(magnitude);
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_LRD.getName())).setValue(magnitude);
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_LLD.getName())).setValue(magnitude);
		   SmartDashboard.putNumber("Magnitude", magnitude);
		   SmartDashboard.putNumber("Desired angle", desiredAngle);
		   
	   }

   }

   @Override
   public String getName()
   {
      return "Drive Base";
   }
   
   private double getRotMag(double actual, double desired) {
	   double oppositeDesired = limitAngle(desired + 180); // get the opposite of the desired angle
	   double rotateMag;
	   double dummyDesired; //These dummy variables fix the cases where the desired angle is in between 270 and 359.9
	   double dummyActual;
	   if (desired >= 270 && (actual > 0 && actual < 90)) {
		   dummyDesired = desired - 90;
		   dummyActual = actual - 90;
	   } else {
		   dummyDesired = desired;
		   dummyActual = actual;
	   }
	   if (actual >= desired - DEADBAND && actual <= desired + DEADBAND) {
		   rotateMag = 0;
	   } else if ((Math.abs(actual - desired) < Math.abs(actual - oppositeDesired)) && // if actual is closer to desired
			   (limitAngle(dummyActual) > limitAngle(dummyDesired) && desired < 270.0)) { // and the actual is to the left of desired
		   rotateMag = -1.0;
	   } else {
		   rotateMag = 1.0;
	   }
	   if (!(Math.abs(actual - desired) > Math.abs(actual - oppositeDesired))) { //going to the opposite of desired angle (run motors in reverse)
		   magnitude *= -1;
	   }
	   return rotateMag;
   }
   
   private static double getAngle(double x, double y) {
	   double angle = 0;
	   if (x >= 0 && y > 0) {
		   angle = limitAngle(Math.atan(x/y));
	   } else if(x >= 0 && y < 0) {
		   angle = Math.PI - limitAngle(Math.atan(x/y) );
	   } else if(x <= 0 && y < 0){
		   angle = Math.PI + limitAngle(Math.atan(x/y));
	   } else if(x <= 0 && y > 0){
		   angle = 2*(Math.PI) - limitAngle(Math.atan(x/y));
   	   } else if( y == 0 && x >= 0){
   		   angle = (Math.PI/2);
   	   } else {
   		   angle = (Math.PI * 1.5);
   	   }

	   return Math.abs(angle);
   }
   
   private static double limitAngle(double oldAngle) {
	   double newAngle = oldAngle;
	   while(newAngle >= (2*Math.PI)) {
		   newAngle -= (2*Math.PI);
	   }
	   while(newAngle < 0) {
		   newAngle += (2*Math.PI);
	   }
	   
	   return newAngle;
   }

}
