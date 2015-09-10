package com.opendomotic.service;

import com.opendomotic.model.entity.Job;
import com.opendomotic.service.dao.JobDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author jaques
 */
@Singleton
@Startup
public class JobService {
    
    private static final Logger LOG = Logger.getLogger(JobService.class.getName());
    
    @Inject
    private DeviceService deviceService;
    
    @Inject
    private JobDAO jobDAO;
        
    private final List<Job> listExecuteJob = new ArrayList<>(); //added to the list to rewrite when error
    
    @Schedule(minute = "*/1", hour = "*")
    public void timerJobs() {
        checkExecuteJobs();
        if (executeJobs()) {
            deviceService.updateDeviceValuesAsync();
        }
    }
    
    private void checkExecuteJobs() {
        Date now = new Date();
            
        for (Job job : jobDAO.findAllEnabled()) {
            try {
                if (canExecuteJob(job, now) && !listExecuteJob.contains(job)) {
                    listExecuteJob.add(job);
                }   
            } catch (Exception ex) {
                LOG.log(Level.INFO, "Error checking job: {0} | {1}", new Object[] {job.toString(), ex.toString()});
            }
        }
    }
    
    private boolean canExecuteJob(Job job, Date now) throws Exception {
        if (job.getInput() != null) {
            Object inputValue = deviceService.getDeviceValue(job.getInput().getName());
            if (inputValue != null) {
                if (!(inputValue instanceof Comparable)) {
                    throw new Exception("Device.value must be a Comparable instance");
                }
                Comparable input = (Comparable) inputValue;
                Object expectValue = job.getExpectValueAsType(inputValue.getClass());
                return job.getOperator().compare(input, expectValue);
            }
        } else if (job.getInputDate() != null && job.getInputDate().compareTo(now) <= 0) {
            return true;
        }
        return false;
    }
    
    private boolean executeJobs() {
        boolean executed = false;
        int index = 0;
        while (index < listExecuteJob.size()) {            
            Job job = listExecuteJob.get(index);
            try {
                if (executeJob(job)) {
                    listExecuteJob.remove(index);
                    executed = true;
                } else {
                    index++;
                }            
            } catch (Exception ex) {
                LOG.log(Level.INFO, "Error executing job: {0} | {1}", new Object[] {job.toString(), ex.toString()});
            }
        }        
        return executed;
    }
    
    private boolean executeJob(Job job) {
        boolean executed = false;
        Object outputValue = deviceService.getDeviceValue(job.getOutput().getName());
        if (outputValue != null) {
            Object actionValue = job.getActionValueAsType(outputValue.getClass());
            if (outputValue.equals(actionValue)) {
                executed = true; //device already has the action value 
            } else {
                LOG.log(Level.INFO, "Executing job: {0}", job.toString());

                deviceService.setDeviceValue(
                        job.getOutput().getName(), 
                        actionValue);

                //check if has really changed:
                outputValue = deviceService.getDeviceValue(job.getOutput().getName());
                if (outputValue != null && outputValue.equals(actionValue)) {
                    if (job.isDeleteAfterExecute()) {
                        jobDAO.delete(job);
                    }
                    executed = true;
                } else {
                    LOG.log(Level.WARNING, "Error on setDeviceValue of job: {0}", job.toString());
                }
            }
        }
        return executed;
    }
    
}
