package com.opendomotic.device.pi.serial;

import com.opendomotic.device.Device;

/**
 *
 * @author jaques
 * @param <T>
 */
public class SerialDevice<T> implements Device<T> {

    private int address;
    private int device;

    @Override
    public void setValue(T value) throws Exception {
        if (SerialBus.getInstance().writeDevice(address, device, (Integer) value) == -1) {
            throw new Exception(getExceptionMessage("setValue"));
        }
    }

    @Override
    public T getValue() throws Exception {
        return (T) getValueInt();
    }

    protected Integer getValueInt() throws Exception {
        int value = SerialBus.getInstance().readDevice(address, device);
        if (value == -1) {
            throw new Exception(getExceptionMessage("getValue"));
        }
        return value;
    }
    
    private String getExceptionMessage(String local) {
        return String.format("Error on %s of address=%d, device=%d", local, address, device);
    }
    
    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "SerialDevice{" + "address=" + address + ", device=" + device + '}';
    }
    
}
