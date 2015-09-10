/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.dao;

import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.entity.DeviceProperty;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jaques
 */
@Stateless
public class DevicePropertyDAO extends AbstractDAO<DeviceProperty> {

    @PersistenceContext
    private EntityManager em;
    
    public void deleteByConfig(DeviceConfig config) {
        em.createQuery("delete from DeviceProperty d where d.deviceConfig=?1")
                .setParameter(1, config)
                .executeUpdate();
    }
    
}
