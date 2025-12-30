package test.jmx;

public interface ThermometerMBean {

    //  Attributes
    public double getTemperature();
    public double getMaximumTemperature();
    public double getMinimumTemperature();

    //  Operations
    public void resetMaxAndMin();  

}
