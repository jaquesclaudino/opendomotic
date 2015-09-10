package com.opendomotic.device.pi.serial;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaques
 */
public class SerialBus {
    
    private static final char COMMAND_READ  = 'r';
    private static final char COMMAND_WRITE = 'w';    
    
    private static final int INDEX_ADDRESS = 0;
    private static final int INDEX_DATA_LENGTH = 1;
    private static final int INDEX_DATA_BEGIN  = 2;
    
    private static final int READ_DEVICE_ATTEMPTS = 3; //tentativas para ler o device
    private static final int WRITE_DEVICE_ATTEMPTS = 3; //tentativas para alterar o device
    private static final int TIMEOUT = 100;
    private static final int MAX_BUFFER_RX_SIZE = 200;
    
    private static SerialBus instance;
    private static final Logger LOG = Logger.getLogger(SerialBus.class.getName());
    private static final boolean SHOW_LOG = false;
    
    //GPIO:
    private GpioController gpio;
    private final GpioPinDigitalOutput pinRE;
    private Serial serial = SerialFactory.createInstance();
    private final SerialDataListener serialListener = new SerialBusListener();
    
    private SerialBus() {        
        gpio = GpioFactory.getInstance();
        pinRE = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "PIN_RE_485", PinState.LOW);
        serial.open(Serial.DEFAULT_COM_PORT, 115200);
        serial.addListener(serialListener);
    }
    
    public static synchronized SerialBus getInstance() {
        if (instance == null) {
            instance = new SerialBus();
        }
        return instance;
    }
    
    public int readDevice(int address, int device) {
        return serialWriteAndWaitResponse(getBufferTx(address, COMMAND_READ, device, 0), READ_DEVICE_ATTEMPTS);
    }
    
    public int writeDevice(int address, int device, int value) {
        return serialWriteAndWaitResponse(getBufferTx(address, COMMAND_WRITE, device, value), WRITE_DEVICE_ATTEMPTS);
    }
    
    public void shutdown() {
        if (gpio != null) {
            serial.shutdown();
            serial = null;
            gpio.shutdown();
            gpio = null;
        }
    }
    
    private void serialWrite(byte[] bufferTx) {
        try {
            pinRE.high(); //tx mode
            serial.write(bufferTx);
            serial.flush();
            Thread.sleep(5); //esperar slave receber 5 era pouco para 9600
            pinRE.low(); //rx mode
        } catch (IllegalStateException | InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);    
        }
    }
    
    private synchronized int serialWriteAndWaitResponse(byte[] bufferTx, int attempts) {
        serial.removeListener(serialListener);
        try {
            serialWrite(bufferTx);
            byte[] bufferRx = waitResponse(bufferTx[INDEX_ADDRESS]);
            if (bufferRx != null) {
                return getBufferInt(bufferRx, 0); //conseguiu ler
            } else if (attempts > 0) {
                if (SHOW_LOG) {
                    LOG.warning("Error on reading device. Trying again...");
                }
                return serialWriteAndWaitResponse(bufferTx, attempts-1);
            } else {
                LOG.severe("Failed on read device.");
                return -1;
            }
        } finally {
            serial.addListener(serialListener);
        }
    }
    
    private byte[] waitResponse(byte address) {
        byte[] bufferRx = new byte[MAX_BUFFER_RX_SIZE];
        int indexRx=0;
        long millisStart = System.currentTimeMillis();        
        
        StringBuilder log = new StringBuilder("waitResponse RX=");
        while (System.currentTimeMillis()-millisStart < TIMEOUT) {
            if (serial.availableBytes() > 0) {
                bufferRx[indexRx++] = (byte) serial.read();
                log.append(bufferRx[indexRx-1]);
                log.append("|");
            } else if (indexRx > 0) {
                break;
            }
        }
        
        long tempo = System.currentTimeMillis()-millisStart;
        if (SHOW_LOG) {
            log.append(tempo);
            log.append(" ms");
            LOG.info(log.toString());
        }
        
        if (indexRx > 0 && address == bufferRx[0] && isCheckSumOK(bufferRx))
            return Arrays.copyOf(bufferRx, indexRx);
        return null;        
    }
    
    private boolean isCheckSumOK(byte[] bufferRx) {
        int dataLength = bufferRx[INDEX_DATA_LENGTH] & 0xff;
        int checksum = 0;
        int i;
        for (i=0; i<dataLength+INDEX_DATA_BEGIN; i++) {
            checksum += bufferRx[i] & 0xff;
        }

        int msb = bufferRx[i] & 0xff;
        int lsb = bufferRx[i+1] & 0xff;
        boolean isOK = (msb == checksum / 256) && (lsb == checksum % 256);

        if (SHOW_LOG && !isOK) {
            StringBuilder log = new StringBuilder("CHECKSUM ERROR: ");
            log.append(checksum);
            log.append(" rx=");
            log.append(bufferRx[i]);
            log.append("|");
            log.append(bufferRx[i+1]);
            log.append(" expected=");
            log.append(checksum / 256);
            log.append("|");
            log.append(checksum % 256);
            LOG.severe(log.toString());
        }

        return isOK;
    }
    
    private byte[] getBufferTx(int address, int command, int device, int value) {
        byte[] bufferTx = new byte[8];
        byte len = 4;
        
        bufferTx[0] = (byte)address;
        bufferTx[1] = len;
        bufferTx[2] = (byte)command;
        bufferTx[3] = (byte)device;
        bufferTx[4] = (byte)(value / 256);
        bufferTx[5] = (byte)(value % 256);
        
        int checksum = address + len + command + device + value;
        bufferTx[6] = (byte)(checksum / 256);
        bufferTx[7] = (byte)(checksum % 256);
        
        return bufferTx;
    }
    
    private int getBufferInt(byte[] bufferRx, int dataIndex) {
        int msb = bufferRx[INDEX_DATA_BEGIN + dataIndex] & 0xff;
        int lsb = bufferRx[INDEX_DATA_BEGIN + dataIndex + 1] & 0xff;
        return msb * 256 + lsb;
    }
    
}
