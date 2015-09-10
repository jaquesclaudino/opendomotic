package com.opendomotic.service.dao;

import com.opendomotic.service.dao.CriteriaGetter.OrderGetter;
import com.opendomotic.service.dao.CriteriaGetter.PredicateGetter;
import com.opendomotic.service.dao.CriteriaGetter.SelectionGetter;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author jaques
 * @param <T>
 */
public abstract class AbstractDAO<T> {

    private static final Logger LOG = Logger.getLogger(AbstractDAO.class.getName());

    @PersistenceContext
    private EntityManager em;

    private final CriteriaGetter<T> criteriaGetter = new CriteriaGetter<>();
    
    public Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public CriteriaGetter<T> getCriteriaGetter() {
        return criteriaGetter;
    }

    public T createEntity() {
        try {
            return getEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.severe(ex.toString());
            return null;
        }
    }
    
    public T save(T entity) {
        return em.merge(entity);
    }

    public void delete(T entity) {
        em.remove(em.merge(entity));
    }
    
    /**
     * 
     * @param <F>
     * Field type
     * @param selectionGetter
     * @param predicateGetter
     * @param orderGetter
     * @param fieldClass
     * @return 
     */
    //TODO: É possível extrair fieldClass de <F> ?
    protected <F> CriteriaQuery<F> createCriteriaQuery(SelectionGetter selectionGetter, PredicateGetter predicateGetter, OrderGetter orderGetter, Class fieldClass) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<F> query = builder.createQuery(fieldClass);
        Root<T> root = query.from(getEntityClass());
        
        query.select(selectionGetter.getSelection(query, builder, root));

        if (predicateGetter != null) {
            query.where(predicateGetter.getPredicate(query, builder, root));
        }

        if (orderGetter != null) {
            query.orderBy(orderGetter.getListOrder(query, builder, root));
        }
        
        return query;
    }
    
    protected CriteriaQuery<T> createCriteriaQuery(SelectionGetter selectionGetter, PredicateGetter predicateGetter, OrderGetter orderGetter) {
        return createCriteriaQuery(selectionGetter, predicateGetter, orderGetter, getEntityClass());
    }
    
    protected CriteriaQuery<T> createCriteriaQuery(PredicateGetter predicateGetter, OrderGetter orderGetter) {
        return createCriteriaQuery(criteriaGetter.getSelectionEntity(), predicateGetter, orderGetter);
    }
        
    //---------- find list
        
    public List<T> findAll(PredicateGetter predicateGetter, OrderGetter orderGetter, int firstResult, int maxResults) {
        Query query =  em.createQuery(createCriteriaQuery(predicateGetter, orderGetter));
        if (firstResult > 0) {
            query.setFirstResult(firstResult);
        }        
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }        
        return query.getResultList();
    }
    
    public List<T> findAll(PredicateGetter predicateGetter, OrderGetter orderGetter) {
        return findAll(predicateGetter, orderGetter, 0, 0);
    }

    public List<T> findAll(PredicateGetter predicateGetter) {
        return findAll(predicateGetter, null);
    }
    
    public List<T> findAll() {
        return findAll(null, null);
    }
    
    public List<T> findAllByLike(String attributeName, String queryString) {
        return findAll(criteriaGetter.getPredicateAttributeLike(attributeName, queryString));
    }   
    
    public List<T> findAllByAttributeEqual(String attributeName, Object value) {
        return findAll(criteriaGetter.getPredicateAttributeEqual(attributeName, value));
    }

    //---------- find single
    
    public <F> F findField(SelectionGetter selectionGetter, PredicateGetter predicateGetter, Class fieldClass) {
        CriteriaQuery<F> query = createCriteriaQuery(selectionGetter, predicateGetter, null, fieldClass);
        return em.createQuery(query).getSingleResult();
    }
    
    public Long findCount(PredicateGetter predicateGetter) {
        return findField(criteriaGetter.getSelectionEntityCount(), predicateGetter, Long.class);
    }
    
    public T findById(Object id) {
        return em.find(getEntityClass(), id);
    }

    public T findFirstByAttributeEqual(final String attributeName, final Object value) {
        return findFirst(criteriaGetter.getPredicateAttributeEqual(attributeName, value), null);
    }
    
    public T findFirst(PredicateGetter predicateGetter, OrderGetter orderGetter) {
        try {
            return em
                    .createQuery(createCriteriaQuery(predicateGetter, orderGetter))
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            LOG.warning(e.toString());
            return null;
        }
    }
    
    public T findFirst() {
        return findFirst(null, null);
    }   

}
