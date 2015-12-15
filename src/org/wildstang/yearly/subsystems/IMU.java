package org.wildstang.yearly.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.Input;
import org.wildstang.framework.io.inputs.DigitalInput;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.yearly.robot.WSInputs;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.TimerTask;

/**
 *
 * @author John
 */
public class IMU implements Subsystem
{
   // Sent states
   boolean dataReceived;
   private byte[] IMUData;
//   PosX1
//   PosX2
//   PosY1
//   PosY2
//   Heading1
//   Heading2

   private String m_name;
   private I2C i2c;
   private final static int I2CADDRESS = 0x6f;
   private java.util.Timer updater;
   

   public IMU(String name)
   {
      m_name = name;
      i2c = new I2C(Port.kOnboard, I2CADDRESS);
      IMUData = new byte[6];
      updater = new java.util.Timer();
   }

   @Override
   public void init()
   {
      // Nothing to do anymore, I'm bored.
   }

   @Override
   public void update()
   	{
	   i2c.read(I2CADDRESS, IMUData.length, IMUData);
	   Timer.delay(0.005); // Delay to prevent over polling
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
   
	// Start 10Hz polling
	public void start() {
		updater.scheduleAtFixedRate(new IMUUpdater(), 0, 100);
	}

	// Start polling for period in milliseconds
	public void start(int period) {
		updater.scheduleAtFixedRate(new IMUUpdater(), 0, period);
	}

	public void stop() {
		updater.cancel();
		updater = new java.util.Timer();
	}
   
	private class IMUUpdater extends TimerTask {
		public void run() {
			while (true) {
				update();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

@Override
public void inputUpdate(Input source) {
	// TODO Auto-generated method stub
	
}
}
