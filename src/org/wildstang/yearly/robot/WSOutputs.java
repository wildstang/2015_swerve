package org.wildstang.yearly.robot;

import org.wildstang.framework.core.Outputs;
import org.wildstang.framework.hardware.OutputConfig;
import org.wildstang.framework.io.outputs.OutputType;
import org.wildstang.hardware.crio.outputs.WSOutputType;
import org.wildstang.hardware.crio.outputs.WsDoubleSolenoidState;
import org.wildstang.hardware.crio.outputs.WsRelayState;
import org.wildstang.hardware.crio.outputs.config.WsDoubleSolenoidConfig;
import org.wildstang.hardware.crio.outputs.config.WsRelayConfig;
import org.wildstang.hardware.crio.outputs.config.WsSolenoidConfig;
import org.wildstang.hardware.crio.outputs.config.WsTalonConfig;
import org.wildstang.hardware.crio.outputs.config.WsVictorConfig;

public enum WSOutputs implements Outputs
{
   VICTOR_URD("Victor SP1",    WSOutputType.VICTOR,    new WsVictorConfig(1,  0.0), true),
//   VICTOR_ULD("Talon",            WSOutputType.TALON,     new WsTalonConfig(1,  0.0), true),
   VICTOR_ULD("Victor SP2",    WSOutputType.VICTOR,    new WsVictorConfig(0,  0.0), true),
   VICTOR_URR("Victor SP3",    WSOutputType.VICTOR,    new WsVictorConfig(5,  0.0), true),
   VICTOR_ULR("Victor SP4",    WSOutputType.VICTOR,    new WsVictorConfig(4,  0.0), true),
   VICTOR_LRD("Victor SP5",    WSOutputType.VICTOR,    new WsVictorConfig(3,  0.0), true),
   VICTOR_LLD("Victor SP6",    WSOutputType.VICTOR,    new WsVictorConfig(2,  0.0), true),
   VICTOR_LRR("Victor SP7",    WSOutputType.VICTOR,    new WsVictorConfig(7,  0.0), true),
   VICTOR_LLR("Victor SP8",    WSOutputType.VICTOR,    new WsVictorConfig(6,  0.0), true),

   // Solenoids
   DOUBLE("Double solenoid", WSOutputType.SOLENOID_DOUBLE, new WsDoubleSolenoidConfig(1, 0, 1, WsDoubleSolenoidState.FORWARD), true),
   SINGLE("Single solenoid", WSOutputType.SOLENOID_SINGLE, new WsSolenoidConfig(1, 2, false), true);

   private String m_name;
   private OutputType m_type;
   private OutputConfig m_config;
   private boolean m_trackingState;

   WSOutputs(String p_name, OutputType p_type, OutputConfig p_config, boolean p_trackingState)
   {
      m_name = p_name;
      m_type = p_type;
      m_config = p_config;
      m_trackingState = p_trackingState;
   }
   
   
   @Override
   public String getName()
   {
      return m_name;
   }
   
   @Override
   public OutputType getType()
   {
      return m_type;
   }
   
   public OutputConfig getConfig()
   {
      return m_config;
   }

   public boolean isTrackingState()
   {
      return m_trackingState;
   }

}
