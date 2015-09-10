package com.opendomotic.service.dao;

import com.opendomotic.model.entity.Environment;
import javax.ejb.Stateless;

/**
 *
 * @author jaques
 */
@Stateless
public class EnvironmentDAO extends AbstractDAO<Environment> {
    
    public Environment findByName(String name) {
        return findFirstByAttributeEqual("name", name);
    }
    
}
