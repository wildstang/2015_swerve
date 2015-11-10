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
   // WsVictor victor1;
   // WsVictor victor2; NO - new framework doesn't require this
   double rightThrottle;
   double leftThrottle;
   double manipulator;
   boolean pneumatic;

   public DriveBase(String name)
   {

   }

   @Override
   public void inputUpdate(Input source)
   {
      if (source.getName() == WSInputs.DRV_RIGHT_Y.getName())
      {
    	  rightThrottle = ((AnalogInput) source).getValue();
      }
      if (source.getName() == WSInputs.DRV_THROTTLE.getName())
      {
         leftThrottle = ((AnalogInput) source).getValue();
      }
      if (source.getName() == WSInputs.MAN_RIGHT_JOYSTICK_Y.getName())
      {
          manipulator = ((AnalogInput) source).getValue();
       }
      if (source.getName() == WSInputs.DRV_BUTTON_1.getName())
      {
          pneumatic = ((DigitalInput)source).getValue();
       }
   }

   @Override
   public void init()
   {
      Core.getInputManager().getInput(WSInputs.DRV_RIGHT_Y.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_THROTTLE.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.MAN_RIGHT_JOYSTICK_Y.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_1.getName()).addInputListener(this);
   }

   @Override
   public void selfTest()
   {
      // TODO Auto-generated method stub

   }

   @Override
   public void update()
   {
      ((WsVictor) Core.getOutputManager().getOutput(WSOutputs.VICTOR.getName())).setValue(rightThrottle);
      ((WsTalon) Core.getOutputManager().getOutput(WSOutputs.TALON.getName())).setValue(leftThrottle);
      ((WsVictor) Core.getOutputManager().getOutput(WSOutputs.VICTOR_SP.getName())).setValue(manipulator);
      ((WsSolenoid) Core.getOutputManager().getOutput(WSOutputs.SINGLE.getName())).setValue(pneumatic);

   }

   @Override
   public String getName()
   {
      return "Drive Base";
   }

}
