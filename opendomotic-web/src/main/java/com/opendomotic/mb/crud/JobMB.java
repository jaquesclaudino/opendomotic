package com.opendomotic.mb.crud;

import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.entity.Job;
import com.opendomotic.model.entity.JobOperator;
import com.opendomotic.service.dao.AbstractDAO;
import com.opendomotic.service.dao.CriteriaGetter;
import com.opendomotic.service.dao.DeviceConfigDAO;
import com.opendomotic.service.dao.JobDAO;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jaques
 */
@Named
@SessionScoped
public class JobMB extends AbstractSelectableCRUD<Job> {
    
    private static final Logger LOG = Logger.getLogger(JobMB.class.getName());
    
    @Inject
    private JobDAO jobDAO;
    
    @Inject
    private DeviceConfigDAO deviceConfigDAO;
    
    private Integer idInput;
    private Integer idOutput;
    private List<JobOperator> listOperator;
    private int tabActiveIndex;
    private int hour;
    private int minute;

    @Override
    public AbstractDAO<Job> getDAO() {
        return jobDAO;
    }

    @Override
    public void create() {
        super.create();
        hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        minute = Calendar.getInstance().get(Calendar.MINUTE);
        entity.setEnabled(true);
    }

    @Override
    public void save() {
        if (tabActiveIndex == 0) {
            entity.setInputDate(null);
            
            if (idInput != null) {
                entity.setInput(deviceConfigDAO.findById(idInput));
            }
        } else { //inputDate:
            entity.setInput(null);
            entity.setOperator(null);
            entity.setExpectValue(null);
            
            Date inputDate = entity.getInputDate();
            if (inputDate == null) {
                inputDate = new Date();
            }
            
            Calendar c = Calendar.getInstance();
            c.setTime(inputDate);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            entity.setInputDate(c.getTime());
        }
        
        if (idOutput != null) {
            entity.setOutput(deviceConfigDAO.findById(idOutput));
        }
        super.save();
    }

    @Override
    public void edit(Job entity) {
        idInput = getIdDeviceConfig(entity.getInput());
        idOutput = getIdDeviceConfig(entity.getOutput());
        tabActiveIndex = idInput != null || entity.getId() == null ? 0 : 1;
        super.edit(entity); 
    }
    
    public void toggleEnable(int id) {
        LOG.log(Level.INFO, "id={0}", id);
        Job job = jobDAO.findById(id);
        LOG.info(job.toString());
        job.setEnabled(!job.isEnabled());
        jobDAO.save(job);
        clearList();
    }

    @Override
    protected CriteriaGetter.OrderGetter getOrderGetter() {
        return jobDAO.getCriteriaGetter().getOrderAttributeAsc("index");
    }
    
    private Integer getIdDeviceConfig(DeviceConfig deviceConfig) {
        return deviceConfig != null ? deviceConfig.getId() : null;         
    }
    
    public Integer getIdInput() {
        return idInput;
    }

    public void setIdInput(Integer idInput) {
        this.idInput = idInput;
    }

    public Integer getIdOutput() {
        return idOutput;
    }

    public void setIdOutput(Integer idOutput) {
        this.idOutput = idOutput;
    }
    
    public List<JobOperator> getListOperator() {
        if (listOperator == null) {
            listOperator = Arrays.asList(JobOperator.values());
        }
        return listOperator;
    }

    public int getTabActiveIndex() {
        return tabActiveIndex;
    }

    public void setTabActiveIndex(int tabActiveIndex) {
        this.tabActiveIndex = tabActiveIndex;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
    
}
