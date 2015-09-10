package com.opendomotic.device.pi2.gpio;

import com.opendomotic.device.Device;
import com.opendomotic.device.impl.HttpDevice;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaques
 */
public class HttpInputPin implements Device<Integer>, GpioPinListenerDigital {

    private static final Logger LOG = Logger.getLogger(HttpInputPin.class.getName());

    private Integer value = 0;   
    private boolean inverse = false;
    private final HttpDevice httpDevice = new HttpDevice();
    private long millisHandler;
    
    @Override
    public Integer getValue() {
        boolean isHigh = inverse ? value.equals(0) : value.equals(1);
        return isHigh ? 1 : 0;
    }

    @Override
    public void setValue(Integer value) throws Exception { 
        this.value = value;
        httpDevice.setValue(value);
    }    
   
    public void setGpio(int gpio) {
        Gpio.getInstance().getInput(gpio, this);
    }

    public void setInverse(boolean inverse) {
        this.inverse = inverse;
        this.value = inverse ? 1 : 0;
    }

    public void setHost(String host) {
        httpDevice.setHost(host);
    }

    public void setPath(String path) {
        httpDevice.setPath(path);
    }
    
    public void setUser(String user) {
        httpDevice.setUser(user);
    }
    
    public void setPassword(String password) {
        httpDevice.setPassword(password);
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpdsce) {
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
