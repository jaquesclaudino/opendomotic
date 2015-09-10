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
public class ShutdownDevice extends SwitchShellDevice {

    @Override
    public String getWriteCommand() {
        return "sudo halt";
    }
    
}
