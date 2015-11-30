/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.yearly.auto.steps.drivebase;

import org.wildstang.framework.auto.steps.AutoStep;
import org.wildstang.framework.core.Core;
import org.wildstang.yearly.robot.WSSubsystems;
import org.wildstang.yearly.subsystems.SixWheelDriveBase;

/**
 *
 * @author Joey
 */
public class StepSetShifter extends AutoStep
{
   protected boolean highGear;;

   public StepSetShifter(boolean highGear)
   {
      this.highGear = highGear;
   }

   @Override
   public void initialize()
   {
//      ((DriveBase) Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName())).setShifter(highGear);
      this.finished = true;
   }

   @Override
   public void update()
   {
   }

   @Override
   public String toString()
   {
      return "Set Shifter State";
   }

}
