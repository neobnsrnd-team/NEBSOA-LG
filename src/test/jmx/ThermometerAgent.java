package test.jmx;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

  public class ThermometerAgent {

  private MBeanServer server = null;

     public ThermometerAgent() {

        server = MBeanServerFactory.createMBeanServer();
 
        Thermometer tBean = new Thermometer();
        ObjectName tBeanName = null;

        try {
           tBeanName = new ObjectName(
               "ThermometerAgent:type=Thermometer");
           server.registerMBean(tBean, tBeanName);
        } catch(Exception e) {
           e.printStackTrace();
        }
     }

     public static void main(String argv[]) {
        ThermometerAgent agent = new ThermometerAgent();
        System.out.println(
                "Agent is ready... Press Enter to close");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
 
     }
  }
