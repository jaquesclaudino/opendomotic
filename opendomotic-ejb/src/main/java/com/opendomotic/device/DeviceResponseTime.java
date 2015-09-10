package com.opendomotic.device;

import com.opendomotic.service.DeviceService;
import javax.naming.InitialContext;

/**
 *
 * @author jaques
 */
public class DeviceResponseTime implements Device<Integer> {

    @Override
    public Integer getValue() throws Exception {
        return ((DeviceService) InitialContext.doLookup("java:module/DeviceService")).getDeviceMillisResponseSum();
    }

    @Override
    public void setValue(Integer value) throws Exception {
    }
    
}
