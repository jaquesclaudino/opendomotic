package com.opendomotic.device.pi2.gpio;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jaques Claudino
 */
public class Gpio {

    public static Gpio instance;
    private final Map<Integer,GpioPinDigitalInput> mapInput = new HashMap<>();    
    private final Map<Integer,GpioPinDigitalOutput> mapOutput = new HashMap<>();    
    
    private Gpio() {        
    }
    
    public synchronized static Gpio getInstance() {
        if (instance == null) {
            instance = new Gpio();
        }
        return instance;
    }
    
    public GpioPinDigitalInput getInput(int gpio, GpioPinListenerDigital listener) {
        GpioPinDigitalInput pin = mapInput.get(gpio);
        if (pin == null) {
            pin = GpioFactory.getInstance().provisionDigitalInputPin(RaspiPin.getPinByName("GPIO "+gpio), PinPullResistance.PULL_UP);
            if (listener != null) {
                pin.addListener(listener);
            }
            mapInput.put(gpio, pin);
        }
        return pin;
    }
    
    public GpioPinDigitalOutput getOutput(int gpio, PinState pinState) {
        GpioPinDigitalOutput pin = mapOutput.get(gpio);
        if (pin == null) {
            pin = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.getPinByName("GPIO "+gpio), pinState);            
            mapOutput.put(gpio, pin);
        }
        return pin;
    }
    
}
