package com.opendomotic.service.websocket;

import javax.ejb.Singleton;

/**
 *
 * @author jaques
 */
@Singleton
public class WebSocketService {
        
    private static final String UPDATE_DEVICE_VALUE  = "updateDeviceValue";
    
    private BroadcastMessenger broadcastMessenger;
    
    public void send(String msg) {
        if (broadcastMessenger != null) {
            broadcastMessenger.sendBroadcast(msg);
        }
    }
    
    public void sendUpdateDeviceValue(String name, String value) {
        send(UPDATE_DEVICE_VALUE + "|" + name + "|" + value);
    }
    
    public void setBroadcastMessenger(BroadcastMessenger broadcastMessenger) {
        this.broadcastMessenger = broadcastMessenger;
    }
    
}
