package com.opendomotic.device.pi.i2c;

import com.opendomotic.device.Device;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;

/**
 *
 * @author Jaques Claudino
 * @param <T>
 */
public class I2CDevice<T> implements Device<T> {
    
    private int address;
    private int device;
    private int readBytes = 2;
    private int invalidValue = 9999;
    
    private com.pi4j.io.i2c.I2CDevice i2c;

    @Override
    public T getValue() throws Exception {
        return (T) getValueInt();
    }

    @Override
    public void setValue(T value) throws Exception {
        byte msb = (byte) ((Integer) value / 256);
        byte lsb = (byte) ((Integer) value % 256);                
        //writeI2C(new byte[] {'w', (byte) device, msb, lsb});
        writeI2C(new byte[] {'w', (byte) device, msb, lsb});
    }
    
    protected Integer getValueInt() throws Exception {   
        //return writeI2C(new byte[] {'r', (byte) device});
        /*if (i2c == null) {
            i2c = I2CFactory.getInstance(I2CBus.BUS_1).getDevice(address);
        }        
        i2c.write(bufferTx, 0, bufferTx.length);*/
        writeI2C(new byte[] {'r', (byte) device});
        
        Thread.sleep(5); //precisa de um intervalo
        
        byte[] bufferRx = new byte[readBytes]; 
        i2c.read(bufferRx, 0, readBytes);
        int value;        
        
        if (readBytes == 4) {
            value = (bufferRx[0] << 24) + //o primeiro byte contém o sinal
                ((bufferRx[1] & 0xff) << 16) + //& 0xff pra ser unsigned. Outros bytes nao contém sinal
                ((bufferRx[2] & 0xff) << 8) +
                (bufferRx[3] & 0xff);
        } else { //2 bytes:
            int msb = bufferRx[0] & 0xff;
            int lsb = bufferRx[1] & 0xff;            
            value = msb * 256 + lsb;
        }
        
        if (value == invalidValue) {                 
            return null;
        }        
        return value;
    }   
    
    private void writeI2C(byte[] bufferTx) throws IOException, InterruptedException {
        getI2C().write(bufferTx, 0, bufferTx.length);
    }
    
    /*private Integer writeI2C(byte[] bufferTx) throws IOException, InterruptedException {
        if (i2c == null) {
            i2c = I2CFactory.getInstance(I2CBus.BUS_1).getDevice(address);
        }        
        i2c.write(bufferTx, 0, bufferTx.length);
        
        Thread.sleep(5); //precisa de um intervalo
        
        byte[] bufferRx = new byte[readBytes]; 
        i2c.read(bufferRx, 0, readBytes);
        int value;        
        
        if (readBytes == 4) {
            value = (bufferRx[0] << 24) + //o primeiro byte contém o sinal
                ((bufferRx[1] & 0xff) << 16) + //& 0xff pra ser unsigned. Outros bytes nao contém sinal
                ((bufferRx[2] & 0xff) << 8) +
                (bufferRx[3] & 0xff);
        } else { //2 bytes:
            int msb = bufferRx[0] & 0xff;
            int lsb = bufferRx[1] & 0xff;            
            value = msb * 256 + lsb;
        }
        
        if (value == invalidValue) {                 
            return null;
        }        
        return value;
    }*/
    
    private com.pi4j.io.i2c.I2CDevice getI2C() throws IOException {
        if (i2c == null) {
            i2c = I2CFactory.getInstance(I2CBus.BUS_1).getDevice(address);
        }
        return i2c;
    }
    
    public void setAddress(int address) {
        this.address = address;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public void setReadBytes(int readBytes) {
        this.readBytes = readBytes;
    }

    public void setInvalidValue(int invalidValue) {
        this.invalidValue = invalidValue;
    }
    
}
