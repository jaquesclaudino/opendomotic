package com.opendomotic.device.impl;

import com.opendomotic.device.Device;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaques Claudino
 */
public class LineSocketDevice implements Device<String> {
    
    private static final Logger LOG = Logger.getLogger(LineSocketDevice.class.getName());
    
    private String host;
    private int port;
    private String command;
    private String response = "unknown";

    @Override
    public String getValue() throws Exception {
        LOG.info("SocketDevice.getValue");
        return response;
    }

    @Override
    public void setValue(String value) throws Exception { 
        
        LOG.info("SocketDevice.setValue");
        try (Socket socket = new Socket(host, port); 
                PrintWriter out = new PrintWriter(socket.getOutputStream(), false);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            LOG.info("SocketDevice connected");            
            
            out.write(command);
            out.write('\r');
            out.flush();
            
            response = in.readLine();
            
            LOG.log(Level.INFO, "SocketDevice response={0}", response);
            
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            response = ex.toString();
        }        
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
