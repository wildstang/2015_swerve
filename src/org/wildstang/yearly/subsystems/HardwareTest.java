package org.wildstang.yearly.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.Input;
import org.wildstang.framework.io.inputs.AnalogInput;
import org.wildstang.framework.io.inputs.DigitalInput;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.yearly.robot.WSInputs;
import org.wildstang.yearly.robot.WSOutputs;

import edu.wpi.first.wpilibj.DriverStation;

public class HardwareTest implements Subsystem
{
   boolean m_limitSwitch;
   double m_throttle = 0.0;
   private String m_name = "";
   private double potVal = 0.0;
   boolean b2, b3, b4;
   
   private int m_cycleCount = 0;
   /* Constructor should not take args to insure that it can be instantiated via reflection. */
   public HardwareTest()
   {
      m_name = "Hardware Test";
   }
   
   @Override
   public void inputUpdate(Input source)
   {
//      if (source.getName().equals(WSInputs.LIMIT_SWITCH.getName()))
//      {
//         m_limitSwitch = ((DigitalInput)source).getValue();
//      }
    /*  else */if (source.getName().equals(WSInputs.DRV_BUTTON_1.getName()))
      {
//         printButtonState(source, 1);
         if (((DigitalInput)source).getValue())
         {
            m_limitSwitch = true;
         }
         else
         {
            m_limitSwitch = false;
         }
      }
      else if (source.getName().equals(WSInputs.DRV_BUTTON_2.getName()))
      {
//         printButtonState(source, 3);
         b2 = ((DigitalInput)source).getValue();
      }
      else if (source.getName().equals(WSInputs.DRV_BUTTON_3.getName()))
      {
//         printButtonState(source, 3);
         b3 = ((DigitalInput)source).getValue();
      }
      else if (source.getName().equals(WSInputs.DRV_BUTTON_4.getName()))
      {
//         printButtonState(source, 4);
         b4 = ((DigitalInput)source).getValue();
      }
//      else if (source.getName().equals(WSInputs.DRV_BUTTON_5.getName()))
//      {
//         printButtonState(source, 5);
//      }
//      else if (source.getName().equals(WSInputs.DRV_BUTTON_6.getName()))
//      {
//         printButtonState(source, 6);
//      }
//      else if (source.getName().equals(WSInputs.DRV_BUTTON_7.getName()))
//      {
//         printButtonState(source, 7);
//      }
//      else if (source.getName().equals(WSInputs.DRV_BUTTON_8.getName()))
//      {
//         printButtonState(source, 8);
//      }
//      else if (source.getName().equals(WSInputs.DRV_BUTTON_9.getName()))
//      {
//         printButtonState(source, 9);
//      }
//      else if (source.getName().equals(WSInputs.DRV_BUTTON_10.getName()))
//      {
//         printButtonState(source, 10);
//      }
//      else if (source.getName().equals(WSInputs.DRV_BUTTON_11.getName()))
//      {
//         printButtonState(source, 11);
//      }
//      else if (source.getName().equals(WSInputs.DRV_BUTTON_12.getName()))
//      {
//         printButtonState(source, 12);
//      }
//      else if (source.getName().equals(WSInputs.DRV_HEADING.getName()))
//      {
//         printJoystickState(source);
//      }
//      else 
         if (source.getName().equals(WSInputs.DRV_THROTTLE.getName()))
      {
//         printJoystickState(source);
         m_throttle = ((AnalogInput)source).getValue();
      }
//         if (source.getName().equals(WSInputs.POT.getName()))
//      {
//         printJoystickState(source);
//         potVal = ((AnalogInput)source).getValue();
//      }
//      else if (source.getName().equals(WSInputs.DRV_LEFT_X.getName()))
//      {
//         printJoystickState(source);
//      }
//      else if (source.getName().equals(WSInputs.DRV_RIGHT_Y.getName()))
//      {
//         printJoystickState(source);
//      }
//      else if (source.getName().equals(WSInputs.DRV_DPAD_X.getName()))
//      {
//      }
//      else if (source.getName().equals(WSInputs.DRV_DPAD_Y.getName()))
//      {
//      }
   }

   private void printButtonState(Input source, int button)
   {
      if (((DigitalInput)source).getValue())
      {
         DriverStation.reportError("Driver button " + button + " pressed\n", false);
         System.out.println("Driver button " + button + " pressed");
      }
      else
      {
         DriverStation.reportError("Driver button " + button + " released\n", false);
         System.out.println("Driver button " + button + " released");
      }
   }

   private void printJoystickState(Input source)
   {
         System.out.println(source.getName() + " value: " + ((AnalogInput)source).getValue());
   }

   @Override
   public void init()
   {
  //    Core.getInputManager().getInput(WSInputs.LIMIT_SWITCH.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_1.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_2.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_3.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_4.getName()).addInputListener(this);
//      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_5.getName()).addInputListener(this);
//      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_6.getName()).addInputListener(this);
//      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_7.getName()).addInputListener(this);
//      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_8.getName()).addInputListener(this);
//      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_9.getName()).addInputListener(this);
//      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_10.getName()).addInputListener(this);
//      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_11.getName()).addInputListener(this);
//      Core.getInputManager().getInput(WSInputs.DRV_BUTTON_12.getName()).addInputListener(this);
//      Core.getInputManager().getInput(WSInputs.DRV_HEADING.getName()).addInputListener(this);
      Core.getInputManager().getInput(WSInputs.DRV_THROTTLE.getName()).addInputListener(this);
//      Core.getInputManager().getInput(WSInputs.DRV_LEFT_X.getName()).addInputListener(this);
//      Core.getInputManager().getInput(WSInputs.DRV_RIGHT_Y.getName()).addInputListener(this);
//      Core.getInputManager().getInput(WSInputs.DRV_DPAD_X.getName()).addInputListener(this);
//      Core.getInputManager().getInput(WSInputs.DRV_DPAD_Y.getName()).addInputListener(this);
      
//    Core.getInputManager().getInput(WSInputs.POT.getName()).addInputListener(this);
   }

   @Override
   public void selfTest()
   {
   }

   @Override
   public void update()
   {
      m_cycleCount++;
      
      DriverStation ds = DriverStation.getInstance();
      
//      switch (m_cycleCount / 50)
//      {
//         case 0:
//            ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR.getName())).setValue(-1.0);
//            DriverStation.reportError("Victor reverse\n", false);
//            break;
//         case 1:
//            ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR.getName())).setValue(1.0);
//            DriverStation.reportError("Victor forward\n", false);
//            break;
//         case 2:
//            ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_SP.getName())).setValue(-1.0);
//            DriverStation.reportError("Victor SP reverse\n", false);
//            break;
//         case 3:
//            ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_SP.getName())).setValue(1.0);
//            DriverStation.reportError("Victor SP forward\n", false);
//            break;
//         case 4:
//            ((WsRelay)Core.getOutputManager().getOutput(WSOutputs.SPIKE.getName())).setValue(WsRelay.RELAY_FORWARD);
//            DriverStation.reportError("Relay forward\n", false);
//            break;
//         case 5:
//            ((WsRelay)Core.getOutputManager().getOutput(WSOutputs.SPIKE.getName())).setValue(WsRelay.RELAY_REVERSE);
//            DriverStation.reportError("Relay reverse\n", false);
//            break;
//         case 6:
//            ((WsTalon)Core.getOutputManager().getOutput(WSOutputs.TALON.getName())).setValue(-1.0);
//            DriverStation.reportError("Talon reverse\n", false);
//            break;
//         case 7:
//            ((WsTalon)Core.getOutputManager().getOutput(WSOutputs.TALON.getName())).setValue(1.0);
//            DriverStation.reportError("Talon forward\n", false);
//            break;
//      }
//      
      
      double spOutput = (2.5 - potVal) / 2.5;

//      ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR.getName())).setValue(m_throttle);
//      ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_SP.getName())).setValue(spOutput);

//    ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR.getName())).setValue(b2 ? 1.0 : -1.0);
//    ((WsVictor)Core.getOutputManager().getOutput(WSOutputs.VICTOR_SP.getName())).setValue(b3 ? 1.0 : -1.0);
//    ((WsTalon)Core.getOutputManager().getOutput(WSOutputs.TALON.getName())).setValue(b4 ? 1.0 : -1.0);
      
//      if (m_limitSwitch)
//      {
//         ((WsSolenoid)Core.getOutputManager().getOutput(WSOutputs.SINGLE.getName())).setValue(true);
//         ((WsDoubleSolenoid)Core.getOutputManager().getOutput(WSOutputs.DOUBLE.getName())).setValue(WsDoubleSolenoidState.FORWARD.ordinal());
//         ((WsRelay)Core.getOutputManager().getOutput(WSOutputs.SPIKE.getName())).setValue(WsRelayState.RELAY_FORWARD.ordinal());
//         ((WsTalon)Core.getOutputManager().getOutput(WSOutputs.TALON.getName())).setValue(1.0);
//      }
//      else
//      {
//         ((WsSolenoid)Core.getOutputManager().getOutput(WSOutputs.SINGLE.getName())).setValue(false);
//         ((WsDoubleSolenoid)Core.getOutputManager().getOutput(WSOutputs.DOUBLE.getName())).setValue(WsDoubleSolenoidState.REVERSE.ordinal());
//         ((WsRelay)Core.getOutputManager().getOutput(WSOutputs.SPIKE.getName())).setValue(WsRelayState.RELAY_REVERSE.ordinal());
//         ((WsTalon)Core.getOutputManager().getOutput(WSOutputs.TALON.getName())).setValue(-1.0);
//      }
   }

   @Override
   public String getName()
   {
      return m_name;
   }

}
