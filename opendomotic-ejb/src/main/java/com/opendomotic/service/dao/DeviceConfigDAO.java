package com.opendomotic.service.dao;

import com.opendomotic.model.entity.DeviceConfig;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jaques
 */
@Stateless
public class DeviceConfigDAO extends AbstractDAO<DeviceConfig> {
    
    @PersistenceContext
    private EntityManager em;
    
    public DeviceConfig findByName(String name) {
        return findFirstByAttributeEqual("name", name);
    }
    
    public List<DeviceConfig> findAllByNameLike(String name) {
        return em
                .createQuery("select c from DeviceConfig c where c.name like ?1")
                .setParameter(1, "%"+name+"%")
                .getResultList();
    }
    
    public List<DeviceConfig> findAllEnabled() {
        return em
                .createQuery("select c from DeviceConfig c where c.enabled = true order by c.threadId, c.name")
                .getResultList();
    }
    
    public List<DeviceConfig> findAllOrderByName() {
        return em
                .createQuery("select c from DeviceConfig c order by c.name")
                .getResultList();
    }
    
    public List<DeviceConfig> findAllEnabledWithHistory() {
        return em
                .createQuery("select c from DeviceConfig c where c.enabled = true and c.history = true order by c.name")
                .getResultList();
    }
    
}
