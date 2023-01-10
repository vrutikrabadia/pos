package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderItemsPojo;

@Repository
public class OrderItemsDao extends AbstractDao{
    
    @PersistenceContext
    private EntityManager em;

    public OrderItemsPojo selectById(int id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderItemsPojo> cq = cb.createQuery(OrderItemsPojo.class);
        Root<OrderItemsPojo> productRoot = cq.from(OrderItemsPojo.class);
        Predicate idPredicate = cb.equal(productRoot.get("id"), id);
        cq.where(idPredicate);

        TypedQuery<OrderItemsPojo> query = em.createQuery(cq);

        return getSingle(query);
    }

    public List<OrderItemsPojo> selectByOrderId(int orderId){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderItemsPojo> cq = cb.createQuery(OrderItemsPojo.class);
        Root<OrderItemsPojo> productRoot = cq.from(OrderItemsPojo.class);
        Predicate orderIdPredicate = cb.equal(productRoot.get("orderId"), orderId);
        cq.where(orderIdPredicate);

        TypedQuery<OrderItemsPojo> query = em.createQuery(cq);

        return query.getResultList();
    }

    public List<OrderItemsPojo> selectAll(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderItemsPojo> cq = cb.createQuery(OrderItemsPojo.class);
        Root<OrderItemsPojo> productRoot = cq.from(OrderItemsPojo.class);
        CriteriaQuery<OrderItemsPojo> all = cq.select(productRoot);
        TypedQuery<OrderItemsPojo> allQuery = em.createQuery(all);

        return allQuery.getResultList();
    }


    
}
