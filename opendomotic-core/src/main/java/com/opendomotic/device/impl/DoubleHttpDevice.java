/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.device.impl;

/**
 *
 * @author Jaques Claudino
 */
public class DoubleHttpDevice extends HttpDevice<Double> {

    @Override
    public Double getValue() throws Exception {
        return Double.parseDouble(makeRequest(getURI())); 
    }
    
}
