package com.opendomotic.device.pi2.gpio;

import com.opendomotic.device.Device;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaques
 */
public class ToggleInputPin implements Device<Integer>, GpioPinListenerDigital {

    private static final Logger LOG = Logger.getLogger(ToggleInputPin.class.getName());

    private Integer value = 0;   
    private boolean inverse = false;
    private long millisHandler;
    private String gpioOutput;
    
    @Override
    public Integer getValue() {
        boolean isHigh = inverse ? value.equals(0) : value.equals(1);
        return isHigh ? 1 : 0;
    }

    @Override
    public void setValue(Integer value) throws Exception { 
        this.value = value;
        
        for (String item : gpioOutput.split(";")) {
            Gpio.getInstance().getOutput(Integer.parseInt(item), PinState.LOW).toggle();
        }
    }    
   
    public void setGpioInput(int gpioInput) {
        Gpio.getInstance().getInput(gpioInput, this);
    }
    
    public void setGpioOutput(String gpioOutput) {
        this.gpioOutput = gpioOutput;
    }

    public void setInverse(boolean inverse) {
        this.inverse = inverse;
        this.value = inverse ? 1 : 0;
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        try {
            if (System.currentTimeMillis()-millisHandler > 1000) {
                millisHandler = System.currentTimeMillis();
                //toggle:           
                setValue(value.equals(1) ? 0 : 1);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }        
    }
    
}
