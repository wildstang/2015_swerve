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
 * @author Nathan
 */
public class StepStartDriveUsingMotionProfile extends AutoStep
{

   double distance;
   double goal_velocity;

   public StepStartDriveUsingMotionProfile(double distance, double goal_velocity)
   {
      this.distance = distance;
      this.goal_velocity = goal_velocity;
   }

   @Override
   public void initialize()
   {
//      ((DriveBase) Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName())).startStraightMoveWithMotionProfile(distance, goal_velocity);
      finished = true;
   }

   @Override
   public void update()
   {
   }

   @Override
   public String toString()
   {
      return "Start the drive using motion profile for " + distance
            + " inches and reach going " + goal_velocity + " inches/second ";
   }
}
