package com.opendomotic.device.pi2.gpio;

import com.opendomotic.device.Device;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

/**
 *
 * @author jaques
 */
public class OutputPin implements Device<Integer> {

    private Integer gpio = 0;    
    private Integer initialState = 0;
    private boolean inverse = false; //inverse quando 0=on e 1=off
        
    @Override
    public Integer getValue() { 
        boolean isHigh = inverse ? getPin().isLow() : getPin().isHigh();        
        return isHigh ? 1 : 0;
    }

    @Override
    public void setValue(Integer value) {
        getPin().setState(value.equals(inverse ? 0 : 1));
    }    
    
    private GpioPinDigitalOutput getPin() {
        return Gpio.getInstance().getOutput(gpio, PinState.getState(initialState));
    }
    
    public void setGpio(Integer gpio) {
        this.gpio = gpio;
    }

    public void setInitialState(Integer initialState) {
        this.initialState = initialState;
    }

    public void setInverse(boolean inverse) {
        this.inverse = inverse;
    }
        
}
