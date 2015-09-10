/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.converter;

import com.opendomotic.model.entity.AbstractEntityId;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jaques
 */
@Named
@ApplicationScoped
public class EntityConverter implements Converter {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.isEmpty() && value.contains("|")) {
            try {
                String key[] = value.split("\\|");
                Integer id = Integer.parseInt(key[0]);
                Class clazz = Class.forName(key[1]);
                return em.find(clazz, id);                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(EntityConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent component, Object value) {
        if (value != null && value instanceof AbstractEntityId) {
            AbstractEntityId entity = (AbstractEntityId) value;
            return entity.getId() + "|" + value.getClass().getName();
        }
        return null;
    }

}
