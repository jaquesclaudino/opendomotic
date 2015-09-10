/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.opendomotic.model.entity;

/**
 *
 * @author jaques
 */
public enum DeviceType {
    
    SENSOR("Sensor"), VALUE("Value"), SWITCH("Switch"), SWITCH_CONFIRM("SwitchConfirm"), CUSTOM("Custom");
    
    private final String name;
    
    DeviceType(String name) {
        this.name = name;
    }
            
    @Override
    public String toString() {
        return name;
    }
    
}
