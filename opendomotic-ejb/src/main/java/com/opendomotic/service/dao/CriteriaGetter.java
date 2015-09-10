package com.opendomotic.service.dao;

import java.util.Arrays;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

/**
 *
 * @author jaques
 * @param <T>
 */
public class CriteriaGetter<T> {
    
    public interface SelectionGetter<T> {
        Selection getSelection(CriteriaQuery<T> query, CriteriaBuilder builder, Root<T> root);
    }
    
    public interface PredicateGetter<T> {
        Predicate getPredicate(CriteriaQuery<T> query, CriteriaBuilder builder, Root<T> root);
    }
    
    public interface OrderGetter<T> {
        List<Order> getListOrder(CriteriaQuery<T> query, CriteriaBuilder builder, Root<T> root);
    }
    
    //---------- selection
    
    public SelectionGetter getSelectionEntity() {
        return new SelectionGetter() {
            @Override
            public Selection getSelection(CriteriaQuery query, CriteriaBuilder builder, Root root) {
                return root;
            }
        };
    }
    
    public SelectionGetter getSelectionEntityCount() {
        return new SelectionGetter() {
            @Override
            public Selection getSelection(CriteriaQuery query, CriteriaBuilder builder, Root root) {
                return builder.count(root);
            }
        };
    }
    
    //---------- predicate
    
    public PredicateGetter getPredicateAttributeEqual(final String attributeName, final Object value) {
        return new PredicateGetter() {
            @Override
            public Predicate getPredicate(CriteriaQuery query, CriteriaBuilder builder, Root root) {
                return builder.equal(root.get(attributeName), value);
            }
        };
    }
    
    public PredicateGetter getPredicateAttributeLike(final String attributeName, final String queryString) {
        return new PredicateGetter() {
            @Override
            public Predicate getPredicate(CriteriaQuery query, CriteriaBuilder builder, Root root) {
                return builder.like(builder.lower(root.get(attributeName)), "%" + queryString.toLowerCase() + "%");
            }
        };
    }
    
    public Predicate orNotNull(CriteriaBuilder builder, Predicate predicate1, Predicate predicate2) {
        if (predicate1 != null && predicate2 != null) {
            return builder.or(predicate1, predicate2);
        } else if (predicate1 != null) {
            return predicate1;
        } else if (predicate2 != null) {
            return predicate2;
        }
        return null;
    }
    
    //---------- order
    
    public OrderGetter getOrderAttributeAsc(final String attributeName) {
        return new OrderGetter() {
            @Override
            public List getListOrder(CriteriaQuery query, CriteriaBuilder builder, Root root) {          
                return Arrays.asList(builder.asc(root.get(attributeName)));
            }
        };
    }
    
}
