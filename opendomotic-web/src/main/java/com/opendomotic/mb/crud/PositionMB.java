/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb.crud;

import com.opendomotic.service.dao.EnvironmentDAO;
import com.opendomotic.service.dao.DevicePositionDAO;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jaques
 */
@Named
@SessionScoped
public class PositionMB implements Serializable {
    
    @Inject 
    private EnvironmentDAO environmentDAO;
    
    @Inject 
    private DevicePositionDAO positionDAO;
    
    private Integer idEnvironment;    
    private Integer idConfig;
    
    @PostConstruct
    public void init() {
        idEnvironment = environmentDAO.findFirst().getId();
    }
    
    public void valueChangeMethod(ValueChangeEvent e) {
        idEnvironment = (Integer) e.getNewValue();
    }
    
    public void add() {
        positionDAO.save(idEnvironment, idConfig);       
    }
    
    public void remove() {
        positionDAO.delete(idEnvironment, idConfig);       
    }
    
    public void clear() {
        positionDAO.deleteByIdEnvironment(idEnvironment);
    }

    public Integer getIdEnvironment() {
        return idEnvironment;
    }

    public void setIdEnvironment(Integer idEnvironment) {
        this.idEnvironment = idEnvironment;
    }

    public Integer getIdConfig() {
        return idConfig;
    }

    public void setIdConfig(Integer idConfig) {
        this.idConfig = idConfig;
    }
    
}
