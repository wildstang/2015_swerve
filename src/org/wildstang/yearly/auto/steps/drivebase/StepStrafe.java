package org.wildstang.yearly.auto.steps.drivebase;

import org.wildstang.framework.auto.steps.AutoStep;
import org.wildstang.framework.core.Core;
import org.wildstang.yearly.robot.WSSubsystems;
import org.wildstang.yearly.subsystems.SixWheelDriveBase;

public class StepStrafe extends AutoStep
{

   private double strafe;

   // Left is negative, right is positive
   public StepStrafe(double strafe)
   {
      this.strafe = strafe;
   }

   @Override
   public void initialize()
   {
//      ((DriveBase) Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName())).overrideStrafeValue(strafe);
      finished = true;
   }

   @Override
   public void update()
   {
   }

   @Override
   public String toString()
   {
      return "Strafing";
   }
}
