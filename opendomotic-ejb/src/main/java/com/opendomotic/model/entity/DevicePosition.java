/*
 * To change this template, choose Tools | Templates
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
public class DevicePosition extends AbstractEntityId {
    
    @ManyToOne
    private Environment environment;
    
    @ManyToOne
    private DeviceConfig deviceConfig;
    
    private Integer x;
    private Integer y;

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public DeviceConfig getDeviceConfig() {
        return deviceConfig;
    }

    public void setDeviceConfig(DeviceConfig deviceConfig) {
        this.deviceConfig = deviceConfig;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
    
}
