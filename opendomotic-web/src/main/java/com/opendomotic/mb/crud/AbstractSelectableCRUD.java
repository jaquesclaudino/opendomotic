package com.opendomotic.mb.crud;

import com.opendomotic.model.entity.AbstractEntityId;
import javax.faces.model.DataModel;

/**
 *
 * @author jaques
 * @param <T>
 */
public abstract class AbstractSelectableCRUD<T extends AbstractEntityId> extends AbstractCRUD<T> {
    
    private T selected;
    protected DataModel<T> dataModel;
    
    @Override
    public void save() { 
        super.save();  
        clearList(); 
    }
    
    @Override
    public void clearList() {
        super.clearList(); 
        dataModel = null;
        selected = null;
    }
    
    public void editSelected() {
        edit(selected);
    }
    
    public void deleteSelected() {
        delete(selected);
        clearList();
    }
    
    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }
    
    public DataModel<T> getDataModel() {        
        if (dataModel == null) {
            dataModel = new EntityLazyDataModel<>(getDAO(), getPredicateGetter(), getOrderGetter());
        }
        return dataModel;
    }   
    
}
