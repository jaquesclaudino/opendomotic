/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.device.pi.serial;

/**
 *
 * @author Jaques Claudino
 */
public class DoubleSerialDevice extends SerialDevice<Double> {

    private double factor = 1;
    
    @Override
    public Double getValue() throws Exception {
        double value = super.getValueInt();
        return value / factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
    
}