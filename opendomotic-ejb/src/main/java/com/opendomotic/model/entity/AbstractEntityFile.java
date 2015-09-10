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
public class AbstractEntityFile extends AbstractEntityName {
    
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
}
