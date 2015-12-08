package org.wildstang.yearly.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.Input;
import org.wildstang.framework.io.inputs.DigitalInput;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.yearly.robot.WSInputs;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

/**
 *
 * @author John
 */
public class IMU implements Subsystem
{

   MessageHandler messageSender;
   // Sent states
   boolean dataReceived;
   static byte[] IMUData = new byte[6];
//   PosX1
//   PosX2
//   PosY1
//   PosY2
//   Heading1
//   Heading2

   private String m_name;

   public static class LedCmd
   {

      byte[] dataBytes = new byte[5];

      public LedCmd(int command, int payloadByteOne, int payloadByteTwo)
      {

         dataBytes[0] = (byte) command;
         dataBytes[1] = (byte) payloadByteOne;
         dataBytes[2] = (byte) payloadByteTwo;
         dataBytes[3] = 0;
         dataBytes[4] = 0;
      }

      byte[] getBytes()
      {
         return dataBytes;
      }
   }

   public IMU(String name)
   {
      m_name = name;
      // Fire up the message sender thread.
      Thread t = new Thread(messageSender = new MessageHandler());
      // This is safe because there is only one instance of the subsystem in
      // the subsystem container.
      t.start();

   }

   @Override
   public void init()
   {
      // Nothing to do anymore, I'm bored.
      
      dataReceived = false;
   }

   @Override
   public void update()
   {

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

   private boolean sendData(LedCmd ledCmd)
   {
      byte[] dataBytes = ledCmd.getBytes();

      synchronized (messageSender)
      {
         messageSender.setSendData(dataBytes, dataBytes.length);
         messageSender.notify();
      }

      return true;
   }

   private static class MessageHandler implements Runnable
   {
      // Designed to only have one single threaded controller. (LED)
      // Offload to a thread avoid blocking main thread with LED sends.

      static byte[] rcvBytes;
      byte[] sendData;
      int sendSize = 0;
      I2C i2c;
      boolean running = true;
      boolean dataToSend = false;

      public MessageHandler()
      {
         // Get ourselves an i2c instance to send out some data.
         i2c = new I2C(Port.kOnboard, 0x6F);
      }

      @Override
      public void run()
      {
         while (running)
         {
            synchronized (this)
            {
               try
               {
                  // blocking sleep until someone calls notify.
                  this.wait();
                  // Need at least 5 bytes and someone has to have called
                  // setSendData.
                  i2c.transaction(sendData, 0, IMUData, IMUData.length);
               }
               catch (InterruptedException e)
               {
               }
            }
         }
      }
      
      public void setSendData(byte[] data, int size)
      {
         sendData = data;
         sendSize = size;
         dataToSend = true;
      }
      
      public void stop()
      {
         running = false;
      }
   }

@Override
public void inputUpdate(Input source) {
	// TODO Auto-generated method stub
	
}
}
