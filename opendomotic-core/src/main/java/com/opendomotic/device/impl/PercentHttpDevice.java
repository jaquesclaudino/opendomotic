package com.opendomotic.device.impl;

import com.opendomotic.device.util.Percent;

/**
 *
 * @author Jaques Claudino
 */
public class PercentHttpDevice extends DoubleHttpDevice {   

    private final Percent percent = new Percent();

    @Override
    public Double getValue() throws Exception {
        Double value = super.getValue();
        return percent.getPercent(value);
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
