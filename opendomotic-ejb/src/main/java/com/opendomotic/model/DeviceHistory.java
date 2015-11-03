/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.opendomotic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Jaques Claudino
 */
public class DeviceHistory {
    
    private static final Logger LOG = Logger.getLogger(DeviceHistory.class.getName());
    private static final int MAX_HISTORY_RECORDS = 2*60*24*7; //máximo de 7 dias, com 2 leituras por minuto.
    
    private final List<DeviceHistoryItem> list = new ArrayList<>();
    
    public void add(Date date, Comparable value) {
        if (list.size() >= MAX_HISTORY_RECORDS) {
            list.remove(0);
        }
        list.add(new DeviceHistoryItem(date, value));        
    }

    public List<DeviceHistoryItem> getList() {
        return list;
    }
    
    public List<DeviceHistoryItem> getListCopy(int minutesInterval, int maxItems) {
        List<DeviceHistoryItem> listCopy = new ArrayList<>();
        DeviceHistoryItem last = null;
        DeviceHistoryItem min = null;
        DeviceHistoryItem max = null;
        int millisInterval = minutesInterval*60*1000;
        
        int i = list.size()-1;
        while (i >= 0 && listCopy.size() < maxItems) {
            DeviceHistoryItem item = list.get(i);
            
            if (min == null || item.getValue().compareTo(min.getValue()) < 0) {
                min = item;
            }
            if (max == null || item.getValue().compareTo(max.getValue()) > 0) {
                max = item;
            }            
            if (last == null || minutesInterval == 0 || last.getDate().getTime() - item.getDate().getTime() >= millisInterval) {
                listCopy.add(item);
                last = item;
            }
                                    
            i--;
        }
        
        if (min != null && !listCopy.contains(min)) {
            listCopy.add(min);
        }
        if (max != null && !listCopy.contains(max)) {
            listCopy.add(max);
        }
        
        Collections.sort(listCopy, 
            new Comparator<DeviceHistoryItem>() {
                @Override
                public int compare(DeviceHistoryItem o1, DeviceHistoryItem o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            }
        );
        
        return listCopy;
    }
    
    public class DeviceHistoryItem {
        
        private final Date date;
        private final Comparable value;

        public DeviceHistoryItem(Date date, Comparable value) {
            this.date = date;
            this.value = value;
        }

        public Date getDate() {
            return date;
        }

        public Comparable getValue() {
            return value;
        }
        
        /*public Integer getValueAsInt() {
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
        }*/
        
    }
    
}
