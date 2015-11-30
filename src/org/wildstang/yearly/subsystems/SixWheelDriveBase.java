package org.wildstang.yearly.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.Input;
import org.wildstang.framework.io.inputs.AnalogInput;
import org.wildstang.framework.io.outputs.AnalogOutput;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.yearly.robot.WSInputs;
import org.wildstang.yearly.robot.WSOutputs;

public class SixWheelDriveBase implements Subsystem
{
  private double m_currentHeading;
  private double m_currentThrottle;
  private double leftSpeed;
  private double rightSpeed;
  /* Constructor should not take args to insure that it can be instantiated via reflection. */
   public SixWheelDriveBase()
   {

   }

   @Override
   public void inputUpdate(Input source)
   {
	   
	   	if (source.getName().equals(WSInputs.DRV_HEADING.getName()))
   		{
	   		m_currentHeading = ((AnalogInput)source).getValue();
   		}
	   	else if (source.getName().equals(WSInputs.DRV_THROTTLE.getName()))
	   	{
	   		m_currentThrottle = ((AnalogInput)source).getValue();
	   	}
//      if (source.getName() == WSInputs.DRV_BUTTON_1.getName())
//      {
//
//      }
//      if (source.getName() == WSInputs.DRV_THROTTLE.getName())
//      {
//         throttle = ((AnalogInput) source).getValue();
//      }
   }

   @Override
   public void init()
   {
      Core.getInputManager().getInput(WSInputs.DRV_HEADING.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_THROTTLE.getName()).addInputListener(this);
   }

   @Override
   public void selfTest()
   {
      // TODO Auto-generated method stub

   }

   @Override
   public void update()
   {
	   /* Quick and dirty 6 wheel drive control. */
	   if (m_currentThrottle > -0.1 && m_currentThrottle < 0.1)
	   {
		   /* Zero Point turn */
		   if (m_currentHeading > 0.1)
		   {
			   leftSpeed = Math.abs(m_currentHeading);
			   rightSpeed = -1 * Math.abs(m_currentHeading);
		   }
		   else if (m_currentHeading < -0.1)
		   {
			   rightSpeed = -1 * Math.abs(m_currentHeading); 
			   leftSpeed = Math.abs(m_currentHeading);
		   }
		   else
		   {
			   rightSpeed = 0;
			   leftSpeed = 0;
		   }
	   }
	   else if (m_currentHeading > 0.1)
	   {
		   leftSpeed = m_currentThrottle * (1 - Math.abs(m_currentHeading));
		   rightSpeed = m_currentThrottle;
	   }
	   else if (m_currentHeading < -0.1)
	   {
		   rightSpeed = m_currentThrottle * (1 - Math.abs(m_currentHeading)); 
		   leftSpeed = m_currentThrottle;
	   }
	   else
	   {
		   leftSpeed = m_currentThrottle;
	   	   rightSpeed = m_currentThrottle;
	   }
	   	   
      ((AnalogOutput) Core.getOutputManager().getOutput(WSOutputs.VICTOR_1_LEFT.getName())).setValue(leftSpeed);
      ((AnalogOutput) Core.getOutputManager().getOutput(WSOutputs.VICTOR_2_LEFT.getName())).setValue(leftSpeed);
      ((AnalogOutput) Core.getOutputManager().getOutput(WSOutputs.VICTOR_1_RIGHT.getName())).setValue(rightSpeed);
      ((AnalogOutput) Core.getOutputManager().getOutput(WSOutputs.VICTOR_2_RIGHT.getName())).setValue(rightSpeed);
   }

   @Override
   public String getName()
   {
      return "Drive Base";
   }

}
