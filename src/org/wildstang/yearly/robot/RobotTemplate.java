/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.wildstang.yearly.robot;

import org.wildstang.framework.auto.AutoManager;
import org.wildstang.framework.core.Core;
import org.wildstang.framework.logger.StateLogger;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.framework.timer.ProfilingTimer;
import org.wildstang.hardware.crio.RoboRIOInputFactory;
import org.wildstang.hardware.crio.RoboRIOOutputFactory;
import org.wildstang.yearly.subsystems.DriveBase;
import org.wildstang.yearly.subsystems.HardwareTest;
import org.wildstang.yearly.subsystems.Monitor;
import org.wildstang.yearly.subsystems.WSSubsystems;

import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

//import edu.wpi.first.wpilibj.Watchdog;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot
{

   private static long lastCycleTime = 0;
   private static int session;
   private static Image frame;
   private StateLogger m_stateLogger = null;
   private Core m_core = null;
   private static Logger s_log = Logger.getLogger(RobotTemplate.class.getName());

   static boolean teleopPerodicCalled = false;

   private void startloggingState()
   {
      // Add the monitored inputs
      // TODO: Can we do this better?
      
      Core.getStateTracker().addIOInfo("Current 0", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 1", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 2", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 3", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 4", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 5", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 6", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 7", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 8", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 9", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 10", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 11", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 12", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 13", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 14", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Current 15", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Total Current", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Voltage", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Temperature", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Enabled", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Teleop", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Auto", "Monitor", "Input", null);
      Core.getStateTracker().addIOInfo("Memory in use", "Monitor", "Input", null);

      Core.getStateTracker().addIOInfo("Encoder offset", "Swerve", "Input", null);
      Core.getStateTracker().addIOInfo("Desired angle", "Swerve", "Input", null);
      Core.getStateTracker().addIOInfo("Target encoder angle", "Swerve", "Input", null);
      Core.getStateTracker().addIOInfo("Magnitude", "Swerve", "Input", null);
      Core.getStateTracker().addIOInfo("Rotation magnitude", "Swerve", "Input", null);
      Core.getStateTracker().addIOInfo("angleDistance", "Swerve", "Input", null);
      Core.getStateTracker().addIOInfo("Encoder angle", "Swerve", "Input", null);
      
      Writer outputWriter = null;
      
      outputWriter = getFileWriter();
//      outputWriter = getNetworkWriter("10.1.11.12", 17654);

      m_stateLogger.setWriter(outputWriter);
      m_stateLogger.start();
      Thread t = new Thread(m_stateLogger);
      t.start();
   }

   private Writer getNetworkWriter(String ipAddress, int port)
   {
      BufferedWriter output = null;
      
      try
      {
         Socket socket = new Socket(ipAddress, port);
         output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      }
      catch (UnknownHostException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      return output;
   }
   
   private FileWriter getFileWriter()
   {
      FileWriter output = null;
      
      try
      {
         File outputFile = new File("/home/lvuser/log.txt");
         if (outputFile.exists())
         {
            outputFile.delete();
         }
         outputFile.createNewFile();
         output = new FileWriter(outputFile);
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return output;
   }

   /**
    * This function is run when the robot is first started up and should be used
    * for any initialization code.
    */
   public void robotInit()
   {
      startupTimer.startTimingSection();

      m_core = new Core(RoboRIOInputFactory.class, RoboRIOOutputFactory.class);
      m_stateLogger = new StateLogger(Core.getStateTracker());
      startloggingState();

      // Load the config
      loadConfig();

      // Create application systems
      m_core.createInputs(WSInputs.values());
      m_core.createOutputs(WSOutputs.values());

      // TODO
      // 1. Add subsystems
      m_core.createSubsystems(WSSubsystems.values());

      // 2. Add Auto programs

      // OLD CODE
      // AutoManager.getInstance();

      // sets up the USB camera for streaming to the smartdashboard
      // this is unneeded if using an Ethernet camera (or no camera)
      /*
       * try { frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB,
       * 0);
       * 
       * // the camera name (ex "cam0") can be found through the roborio web
       * interface session = NIVision.IMAQdxOpenCamera("cam0",
       * NIVision.IMAQdxCameraControlMode.CameraControlModeController);
       * CameraServer.getInstance().setQuality(1);
       * NIVision.IMAQdxConfigureGrab(session);
       * 
       * NIVision.IMAQdxStartAcquisition(session); } catch(Exception e){}
       */

      s_log.logp(Level.ALL, this.getClass().getName(), "robotInit", "Startup Completed");
      startupTimer.endTimingSection();

   }

   private void loadConfig()
   {
      // TODO: Get filename somehow
      File configFile = new File("/ws_config.txt");

      BufferedReader reader = null;

      try
      {
         reader = new BufferedReader(new FileReader(configFile));
         Core.getConfigManager().loadConfig(reader);

      }
      catch (FileNotFoundException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      if (reader != null)
      {
         try
         {
            reader.close();
         }
         catch (IOException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   ProfilingTimer durationTimer = new ProfilingTimer("Periodic method duration", 50);
   ProfilingTimer periodTimer = new ProfilingTimer("Periodic method period", 50);
   ProfilingTimer startupTimer = new ProfilingTimer("Startup duration", 1);
   ProfilingTimer initTimer = new ProfilingTimer("Init duration", 1);

   public void disabledInit()
   {
      initTimer.startTimingSection();
      AutoManager.getInstance().clear();

      loadConfig();

      Core.getSubsystemManager().init();

      initTimer.endTimingSection();
      s_log.logp(Level.ALL, this.getClass().getName(), "disabledInit", "Disabled Init Complete");

   }

   public void disabledPeriodic()
   {
      // If we are finished with teleop, finish and close the log file
      if (teleopPerodicCalled)
      {
         m_stateLogger.stop();
      }
   }

   public void autonomousInit()
   {
      Core.getSubsystemManager().init();
      
      m_core.setAutoManager(AutoManager.getInstance());
      AutoManager.getInstance().startCurrentProgram();
   }

   /**
    * This function is called periodically during autonomous
    */
   public void autonomousPeriodic()
   {
      // Update all inputs, outputs and subsystems
      m_core.executeUpdate();

   }

   /**
    * This function is called periodically during operator control
    */
   public void teleopInit()
   {
      // Remove the AutoManager from the Core
      m_core.setAutoManager(null);
      
      Core.getSubsystemManager().init();

      periodTimer.startTimingSection();
   }

   public void teleopPeriodic()
   {
      teleopPerodicCalled = true;

      long cycleStartTime = System.currentTimeMillis();
//      System.out.println("Cycle separation time: " + (cycleStartTime - lastCycleTime));

      // Update all inputs, outputs and subsystems
      m_core.executeUpdate();

      /*
       * try { NIVision.IMAQdxGrab(session, frame, 1);
       * CameraServer.getInstance().setImage(frame); } catch(Exception e){}
       */

      long cycleEndTime = System.currentTimeMillis();
      long cycleLength = cycleEndTime - cycleStartTime;
//      System.out.println("Cycle time: " + cycleLength);
      lastCycleTime = cycleEndTime;
      // Watchdog.getInstance().feed();
   }

   /**
    * This function is called periodically during test mode
    */
   public void testPeriodic()
   {
      // Watchdog.getInstance().feed();
   }
}
