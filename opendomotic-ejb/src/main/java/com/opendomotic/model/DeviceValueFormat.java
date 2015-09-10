package com.opendomotic.model;

import java.util.logging.Logger;

/**
 *
 * @author jaques
 */
public class DeviceValueFormat {
    
    private static final Logger LOG = Logger.getLogger(DeviceValueFormat.class.getName());   
    private static final String FORMAT_HOUR_MINUTE = "%hh:mm";    
    
    public String format(Object value, String format) {
        try {
            if (value != null && format != null && !format.isEmpty()) {
                if (format.contains(FORMAT_HOUR_MINUTE)) {
                    format = format.replace(FORMAT_HOUR_MINUTE, formatHourMinute((int) value));
                }                
                return String.format(format, value);
            } else {
                return String.valueOf(value);
            }    
        } catch (Exception ex) {
            LOG.severe(ex.toString());
        }
        return null;
    }
    
    private String formatHourMinute(int minutes) {
        return String.format("%02d:%02d", minutes/60, minutes%60);
    }
    
}
