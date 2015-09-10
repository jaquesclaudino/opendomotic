/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.device.impl;

import com.opendomotic.device.Device;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jaques
 */
public class Clock implements Device<String> {

    private String format;
    
    @Override
    public String getValue() {
        if (format != null) {
            return new SimpleDateFormat(format).format(new Date());
        } else {
            return new Date().toString();
        }        
    }

    @Override
    public void setValue(String o) {
    }

    public void setFormat(String format) {
        this.format = format;
    }
    
}
