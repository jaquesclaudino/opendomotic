/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 *
 * @author jaques
 */
@Entity
public class Environment extends AbstractEntityFile {
    
    @OneToMany(mappedBy = "environment", fetch = FetchType.EAGER)
    private List<DevicePosition> listDevicePosition;

    public List<DevicePosition> getListDevicePosition() {
        return listDevicePosition;
    }

    public void setListDevicePosition(List<DevicePosition> listDevicePosition) {
        this.listDevicePosition = listDevicePosition;
    }
    
}
