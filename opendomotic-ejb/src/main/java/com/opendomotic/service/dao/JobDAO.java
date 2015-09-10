package com.opendomotic.service.dao;

import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.entity.Job;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author jaques
 */
@Stateless
public class JobDAO extends AbstractDAO<Job> {
    
    public List<Job> findAllEnabled() {
        return getEntityManager()
                .createQuery("select j from Job j where j.enabled = true order by j.index")
                .getResultList();
    }
    
    public void deleteByConfig(DeviceConfig deviceConfig) {
        getEntityManager()
                .createQuery("delete from Job j where j.input=?1 or j.output=?1")
                .setParameter(1, deviceConfig)
                .executeUpdate();
    }
    
}
