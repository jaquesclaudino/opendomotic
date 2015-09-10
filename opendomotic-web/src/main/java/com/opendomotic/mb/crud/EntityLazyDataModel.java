package com.opendomotic.mb.crud;

import com.opendomotic.model.entity.AbstractEntityId;
import com.opendomotic.service.dao.AbstractDAO;
import com.opendomotic.service.dao.CriteriaGetter.OrderGetter;
import com.opendomotic.service.dao.CriteriaGetter.PredicateGetter;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author jaques
 * @param <T>
 */
public class EntityLazyDataModel<T extends AbstractEntityId> extends LazyDataModel<T> implements SelectableDataModel<T>, Serializable {
    
    private static final Logger LOG = Logger.getLogger(EntityLazyDataModel.class.getName());
    
    private final AbstractDAO<T> dao;
    private final PredicateGetter predicateGetter;
    private final OrderGetter orderGetter;
    
    public EntityLazyDataModel(AbstractDAO<T> dao, PredicateGetter predicateGetter, OrderGetter orderGetter) {
        this.dao = dao;
        this.predicateGetter = predicateGetter;
        this.orderGetter = orderGetter;
        this.setRowCount(dao.findCount(predicateGetter).intValue());
    }
        
    @Override
    public Object getRowKey(T entity) {
        return entity.getId();
    }

    @Override
    public T getRowData(String rowKey) {
        return dao.findById(Integer.parseInt(rowKey));
    }
    
    @Override  
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {  
        LOG.log(Level.INFO, "first={0} | pageSize={1}", new Object[] {first, pageSize});
        return dao.findAll(predicateGetter, orderGetter, first, pageSize);
    }
    
}
