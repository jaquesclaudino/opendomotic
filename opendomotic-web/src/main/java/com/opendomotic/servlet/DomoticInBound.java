/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.servlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

/**
 *
 * @author jaques
 */
public class DomoticInBound extends MessageInbound {
 
    private static final Logger LOG = Logger.getLogger(DomoticInBound.class.getName());
 
    private final DomoticWebsocket servlet;

    public DomoticInBound(DomoticWebsocket domoticWebsocket) {
        servlet = domoticWebsocket;
    }

    @Override
    protected void onOpen(WsOutbound outbound) {
        servlet.getConnections().add(this);
    }

    @Override
    protected void onClose(int status) {
        super.onClose(status);
        
        LOG.log(Level.INFO, "status={0}", status);
        
        servlet.getConnections().remove(this);
    }

    @Override
    protected void onBinaryMessage(ByteBuffer bb) throws IOException {
        getWsOutbound().writeBinaryMessage(bb);
    }

    @Override
    protected void onTextMessage(CharBuffer cb) throws IOException {
        servlet.sendBroadcast(cb.toString());
    }
    
}
