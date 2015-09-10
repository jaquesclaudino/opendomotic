/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.opendomotic.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaques Claudino
 */
public class DeviceHistory {
    
    private static final Logger LOG = Logger.getLogger(DeviceHistory.class.getName());
    private static final int HISTORY_RECORDS = 1440;
    
    private List<DeviceHistoryItem> list = new ArrayList<>();
    
    public void add(Date date, Object value) {
        if (list.size() >= HISTORY_RECORDS) {
            list.remove(0);
        }
        list.add(new DeviceHistoryItem(date, value));        
    }

    public List<DeviceHistoryItem> getList() {
        return list;
    }
    
    public class DeviceHistoryItem {
        
        private final Date date;
        private final Object value;

        public DeviceHistoryItem(Date date, Object value) {
            this.date = date;
            this.value = value;
        }

        public Date getDate() {
            return date;
        }

        public Object getValue() {
            return value;
        }
        
        public Integer getValueAsInt() {
            if (value instanceof Integer) {
                return (Integer) value;
            } else if (value instanceof Double) { 
                return ((Double) value).intValue();
            } else if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else {
                LOG.log(Level.SEVERE, "Type not supported: {0}", value.getClass().getName());
                return null;
            }
        }
        
    }
    
}
