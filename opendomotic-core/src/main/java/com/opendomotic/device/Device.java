package com.opendomotic.device;

/**
 *
 * @author jaques
 * @param <T>
 */
public interface Device<T> {

    T getValue() throws Exception;
    void setValue(T value) throws Exception;
    
}
