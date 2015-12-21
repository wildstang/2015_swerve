package org.wildstang.yearly.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.Input;
import org.wildstang.framework.io.inputs.DigitalInput;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.crio.outputs.WsI2COutput;
import org.wildstang.yearly.robot.SwerveInputs;
import org.wildstang.yearly.robot.WSOutputs;

import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 */
public class LED implements Subsystem
{

   // Sent states
   boolean autoDataSent = false;
   boolean m_newDataAvailable = false;
   boolean disableDataSent = false;

   private String m_name;

   WsI2COutput m_ledOutput;

   boolean m_antiTurbo;
   boolean m_turbo;
   boolean m_normal;

   /*
    * | Function | Cmd | PL 1 | PL 2 |
    * --------------------------------------------------------- | Shoot | 0x05 |
    * 0x13 | 0x14 | | Climb | 0x06 | 0x11 | 0x12 | | Autonomous | 0x02 | 0x11 |
    * 0x12 | | Red Alliance | 0x04 | 0x52 | 0x01 | | Blue Alliance | 0x04 | 0x47
    * | 0x01 |
    * 
    * Send sequence once, no spamming the Arduino.
    */

   // Reused commands from year to year
   LedCmd autoCmd = new LedCmd(0x02, 0x11, 0x12);
   LedCmd redCmd = new LedCmd(0x04, 0x52, 0x01);
   LedCmd blueCmd = new LedCmd(0x04, 0x47, 0x01);

   // New commands each year
   LedCmd turboCmd = new LedCmd(0x05, 0x13, 0x14);
   LedCmd antiturboCmd = new LedCmd(0x06, 0x11, 0x12);
   LedCmd normalCmd = new LedCmd(0x07, 0x11, 0x12);

   public LED()
   {
      m_name = "LED";
   }

   @Override
   public void init()
   {
      autoDataSent = false;
      disableDataSent = false;

      Core.getInputManager().getInput(SwerveInputs.ANTI_TURBO.getName()).addInputListener(this);
      Core.getInputManager().getInput(SwerveInputs.TURBO.getName()).addInputListener(this);

      m_ledOutput = (WsI2COutput) Core.getOutputManager().getOutput(WSOutputs.LED.getName());
   }

   @Override
   public void update()
   {
      // Get all inputs relevant to the LEDs
      boolean isRobotEnabled = DriverStation.getInstance().isEnabled();
      boolean isRobotTeleop = DriverStation.getInstance().isOperatorControl();
      boolean isRobotAuton = DriverStation.getInstance().isAutonomous();
      DriverStation.Alliance alliance = DriverStation.getInstance().getAlliance();

      m_normal = !(m_antiTurbo || m_turbo);
      
//      if (isRobotEnabled)
//      {
//         if (isRobotTeleop)
//         {
            if (m_newDataAvailable)
            {
               if (m_antiTurbo)
               {
                  m_ledOutput.setValue(antiturboCmd.getBytes());
               }
               else if (m_turbo)
               {
                  m_ledOutput.setValue(turboCmd.getBytes());
               }
               else if (m_normal)
               {
                  m_ledOutput.setValue(normalCmd.getBytes());
               }
               m_newDataAvailable = false;
            }
//         }
//         else if (isRobotAuton)
//         {
//            // --------------------------------------------------------------
//            // Handle Autonomous signalling here
//            // --------------------------------------------------------------
//            // One send and one send only.
//            // Don't take time in auto sending LED cmds.
//            if (!autoDataSent)
//            {
//               m_ledOutput.setValue(autoCmd.getBytes());
//               autoDataSent = true;
//            }
//         }
//      }
//      else
//      {
//         // ------------------------------------------------------------------
//         // Handle Disabled signalling here
//         // ------------------------------------------------------------------
//         switch (alliance)
//         {
//            case Red:
//            {
//               if (!disableDataSent)
//               {
//                  m_ledOutput.setValue(redCmd.getBytes());
//                  disableDataSent = true;
//               }
//            }
//               break;
//
//            case Blue:
//            {
//               if (!disableDataSent)
//               {
//                  m_ledOutput.setValue(blueCmd.getBytes());
//                  disableDataSent = true;
//               }
//            }
//               break;
//            default:
//            {
//               disableDataSent = false;
//            }
//               break;
//         }
//      }
   }

   @Override
   public void inputUpdate(Input source)
   {
      if (source.getName().equals(SwerveInputs.ANTI_TURBO.getName()))
      {
         m_antiTurbo = ((DigitalInput) source).getValue();
      }
      else if (source.getName().equals(SwerveInputs.TURBO.getName()))
      {
         m_turbo = ((DigitalInput) source).getValue();
      }
      
      m_newDataAvailable = true;
   }

   @Override
   public void selfTest()
   {
   }

   @Override
   public String getName()
   {
      return m_name;
   }

   public static class LedCmd
   {

      byte[] dataBytes = new byte[5];

      public LedCmd(int command, int payloadByteOne, int payloadByteTwo)
      {

         dataBytes[0] = (byte) command;
         dataBytes[1] = (byte) payloadByteOne;
         dataBytes[2] = (byte) payloadByteTwo;
         // Extremely fast and cheap data confirmation algorithm
         dataBytes[3] = (byte) (~dataBytes[1]);
         dataBytes[4] = (byte) (~dataBytes[2]);
      }

      byte[] getBytes()
      {
         return dataBytes;
      }
   }
}
