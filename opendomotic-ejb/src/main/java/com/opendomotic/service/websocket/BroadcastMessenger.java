/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.websocket;

/**
 *
 * @author jaques
 */
public interface BroadcastMessenger {
    
    void sendBroadcast(String message);
    
}
