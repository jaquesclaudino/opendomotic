/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.device.util;

/**
 *
 * @author Jaques Claudino
 */
public class Percent {
 
    public static final double DEFAULT_MIN = 0;
    public static final double DEFAULT_MAX = 1023;
    
    private Double min;
    private Double max;
    private boolean auto;
    
    public double getPercent(double value) {
        checkMinMax(value);
        
        double range = max - min;
        if (range == 0)
            return 0;
        
        double percent = (value - min) * 100 / range;       
        
        //check range:
        return Math.max(0, Math.min(100, percent));
    }
    
    private void checkMinMax(double value) {
        if (auto) {
            if (min == null || value < min) {
                min = value;
            }
            if (max == null || value > max) {
                max = value;
            }
        } else if (min == null || max == null) {
            min = DEFAULT_MIN;
            max = DEFAULT_MAX;
        }
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }
    
}
