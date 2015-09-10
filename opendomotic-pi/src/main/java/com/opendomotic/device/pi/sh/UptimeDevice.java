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
public class UptimeDevice extends ShellDevice {

    private boolean full = false;
    
    @Override
    public String getReadCommand() {
        return full ? "uptime" : "uptime | grep -ohe 'up .*' | sed 's/,//g' | awk '{ print $2 \" \" $3 \" \" $4 }'";
        
        //for hour and users: "uptime | grep -ohe 'up .*' | sed 's/,//g' | awk '{ print $2\" \"$3 }'"
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }
    
}
