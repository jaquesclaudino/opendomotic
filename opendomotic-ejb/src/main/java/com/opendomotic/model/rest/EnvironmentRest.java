/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.rest;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jaques
 */
@XmlRootElement
public class EnvironmentRest {
    
    private String fileName;
    private List<DevicePositionRest> listDevicePositionRest;    
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<DevicePositionRest> getListDevicePositionRest() {
        return listDevicePositionRest;
    }

    public void setListDevicePositionRest(List<DevicePositionRest> listDevicePositionRest) {
        this.listDevicePositionRest = listDevicePositionRest;
    }
    
}
