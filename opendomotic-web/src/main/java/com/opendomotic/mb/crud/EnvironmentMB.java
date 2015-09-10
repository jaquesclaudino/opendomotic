/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb.crud;

import com.opendomotic.model.entity.Environment;
import com.opendomotic.service.dao.AbstractDAO;
import com.opendomotic.service.dao.EnvironmentDAO;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Jaques
 */
@Named
@SessionScoped
public class EnvironmentMB extends AbstractFileUpload<Environment> {
    
    @Inject
    private EnvironmentDAO dao;

    @Override
    public AbstractDAO<Environment> getDAO() {
        return dao;
    }

    @Override
    public void save() {
        super.save(); 
        visible = entity.getFileName() == null;
    }
    
}
