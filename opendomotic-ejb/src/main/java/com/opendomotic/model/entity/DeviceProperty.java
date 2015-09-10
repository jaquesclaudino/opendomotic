/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author jaques
 */
@Entity
public class DeviceProperty extends AbstractEntityName {
    
    @ManyToOne
    private DeviceConfig deviceConfig;
    
    private String value;

    public DeviceProperty() {
    }
    
    public DeviceProperty(DeviceConfig deviceConfig, String name, String value) {
        this.deviceConfig = deviceConfig;
        setName(name);
        this.value = value;
    }
    
    public DeviceConfig getDeviceConfig() {
        return deviceConfig;
    }

    public void setDeviceConfig(DeviceConfig deviceConfig) {
        this.deviceConfig = deviceConfig;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public boolean isEmpty() {
        return getName() == null ||
                getValue() == null ||
                getName().trim().isEmpty() ||
                getValue().trim().isEmpty();
    }

    @Override
    public String toString() {
        return getName() + "=" + value;
    }
    
}
