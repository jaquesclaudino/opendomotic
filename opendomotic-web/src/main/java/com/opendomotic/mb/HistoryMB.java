package com.opendomotic.mb;

import com.opendomotic.model.DeviceHistory;
import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.service.DeviceService;
import com.opendomotic.service.dao.DeviceConfigDAO;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author Jaques Claudino
 */
@Named
@SessionScoped
public class HistoryMB implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(HistoryMB.class.getName());
    private static final int DEFAULT_INTERVAL = 10;
    private static final int DEFAULT_MAX_ITEMS = 100;
    
    @Inject 
    private DeviceService deviceService;
    
    @Inject 
    private DeviceConfigDAO deviceConfigDAO;
    
    private DeviceConfig config;
    private DeviceHistory history;
    private CartesianChartModel linearModel;  
    private List<String> listItem;
    private LineChartSeries lineSeries;
    private DeviceHistory.DeviceHistoryItem min;
    private DeviceHistory.DeviceHistoryItem max;
    private DeviceHistory.DeviceHistoryItem last;    
    private List<DeviceConfig> listDeviceConfigHistory;
    
    private int idConfig;
    private int minutesInterval = DEFAULT_INTERVAL;
    private int maxItems = DEFAULT_MAX_ITEMS;
    
    @PostConstruct
    public void init() {
        listDeviceConfigHistory = deviceConfigDAO.findAllEnabledWithHistory();        
        if (!listDeviceConfigHistory.isEmpty()) {
            refresh(listDeviceConfigHistory.get(0));
        }
    }
       
    public void refresh() {
        config = deviceConfigDAO.findById(idConfig); //by selectOneMenu       
        if (config != null) {
            refresh(config);
        }        
    }
    
    public void refresh(DeviceConfig config) {
        this.idConfig = config.getId();
        this.config = config;
        this.history = deviceService.getDeviceHistory(config);
        if (history != null) {
            createLinearModel();
        }
    }
    
    public void checkParamName() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String paramName = params.get("name");
        if (paramName != null) {
            DeviceConfig configParam = deviceConfigDAO.findByName(paramName);
            if (configParam != null) {
                refresh(configParam);
            }
        }
    }
    
    private void createLinearModel() {
        lineSeries = new LineChartSeries();
        lineSeries.setLabel(config.getName());
        lineSeries.setFill(true);
        linearModel = new CartesianChartModel();
        linearModel.addSeries(lineSeries); 
        listItem = new ArrayList<>();        
        List<DeviceHistory.DeviceHistoryItem> list = history.getListCopy(minutesInterval, maxItems);
        SimpleDateFormat sdf = new SimpleDateFormat(getDateFormat());
        
        last = null; 
        min = null;
        max = null;
        
        if (!list.isEmpty()) {        
            for (DeviceHistory.DeviceHistoryItem item : list) {
                String dateFmt = sdf.format(item.getDate());
                if (!lineSeries.getData().containsKey(dateFmt)) {
                    lineSeries.set(dateFmt, (Number) item.getValue());
                    listItem.add(String.format("%s = %s", dateFmt, item.getValue().toString()));
                }
                
                if (min == null || item.getValue().compareTo(min.getValue()) < 0) {
                    min = item;
                }
                if (max == null || item.getValue().compareTo(max.getValue()) > 0) {
                    max = item;
                }
            }   
            
            last = list.get(list.size()-1);
        }        
    }
    
    private String getDateFormat() {
        if (minutesInterval*maxItems > 1000) {
            return "dd/MM HH:mm";
        }        
        if (minutesInterval == 0) {
            return "HH:mm:ss";
        }
        return "HH:mm";
    }
    
    public void configChangeMethod(ValueChangeEvent e) {
        idConfig = (Integer) e.getNewValue();
        refresh();
    }
    
    public void intervalChangeMethod(ValueChangeEvent e) {
        minutesInterval = (Integer) e.getNewValue();
        refresh();
    }
    
    public void maxItemsChangeMethod(ValueChangeEvent e) {
        LOG.info(e.getNewValue().toString());
        maxItems = (Integer) e.getNewValue();
        refresh();
    }
    
    public void itemSelect(ItemSelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, listItem.get(event.getItemIndex()), ""));  
    }
    
    public CartesianChartModel getLinearModel() {      
        return linearModel;
    }

    public DeviceHistory getHistory() {
        return history;
    }

    public DeviceHistory.DeviceHistoryItem getMin() {
        return min;
    }

    public DeviceHistory.DeviceHistoryItem getMax() {
        return max;
    }

    public DeviceHistory.DeviceHistoryItem getLast() {
        return last;
    }

    public int getMinutesInterval() {
        return minutesInterval;
    }

    public void setMinutesInterval(int minutesInterval) {
        this.minutesInterval = minutesInterval;
    }

    public int getIdConfig() {
        return idConfig;
    }

    public void setIdConfig(int idConfig) {
        this.idConfig = idConfig;
    }

    public List<DeviceConfig> getListDeviceConfigHistory() {
        return listDeviceConfigHistory;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }
    
}
