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

public class DriveBase implements Subsystem
{
   double leftX;
   double leftY;
   double rightX;

   public DriveBase(String name)
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
//      ((WsVictor) Core.getOutputManager().getOutput(WSOutputs.VICTOR.getName())).setValue(rightThrottle);
//      ((WsTalon) Core.getOutputManager().getOutput(WSOutputs.TALON.getName())).setValue(leftThrottle);
//      ((WsVictor) Core.getOutputManager().getOutput(WSOutputs.VICTOR_SP.getName())).setValue(manipulator);
//      ((WsSolenoid) Core.getOutputManager().getOutput(WSOutputs.SINGLE.getName())).setValue(pneumatic);

   }

   @Override
   public String getName()
   {
      return "Drive Base";
   }

}
