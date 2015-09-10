package com.opendomotic.mb.crud;

import com.opendomotic.model.entity.AbstractEntityId;
import com.opendomotic.service.dao.AbstractDAO;
import com.opendomotic.service.dao.CriteriaGetter.OrderGetter;
import com.opendomotic.service.dao.CriteriaGetter.PredicateGetter;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author jaques
 * @param <T>
 */
public abstract class AbstractCRUD<T extends AbstractEntityId> implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(AbstractCRUD.class.getName());
    
    protected T entity;    
    private List<T> listAll;
    protected boolean visible = false;
 
    public abstract AbstractDAO<T> getDAO();
    
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public T getEntity() {
        if (entity == null) {
            entity = getDAO().createEntity();
        }
        return entity;
    }
    
    public void create() {
        edit(getDAO().createEntity());
    }
    
    public void save() {
        entity = getDAO().save(entity);
        listAll = null;
        visible = false;
    }
    
    public void cancel() {
        visible = false;
    }
    
    public void delete(T entity) {
        getDAO().delete(entity);
        listAll = null;
    }
    
    public void edit(T entity) {
        LOG.info(entity.toString());
        this.entity = entity;
        this.visible = true;
    }
    
    public void clearList() {
        listAll = null;
    }
    
    public List<T> getListAll() {
        if (listAll == null) {
            listAll = getDAO().findAll(getPredicateGetter(), getOrderGetter());
        }
        return listAll;
    }

    protected PredicateGetter getPredicateGetter() {
        return null; //reimplementar nas subclasses. Prefira implementar o PredicateGetter dentro do DAO.
    }
    
    protected OrderGetter getOrderGetter() {
        return null; //reimplementar nas subclasses. Ex: return new String[] {"id"};
    }
    
    //chamado ao fechar o dialog:
    public void handleClose(CloseEvent event) {  
        visible = false; 
    }
    
}
