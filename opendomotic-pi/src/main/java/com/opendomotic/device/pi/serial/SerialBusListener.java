/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.device.pi.serial;

import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import java.util.logging.Logger;

/**
 *
 * @author Jaques
 */
public class SerialBusListener implements SerialDataListener {

    private static final Logger LOG = Logger.getLogger(SerialBusListener.class.getName());
    private static final boolean SHOW_LOG = false;

    @Override
    public void dataReceived(SerialDataEvent event) {
        //byte ferra quando é maior que 127. Deve ser por causa do unsigned. Vem 1 byte a mais na frente com 194 ou 195
        StringBuilder log = new StringBuilder("listener RX=");
        byte[] bufferRx = event.getData().getBytes();  
        for (int i=0; i<bufferRx.length; i++) {
            int rx = bufferRx[i] & 0xff; //unsigned
            log.append(rx);            
            log.append("|");
        }
        if (SHOW_LOG) {
            LOG.info(log.toString());
        }
    }
    
}
