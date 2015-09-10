package com.opendomotic.device.impl;

import com.opendomotic.device.Device;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaques Claudino
 */
public class SimpleHttpDevice implements Device<Integer> {

    private static final Logger LOG = Logger.getLogger(SimpleHttpDevice.class.getName());
    private static final boolean SHOW_LOG = false;
    private static final int DEFAULT_TIMEOUT = 2000;
    
    private String host;
    private String path;
    
    @Override
    public Integer getValue() throws Exception {
        return Integer.parseInt(makeRequest("GET /" + path, true));
    }

    @Override
    public void setValue(Integer value) throws Exception {
        makeRequest("GET /" + path + "=" + value, false);
    }

    private String makeRequest(String request, boolean waitResponse) throws IOException {
        long time = System.currentTimeMillis();

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, 80), DEFAULT_TIMEOUT);
        socket.setSoTimeout(DEFAULT_TIMEOUT);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);        
        
        out.println(request);        
        
        String responseLast = null;
        String responseBuffer = "";
        if (waitResponse) {            
            String input;            
            while ((input = in.readLine()) != null) {
                responseBuffer += input + "|";
                responseLast = input;
            }
        }
                
        if (SHOW_LOG) {
            LOG.log(Level.INFO, "Response={0} | {1} | {2} ms", new Object[] {responseBuffer, request, System.currentTimeMillis() - time});
        }
        
        return responseLast;
    }
        
    public void setHost(String host) {
        this.host = host;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    @Override
    public String toString() {
        return "SimpleHttpDevice{" + "host=" + host + ", path=" + path + '}';
    }
    
}
