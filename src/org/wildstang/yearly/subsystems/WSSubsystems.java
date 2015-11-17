package org.wildstang.yearly.subsystems;

import org.wildstang.framework.core.Subsystems;


public enum WSSubsystems implements Subsystems
{
   DRIVE_BASE("Drive base", DriveBase.class),
//   HARDWARE_TEST("Hardware test", HardwareTest.class),
//   LED("LED", LED.class),
   MONITOR("Monitor", Monitor.class);

   private String m_name;
   private Class m_class;

   WSSubsystems(String p_name, Class p_class)
   {
      m_name = p_name;
      m_class = p_class;
   }

   @Override
   public String getName()
   {
      return m_name;
   }

   @Override
   public Class getSubsystemClass()
   {
      return m_class;
   }

}