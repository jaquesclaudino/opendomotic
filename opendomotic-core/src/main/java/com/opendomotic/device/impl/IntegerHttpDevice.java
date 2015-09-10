package com.opendomotic.device.impl;

/**
 *
 * @author Jaques Claudino
 */
public class IntegerHttpDevice extends HttpDevice<Integer> {

    @Override
    public Integer getValue() throws Exception {
        return Integer.parseInt(makeRequest(getURI()));
    }
    
}
