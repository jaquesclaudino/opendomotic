/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.opendomotic.device.pi.sh;

/**
 *
 * @author Jaques Claudino
 */
public class SwitchShellDevice extends ShellDevice<Integer> {

    private int value = 0;
    private String writeCommandOff;
    
    @Override
    public Integer getValue() throws Exception {
        if (getReadCommand() != null) {
            value = Integer.parseInt(getRunTimeExecLine(getReadCommand()));
        }
        return value;
    }
    
    @Override
    public void setValue(Integer value) throws Exception {
        this.value = value;
        super.setValue(value); //can be writeCommand[On] or writeCommandOff
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
