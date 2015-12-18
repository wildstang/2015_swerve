package org.wildstang.yearly.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.Input;
import org.wildstang.framework.io.inputs.AnalogInput;
import org.wildstang.framework.io.inputs.DigitalInput;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.crio.outputs.WsVictor;
import org.wildstang.yearly.robot.WSInputs;
import org.wildstang.yearly.robot.WSOutputs;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CopyOfDriveBase implements Subsystem
{
   double leftX;
   double leftY;
   double rightX;
   boolean button9, button10;
   double magnitude;
   double rotMagUR, rotMagUL, rotMagLR, rotMagLL;
   double encodeAngleUR, encodeAngleUL, encodeAngleLR, encodeAngleLL;
   double leftMag;
   double rightMag;
   double desiredAngleUR, desiredAngleUL, desiredAngleLR, desiredAngleLL,
         desiredAngle;
   final double DEADBAND = 1;
   final double JOYSTICKDEADBAND = .02;
   final double HOMEROTATESPEED = .05;
   boolean isOpposite;
   WsVictor VictorURD, 
            VictorULD, 
            VictorURR, 
            VictorULR, 
            VictorLRD, 
            VictorLLD,
            VictorLRR, 
            VictorLLR;
   boolean HallEffectUR, HallEffectUL, HallEffectLR, HallEffectLL;
   double encoderOffsetUR = 0, encoderOffsetUL = 0, encoderOffsetLR = 0,
         encoderOffsetLL = 0;

   /*
    * Constructor should not take args to insure that it can be instantiated via
    * reflection.
    */
   public CopyOfDriveBase()
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
      if (source.getName().equals(WSInputs.ABSOLUTE_ENCODER1.getName()))
      {
         encodeAngleUR = ((AnalogInput) source).getValue();
      }
      if (source.getName().equals(WSInputs.ABSOLUTE_ENCODER2.getName()))
      {
         encodeAngleUL = ((AnalogInput) source).getValue();
      }
      if (source.getName().equals(WSInputs.ABSOLUTE_ENCODER3.getName()))
      {
         encodeAngleLR = ((AnalogInput) source).getValue();
      }
      if (source.getName().equals(WSInputs.ABSOLUTE_ENCODER4.getName()))
      {
         encodeAngleLL = ((AnalogInput) source).getValue();
      }
      if (source.getName().equals(WSInputs.HALL_EFFECT1.getName()))
      {
         HallEffectUR = ((DigitalInput) source).getValue();
      }
      if (source.getName().equals(WSInputs.HALL_EFFECT2.getName()))
      {
         HallEffectUL = ((DigitalInput) source).getValue();
      }
      if (source.getName().equals(WSInputs.HALL_EFFECT3.getName()))
      {
         HallEffectLR = ((DigitalInput) source).getValue();
      }
      if (source.getName().equals(WSInputs.HALL_EFFECT4.getName()))
      {
         HallEffectLL = ((DigitalInput) source).getValue();
      }
      if (source.getName().equals(WSInputs.DRV_BUTTON_9.getName()))
      {
         button9 = ((DigitalInput) source).getValue();
      }
      if (source.getName().equals(WSInputs.DRV_BUTTON_10.getName()))
      {
         button10 = ((DigitalInput) source).getValue();
      }
   }

   @Override
   public void init()
   {
      // add listeners for joysticks and absolute encoders
      Core.getInputManager().getInput(WSInputs.DRV_THROTTLE.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_LEFT_X.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_HEADING.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.ABSOLUTE_ENCODER1.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.ABSOLUTE_ENCODER2.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.ABSOLUTE_ENCODER3.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.ABSOLUTE_ENCODER4.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.HALL_EFFECT1.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.HALL_EFFECT2.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.HALL_EFFECT3.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.HALL_EFFECT4.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_9.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_10.getName()).addInputListener(this);
      // assign variables for Victors
      VictorURD = (WsVictor) Core.getOutputManager().getOutput(WSOutputs.VICTOR_URD.getName());
      VictorULD = (WsVictor) Core.getOutputManager().getOutput(WSOutputs.VICTOR_ULD.getName());
      VictorURR = (WsVictor) Core.getOutputManager().getOutput(WSOutputs.VICTOR_URR.getName());
      VictorULR = (WsVictor) Core.getOutputManager().getOutput(WSOutputs.VICTOR_ULR.getName());
      VictorLRD = (WsVictor) Core.getOutputManager().getOutput(WSOutputs.VICTOR_LRD.getName());
      VictorLLD = (WsVictor) Core.getOutputManager().getOutput(WSOutputs.VICTOR_LLD.getName());
      VictorLRR = (WsVictor) Core.getOutputManager().getOutput(WSOutputs.VICTOR_LRR.getName());
      VictorLLR = (WsVictor) Core.getOutputManager().getOutput(WSOutputs.VICTOR_LLR.getName());

   }

   @Override
   public void selfTest()
   {
      // TODO Auto-generated method stub

   }

   @Override
   public void update()
   {
      // magnitude - how powerful drive motors are running (-1 to 1)
      // rotMag - how powerful the rotational motors are running (-1 to 1)

      // leftX - driver Left Joystick X value (-1 to 1)
      // leftY - driver Left Joystick Y Value (-1 to 1)

      // encodeAngle - encoder angle readout (0 to 359.9)
      // DEADBAND - final double that will be the max disparity between desired
      // angle and the actual angle of the swerve modules

      // If home and select are pressed, rotate the wheels to their home position
      if (button9 && button10)
      {
         phoneHome(HOMEROTATESPEED);
      }

      // Allow a deadband for small joystick offsets
      if (Math.abs(leftX) < JOYSTICKDEADBAND) leftX = 0;
      if (Math.abs(leftY) < JOYSTICKDEADBAND) leftY = 0;
      if (Math.abs(rightX) < JOYSTICKDEADBAND) rightX = 0;

      if (leftX == 0 && leftY == 0 && rightX == 0)
      { // if no controller input
         magnitude = 0;
         rotMagUR = 0;
         rotMagUL = 0;
         rotMagLR = 0;
         rotMagLL = 0;
      }
      else
      {
         // Determine target angle from joystick position
         desiredAngle = Math.abs(getAngle(leftX, leftY));
         
         // Add the offset for each encoder and limit to 360 degrees for individual rotation output
         desiredAngleUR = limitAngle(desiredAngle + encoderOffsetUR);
         desiredAngleUL = limitAngle(desiredAngle + encoderOffsetUL);
         desiredAngleLR = limitAngle(desiredAngle + encoderOffsetLR);
         desiredAngleLL = limitAngle(desiredAngle + encoderOffsetLL);

         // Determine magnitude of stick deflection for output speed
         magnitude = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2)); // here we get the raw magnitude from Pythagorean Theorem
         
         // If the target angle is more than 90 degrees away, turn the other direction and change drive direction
         if (getAngleDistance(encodeAngleUR, desiredAngleUR) > 90)
         {
            isOpposite = true;
            magnitude *= -1;
         }
         else
         {
            isOpposite = false;
         }
         rotMagUR = getRotMag(encodeAngleUR, desiredAngleUR);
         rotMagUL = getRotMag(encodeAngleUL, desiredAngleUL);
         rotMagLR = getRotMag(encodeAngleLR, desiredAngleLR);
         rotMagLL = getRotMag(encodeAngleLL, desiredAngleLL);

         // For small input magnitude (small movements) scale the output to give a slower lead-in ramp up
         if (Math.abs(magnitude) < .25)
         {
            boolean isPositive = true;
            if (magnitude < 0)
            {
               isPositive = false;
            }
            magnitude = Math.pow(magnitude, 2) * 4;
            if (!isPositive)
            {
               magnitude *= -1;
            }
         }

         // Adjust the output magnitude by any rotation amount to allow the robot to turn
         leftMag = adjustMagnitude(magnitude, -rightX, true);
         rightMag = adjustMagnitude(magnitude, -rightX, false);

      }
      // assign motor values
      VictorURD.setValue(rightMag);
      VictorULD.setValue(leftMag);
      VictorURR.setValue(rotMagUR);
      VictorULR.setValue(rotMagUL);
      VictorLRD.setValue(rightMag);
      VictorLLD.setValue(leftMag);
      VictorLRR.setValue(rotMagLR);
      VictorLLR.setValue(rotMagLL);

      // print out important values to dashboard
      SmartDashboard.putNumber("Magnitude", magnitude);
      SmartDashboard.putNumber("Left Mag", leftMag);
      SmartDashboard.putNumber("Right Mag", rightMag);
      SmartDashboard.putNumber("Desired angle", desiredAngle);
      SmartDashboard.putNumber("Left X", leftX);
      SmartDashboard.putNumber("Left Y", leftY);
      SmartDashboard.putNumber("encoder", encodeAngleUR);
      SmartDashboard.putNumber("rotMagUR", rotMagUR);
   }

   @Override
   public String getName()
   {
      return "Drive Base";
   }

   // secondary method
   // Accounts for rotation in drive motors
   private double adjustMagnitude(double original, double rotation, boolean isLeft)
   {
      if (isLeft)
      {
         return limitMotor(original + (Math.abs(original) * rotation));
      }
      else
      {
         return limitMotor(original - (Math.abs(original) * rotation));
      }
   }

   // primary method, calls no others
   // limits motor output to 1
   private double limitMotor(double magnitude)
   {
      if (magnitude > 1)
      {
         return 1d;
      }
      else if (magnitude < -1)
      {
         return -1d;
      }
      else
      {
         return magnitude;
      }
   }

   // big method (2 primary (limitAngle/getAngleDistance), 1
   // secondary(getAbsAngleDistance))
   // sets mag and direction of rotation motor
   private double getRotMag(double actual, double desired)
   {
      double rotateMag;
      double oppositeDesired = limitAngle(180 + desired);
      double angleDistance = getAngleDistance(desired, actual);
      if (isOpposite)
      {
         if (getAbsAngleDistance(oppositeDesired, actual) < 0)
         {
            // rotateMag = 1d / 2;
            rotateMag = -1d / 2;
         }
         else
         {
            rotateMag = 1d / 2;
            // rotateMag = -1d / 2;
         }
      }
      else
      {
         if (getAbsAngleDistance(desired, actual) < 0)
         {
            // rotateMag = 1d / 2;
            rotateMag = -1d / 2;
         }
         else
         {
            rotateMag = 1d / 2;
            // rotateMag = -1d / 2;
         }
      }

      rotateMag /= 5;

      if (Math.abs(angleDistance) < 20)
      {
         // Deadband = 1 degree
         rotateMag *= (angleDistance / 20);
         if (angleDistance < DEADBAND)
         {
            return 0;
         }
      }
      return rotateMag;
   }

   // secondary, 1 primary call
   // Finds angle of driver joystick
   private static double getAngle(double x, double y)
   {
      double angle = 0;
      if (y > 0)
      {
         // 
         angle = limitAngle(Math.atan(x / y) * 180 / Math.PI);
      }
      else if (x >= 0 && y < 0)
      {
         angle = 180 - limitAngle(Math.atan(x / y) * 180 / Math.PI);
      }
      else if (x <= 0 && y < 0)
      {
         angle = 180 + limitAngle(Math.atan(x / y) * 180 / Math.PI);
      }
      else if (y == 0 && x >= 0)
      {
         angle = 90;
      }
      else
      {
         angle = 270;
      }

      return angle;
   }

   // primary
   // runs angle measures over (negative value goes to 359, above 360 goes to 1)
   private static double limitAngle(double oldAngle)
   {
      double newAngle = oldAngle;
      while (newAngle >= 360)
      {
         newAngle -= 360;
      }
      while (newAngle < 0)
      {
         newAngle += 360;
      }

      return newAngle;
   }

   // secondary, 1 primary call
   // returns positive or negative angle distance
   private static double getAbsAngleDistance(double finalAngle,
         double initialAngle)
   {
      double diff = getAngleDistance(finalAngle, initialAngle);
      if (finalAngle < 270)
      {
         if (initialAngle > finalAngle)
         {
            diff *= -1;
         }
      }
      else
      {
         double oppositeFinal = finalAngle - 180;
         if (initialAngle > finalAngle || initialAngle < oppositeFinal)
         {
            diff *= -1;
         }
      }

      return diff;
   }

   // primary
   // returns magnitude of angle distance
   private static double getAngleDistance(double angle1, double angle2)
   {
      double diff = Math.abs(angle1 - angle2);
      if (diff > 180)
      {
         diff = 360 - diff;
      }
      return diff;
   }

   // primary
   // runs wheels until home position found
   private void setHomePosition(double rotateSpeed)
   {
      while (!HallEffectUR)
      {
         VictorURR.setValue(rotateSpeed);
      }
      while (!HallEffectUL)
      {
         VictorULR.setValue(rotateSpeed);
      }
      while (!HallEffectLR)
      {
         VictorLRR.setValue(rotateSpeed);
      }
      while (!HallEffectLL)
      {
         VictorLLR.setValue(rotateSpeed);
      }
   }

   // secondary, 1 primary call
   // sets home position & encoder offset
   private void phoneHome(double rotateSpeed)
   {
      setHomePosition(rotateSpeed);
      encoderOffsetUR = encodeAngleUR;
      encoderOffsetUL = encodeAngleUL;
      encoderOffsetLR = encodeAngleLR;
      encoderOffsetLL = encodeAngleLL;
   }
}
