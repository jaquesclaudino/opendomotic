package com.opendomotic.device.impl;

import com.opendomotic.device.Device;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 *
 * @author Jaques Claudino
 * Aug 6, 2015
 */
public class SocketDevice<T> implements Device<T> {
    
    private static final int BUFFER_RX_MAX_SIZE = 1000;
    
    private String host;
    private int port;
    private String readCommand;
    private String writeCommand;
    private int timeout = 1000;
    
    @Override
    public T getValue() throws Exception {
        int[] bufferRx = writeSocket(readCommand, true);        
        if (bufferRx != null) {
            return convertValue(bufferRx);
        } else {        
            return null;
        }
    }

    @Override
    public void setValue(T value) throws Exception {
        if (getWriteCommand() != null) {
            writeSocket(getWriteCommand(), false);
        }
    }
    
    protected int[] writeSocket(String command, boolean waitRx) throws IOException, InterruptedException {
        try (Socket socket = new Socket(host, port)) {            
            socket.getOutputStream().write(command.getBytes());
            
            if (waitRx) {
                return readSocket(socket);
            } else {
                return null;
            }
        }
    }
    
    protected T convertValue(int[] buffer) {
        return (T) bufferToString(buffer);
    }
    
    protected int[] readSocket(Socket socket) throws IOException, InterruptedException {
        InputStream input = socket.getInputStream();
        int[] bufferRx = new int[BUFFER_RX_MAX_SIZE];
        int bufferRxIndex = 0;        
        long millisTimeout = System.currentTimeMillis()+timeout;        
        
        while (System.currentTimeMillis() < millisTimeout) {
            if (input.available() > 0) {
                int value = input.read();
                if (bufferRxIndex < bufferRx.length-1) {
                    bufferRx[bufferRxIndex++] = value;
                }
            } else if (bufferRxIndex > 0) {
                return Arrays.copyOf(bufferRx, bufferRxIndex); //parou de receber, entao cai fora.
            } else {
                Thread.sleep(1);
            }
        }
        return null;
    }

    protected String bufferToString(int[] buffer) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] < 32 || buffer[i] > 126) {
                sb.append(String.format("#%d ", buffer[i]));
            } else {
                sb.append(Character.toChars(buffer[i]));
            }
        }
        return sb.toString();
    }
    
    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getReadCommand() {
        return readCommand;
    }

    public void setReadCommand(String readCommand) {
        this.readCommand = readCommand;
    }

    public String getWriteCommand() {
        return writeCommand;
    }
    
    public void setWriteCommand(String writeCommand) {
        this.writeCommand = writeCommand;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
