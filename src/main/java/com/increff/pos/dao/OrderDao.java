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

import com.increff.pos.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao{
    
    @PersistenceContext
    private EntityManager em;

    public Integer insert(OrderPojo p){
        em.persist(p);
        return p.getId();
    }

    public List<OrderPojo> selectAll(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderPojo> cq = cb.createQuery(OrderPojo.class);
        Root<OrderPojo> productRoot = cq.from(OrderPojo.class);
        CriteriaQuery<OrderPojo> all = cq.select(productRoot);
        TypedQuery<OrderPojo> allQuery = em.createQuery(all);

        return allQuery.getResultList();
    }

    public OrderPojo selectById(Integer id){
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderPojo> cq = cb.createQuery(OrderPojo.class);
        Root<OrderPojo> productRoot = cq.from(OrderPojo.class);
        Predicate idPredicate = cb.equal(productRoot.get("id"), id);
        cq.where(idPredicate);

        TypedQuery<OrderPojo> query = em.createQuery(cq);

        return getSingle(query);
    }
}
