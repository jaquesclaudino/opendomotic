package com.opendomotic.service;

import com.opendomotic.model.DeviceProxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

/**
 *
 * @author Jaques Claudino
 */
@Stateless
public class DeviceWorker {
    
    private static final Logger LOG = Logger.getLogger(DeviceWorker.class.getName());
    
    @Asynchronous
    public Future<List<DeviceProxy>> updateDevices(String threadId, List<DeviceProxy> listDevice) {
        List<DeviceProxy> listUpdated = new ArrayList<>();
        for (DeviceProxy device : listDevice) {
            boolean updated = false;
                   
            try {
                if (device.updateValue()) {
                    updated = true;
                }
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Error updating device: {0} | {1}", new Object[] {device.getName(), ex.toString()});
                device.incErrors();
                device.setValue(null);
                updated = true;
            }
            
            if (updated) {                
                listUpdated.add(device);
            }
        }
        return new AsyncResult<>(listUpdated);
    }
    
}
