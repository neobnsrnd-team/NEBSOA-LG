package test.jmx;

public class Thermometer implements ThermometerMBean {


    //  Attributes

    public double getTemperature() {
        return getMyStaticDeviceInterface().getTemperature();
    }

    public double getMaximumTemperature() {
        return getMyStaticDeviceInterface().getMax();
    }

    public double getMinimumTemperature() {
        return getMyStaticDeviceInterface().getMin();
    }

    //  Operations

    public void resetMaxAndMin() {
        getMyStaticDeviceInterface().resetMaxAndMin();
    }
    
    public ThermometerDevice getMyStaticDeviceInterface() {
        return ThermometerDevice.getSingletonDevice();
    }
    

}
