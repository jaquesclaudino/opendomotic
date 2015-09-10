package com.opendomotic.device.pi.sh;

/**
 *
 * @author Jaques Claudino
 */
public class DoubleShellDevice extends ShellDevice<Double> {
    
    @Override
    public Double getValue() throws Exception {
        return Double.parseDouble(getRunTimeExecLine(getReadCommand())); 
    }
    
}
