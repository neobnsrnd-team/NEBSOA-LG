package test.jmx;

public class ThermometerDevice {
        
    private static ThermometerDevice device;

    private double temperature = 56.2;
    private double minimum = 28.1;
    private double maximum = 67.6;
    
    public static ThermometerDevice getSingletonDevice() {
        
        if (device == null)
            device = new ThermometerDevice();

        return device;
    }
    
    public double getTemperature() {
        return temperature;
    }

    public double getMax() {
        return maximum;
    }

    public double getMin() {
        return minimum;
    }

    //  Operations

    public void resetMaxAndMin() {
        System.out.println("Maximum and minimum reset!");
    }
}
