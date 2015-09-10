/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

import javax.persistence.MappedSuperclass;

/**
 *
 * @author jaques
 */
@MappedSuperclass
public abstract class AbstractEntityName extends AbstractEntityId {
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
