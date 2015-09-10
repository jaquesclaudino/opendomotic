/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.device.factory;

import com.opendomotic.device.Device;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaques
 */
public class DeviceFactory {
    
    private static final Logger LOG = Logger.getLogger(DeviceFactory.class.getName());
    
    public static Device createDevice(String className, Map<String, Object> properties) throws ClassNotFoundException, InstantiationException, IllegalAccessException, DevicePropertyNotFoundException, IllegalArgumentException, InvocationTargetException {
        Device device = (Device) Class.forName(className).newInstance();
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            Method method = getMethod(device, property.getKey());   
            if (method == null || method.getParameterTypes().length != 1) {
                LOG.log(Level.SEVERE, "property {0} not found", property.getKey());
                throw new DevicePropertyNotFoundException();
            }
            
            if (isStringToBoolean(property.getValue(), method)) {
                method.invoke(device, Boolean.parseBoolean((String) property.getValue()));
            } else if (isStringToDouble(property.getValue(), method)) {
                method.invoke(device, Double.parseDouble((String) property.getValue()));
            } else if (isStringToInteger(property.getValue(), method)) {
                method.invoke(device, Integer.parseInt((String) property.getValue()));
            } else {
                method.invoke(device, property.getValue());
            }
        }        
        return device;
    }
    
    private static Method getMethod(Device device, String name) {
        for (Method method : device.getClass().getMethods()) {
            if (method.getName().equalsIgnoreCase("set"+name)){
                return method;
            }
        }
        return null;
    }    
    
    private static boolean isStringToBoolean(Object value, Method method) {
        return isStringToType(value, method, boolean.class, Boolean.class);
    }
    
    private static boolean isStringToDouble(Object value, Method method) {
        return isStringToType(value, method, double.class, Double.class);
    }
    
    private static boolean isStringToInteger(Object value, Method method) {
        return isStringToType(value, method, int.class, Integer.class);
    }
    
    private static boolean isStringToType(Object value, Method method, Class... classTypes) {
        if (!(value instanceof String))
            return false;
        
        String paramType = method.getParameterTypes()[0].getName();
        for (Class c : classTypes) {
            if (paramType.equals(c.getName())) { //TO-DO: comparacao direta com classType nao funcionou. Verificar forma de evitar comparacao de string.
                return true;
            }
        }
        //no one classType found:
        return false;
    }
    
}
