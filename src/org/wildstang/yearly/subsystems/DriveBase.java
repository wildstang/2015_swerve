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
   double leftMag;
   double rightMag;
   double desiredAngle;
   final double DEADBAND = 0.0;
   boolean isOpposite;
   double potVal;
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
      if (source.getName() == WSInputs.POT.getName())
      {
         potVal = ((AnalogInput) source).getValue();
      }
      

   }

   @Override
   public void init()
   {
      Core.getInputManager().getInput(WSInputs.DRV_THROTTLE.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_LEFT_X.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_HEADING.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.POT.getName()).addInputListener(this);
      
   }

   @Override
   public void selfTest()
   {
      // TODO Auto-generated method stub

   }

   @Override
   public void update()
   {
	   encodeAngle = (potVal / 5) * (2 * Math.PI);
	 //magnitude - how powerful drive motors are running (-1 to 1)
	   //rotMag - how powerful the rotational motors are running (-1 to 1)
	   
	   //leftX - driver Left Joystick X value (-1 to 1)
	   //leftY - driver Left Joystick Y Value (-1 to 1)
	   
	   //encodeAngle - encoder angle readout (0 to 359.9)
	   //DEADBAND - final double that will be the max disparity between desired angle and the actual angle of the swerve modules
	   
	   if (leftX == 0 && leftY == 0 && rightX == 0) { //if no controller input
		   magnitude = 0;
		   rotMag = 0;
	   } else {
		   desiredAngle = Math.abs(getAngle(leftX, leftY));
		   magnitude = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2)); // here we get the raw magnitude from Pythagorean Theorem
		   if (getAngleDistance(encodeAngle, desiredAngle) > Math.PI / 2) {
			   isOpposite = true;
			   magnitude *= -1;
		   } else {
			   isOpposite = false;
		   }
		   rotMag = getRotMag(encodeAngle, desiredAngle);
		   
		   leftMag = adjustMagnitude(magnitude, -rightX, true);
		   rightMag = adjustMagnitude(magnitude, -rightX, false);
		   
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_URD.getName())).setValue(rightMag);
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_ULD.getName())).setValue(leftMag);
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_URR.getName())).setValue(rotMag);
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_ULR.getName())).setValue(rotMag);
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_LRD.getName())).setValue(rightMag);
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_LLD.getName())).setValue(leftMag);
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_LRR.getName())).setValue(rotMag);
		   ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_LLR.getName())).setValue(rotMag);
		  
//		   ((WsTalon)Core.getOutputManager().getOutput(WSOutputs.VICTOR_ULD.getName())).setValue(leftMag);
		   

	   }
	   SmartDashboard.putNumber("Magnitude", magnitude);
	   SmartDashboard.putNumber("Left Mag", leftMag);
	   SmartDashboard.putNumber("Right Mag", rightMag);
	   SmartDashboard.putNumber("Desired angle", desiredAngle);
	   SmartDashboard.putNumber("Left X", leftX);
	   SmartDashboard.putNumber("Left Y", leftY);
	   SmartDashboard.putNumber("pot", potVal);
	   SmartDashboard.putNumber("encoder", encodeAngle);
	   SmartDashboard.putNumber("rotMag", rotMag);
   }

   @Override
   public String getName()
   {
      return "Drive Base";
   }
   
   private double adjustMagnitude(double original, double rotation, boolean isLeft) {
	   if (isLeft) {
		   return limitMotor(original + (Math.abs(original) * rotation));
	   } else {
		   return limitMotor(original - (Math.abs(original) * rotation));
	   }
   }
   
   private double limitMotor(double magnitude) {
	   if (magnitude > 1) {
		   return 1d;
	   } else if (magnitude < -1){
		   return -1d;
	   } else {
		  return magnitude;
	   }
   }
   
   private double getRotMag(double actual, double desired) {
	   double rotateMag;
	   double oppositeDesired = limitAngle(Math.PI + desired);
	   if (isOpposite) {
		   if (getAbsAngleDistance(oppositeDesired, actual) < 0) {
			   rotateMag = 1d;
		   } else {
			   rotateMag = -1d;
		   }
	   } else {
		   if (getAbsAngleDistance(desired, actual) < 0) {
			   rotateMag = 1d;
		   } else {
			   rotateMag = -1d;
		   }
	   }
	   
	   return rotateMag;
   }
   
   private static double getAngle(double x, double y) {
	   double angle = 0;
	   if (y > 0) {
		   angle = limitAngle(Math.atan(x/y));
	   } else if(x >= 0 && y < 0) {
		   angle = Math.PI - limitAngle(Math.atan(x/y) );
	   } else if(x <= 0 && y < 0){
		   angle = Math.PI + limitAngle(Math.atan(x/y));
	   } 
   	    else if( y == 0 && x >= 0){
   		   angle = (Math.PI/2);
   	   } else {
   		   angle = (Math.PI * 1.5);
   	   }

	   return angle;
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
   
   private static double getAbsAngleDistance(double finalAngle, double initialAngle) {
	   double diff = getAngleDistance(finalAngle, initialAngle);
		   if (finalAngle < (1.5*Math.PI)) {
			   if (initialAngle > finalAngle) {
				   diff *= -1;
			   }
		   } else {
			   double oppositeFinal = finalAngle - (Math.PI);
			   if (initialAngle > finalAngle || initialAngle < oppositeFinal) {
				   diff *= -1;
			   }
		   }
	   
	   
	   
	   return diff;
   }
   
   private static double getAngleDistance(double angle1, double angle2) {
	   double diff = Math.abs(angle1 - angle2);
	   if (diff > Math.PI) {
		   diff = (Math.PI * 2) - diff;
	   }
	   return diff;
   }

}
