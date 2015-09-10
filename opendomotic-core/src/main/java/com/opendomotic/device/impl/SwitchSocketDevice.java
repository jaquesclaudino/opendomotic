package com.opendomotic.device.impl;

/**
 *
 * @author Jaques Claudino
 * Aug 6, 2015
 */
public class SwitchSocketDevice extends SocketDevice<Integer> {

    private int value = 0;
    private String writeCommandOff;
    
    @Override
    public void setValue(Integer value) throws Exception {
        this.value = value;
        super.setValue(value); //can be writeCommand[On] or writeCommandOff
    }

    @Override
    protected Integer convertValue(int[] buffer) {
        return Integer.parseInt(bufferToString(buffer));
    }
    
    @Override
    public String getWriteCommand() {
        if (value == 1) {
            return super.getWriteCommand(); //writeCommand[On]
        } else {
            return writeCommandOff;
        }
    }   
    
    public void setWriteCommandOn(String writeCommandOn) {
        this.setWriteCommand(writeCommandOn);
    }
    
    public void setWriteCommandOff(String writeCommandOff) {
        this.writeCommandOff = writeCommandOff;
    }
    
}
