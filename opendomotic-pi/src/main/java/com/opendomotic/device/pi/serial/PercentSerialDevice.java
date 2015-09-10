package com.opendomotic.device.pi.serial;

import com.opendomotic.device.util.Percent;

/**
 *
 * @author Jaques Claudino
 */
public class PercentSerialDevice extends SerialDevice {  

    private final Percent percent = new Percent();
    
    @Override
    public Integer getValue() throws Exception {
        int value = (Integer) super.getValue(); 
        return (int) percent.getPercent(value);
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
