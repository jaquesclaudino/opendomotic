package com.opendomotic.device.pi.i2c;

/**
 *
 * @author Jaques Claudino
 */
public class DoubleI2CDevice extends I2CDevice<Double> {
 
    private double factor = 1;
    
    @Override
    public Double getValue() throws Exception {
        Integer value = super.getValueInt();
        if (value != null) {
            return ((double) value) / factor;
        }
        return null;        
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
    
}
