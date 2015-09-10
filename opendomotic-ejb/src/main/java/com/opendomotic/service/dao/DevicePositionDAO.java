/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.dao;

import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.entity.DevicePosition;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jaques
 */
@Stateless
public class DevicePositionDAO extends AbstractDAO<DevicePosition> {

    @PersistenceContext
    private EntityManager em;
    
    @Inject 
    private EnvironmentDAO environmentService;
    
    @Inject 
    private DeviceConfigDAO configService;
    
    public void save(Integer idEnvironment, Integer idDeviceConfig) {
        DevicePosition position = new DevicePosition();
        position.setX(0);
        position.setY(0);
        position.setEnvironment(environmentService.findById(idEnvironment));
        position.setDeviceConfig(configService.findById(idDeviceConfig));
        em.persist(position);
    }
    
    public void delete(Integer idEnvironment, Integer idDeviceConfig) {
        em.createQuery("delete from DevicePosition p where p.environment.id = ?1 and p.deviceConfig.id = ?2")
                .setParameter(1, idEnvironment)
                .setParameter(2, idDeviceConfig)
                .executeUpdate();
    }
    
    public void deleteByIdEnvironment(Integer idEnvironment) {
        em.createQuery("delete from DevicePosition p where p.environment.id = ?1")
                .setParameter(1, idEnvironment)
                .executeUpdate();
    }
    
    public void deleteByConfig(DeviceConfig deviceConfig) {
        em.createQuery("delete from DevicePosition p where p.deviceConfig = ?1")
                .setParameter(1, deviceConfig)
                .executeUpdate();
    }
    
}
