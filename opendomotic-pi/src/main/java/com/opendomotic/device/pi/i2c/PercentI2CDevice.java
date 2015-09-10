package com.opendomotic.device.pi.i2c;

import com.opendomotic.device.util.Percent;

/**
 *
 * @author Jaques Claudino
 */
public class PercentI2CDevice extends DoubleI2CDevice {
    
    private final Percent percent = new Percent();

    @Override
    public Double getValue() throws Exception {
        Double value = super.getValue();
        if (value != null) {
            return percent.getPercent(value);
        }
        return null;
    }
    
    public void setMin(Double min) {
        percent.setMin(min);
    }
    
    public void setMax(Double max) {
        percent.setMax(max);
    }
    
    public void setAuto(boolean auto) {
        percent.setAuto(auto);
    }
    
}
