package com.opendomotic.device.pi;

import com.opendomotic.device.Device;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 *
 * @author jaques
 */
public class DigitalPin implements Device<Integer> {

    private Integer gpio = 0;    
    private Integer state = 0;
    private Integer valueOn = 1;
    private Integer valueOff = 0;
    private GpioPinDigitalOutput digitalPin;
        
    @Override
    public Integer getValue() { 
        return getDigitalPin().isHigh() ? valueOn : valueOff;
    }

    @Override
    public void setValue(Integer value) {
        getDigitalPin().setState(value.equals(valueOn));
    }    
    
    private GpioPinDigitalOutput getDigitalPin() {
        if (digitalPin == null) {
            digitalPin = createDigitalPin();
        }        
        return digitalPin;
    }
    
    private GpioPinDigitalOutput createDigitalPin() {
        Pin raspiPin;
        switch (gpio) {
            case 1:  raspiPin = RaspiPin.GPIO_01; break;
            case 2:  raspiPin = RaspiPin.GPIO_02; break;
            case 3:  raspiPin = RaspiPin.GPIO_03; break;
            case 4:  raspiPin = RaspiPin.GPIO_04; break;
            case 5:  raspiPin = RaspiPin.GPIO_05; break;
            case 6:  raspiPin = RaspiPin.GPIO_06; break;
            case 7:  raspiPin = RaspiPin.GPIO_07; break;
            case 8:  raspiPin = RaspiPin.GPIO_08; break;
            case 9:  raspiPin = RaspiPin.GPIO_09; break;
            case 10:  raspiPin = RaspiPin.GPIO_10; break;
            case 11:  raspiPin = RaspiPin.GPIO_11; break;
            case 12:  raspiPin = RaspiPin.GPIO_12; break;
            case 13:  raspiPin = RaspiPin.GPIO_13; break;
            case 14:  raspiPin = RaspiPin.GPIO_14; break;
            case 15:  raspiPin = RaspiPin.GPIO_15; break;
            case 16:  raspiPin = RaspiPin.GPIO_16; break;
            case 17:  raspiPin = RaspiPin.GPIO_17; break;
            case 18:  raspiPin = RaspiPin.GPIO_18; break;
            case 19:  raspiPin = RaspiPin.GPIO_19; break;
            case 20:  raspiPin = RaspiPin.GPIO_20; break;
            default: raspiPin = RaspiPin.GPIO_00; break;
        }
        return GpioFactory.getInstance().provisionDigitalOutputPin(raspiPin, PinState.getState(state));
    }
    
    public void setGpio(Integer gpio) {
        this.gpio = gpio;
        
        if (digitalPin != null) {
            digitalPin = createDigitalPin();
        }
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setValueOn(Integer valueOn) {
        this.valueOn = valueOn;
    }

    public void setValueOff(Integer valueOff) {
        this.valueOff = valueOff;
    }
    
}
