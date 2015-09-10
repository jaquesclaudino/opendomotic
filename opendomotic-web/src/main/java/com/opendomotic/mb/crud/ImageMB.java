/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb.crud;

import com.opendomotic.model.entity.DeviceImage;
import com.opendomotic.service.dao.AbstractDAO;
import com.opendomotic.service.dao.CriteriaGetter;
import com.opendomotic.service.dao.DeviceImageDAO;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jaques
 */
@Named
@SessionScoped
public class ImageMB extends AbstractFileUpload<DeviceImage> {
    
    @Inject 
    private DeviceImageDAO dao;

    @Override
    protected CriteriaGetter.OrderGetter getOrderGetter() {
        return dao.getCriteriaGetter().getOrderAttributeAsc("name");
    }
    
    @Override
    public AbstractDAO<DeviceImage> getDAO() {
        return dao;
    }
    
    @Override
    public void save() {
        super.save();
        visible = entity.getFileName() == null;
    }
    
}
