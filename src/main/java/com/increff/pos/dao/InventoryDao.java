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

import com.increff.pos.pojo.InventoryPojo;

@Repository
public class InventoryDao extends AbstractDao{
    
    @PersistenceContext
    private EntityManager em;

    public List<InventoryPojo> selectAll(){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InventoryPojo> cq = cb.createQuery(InventoryPojo.class);
        Root<InventoryPojo> productRoot = cq.from(InventoryPojo.class);
        CriteriaQuery<InventoryPojo> all = cq.select(productRoot);
        TypedQuery<InventoryPojo> allQuery = em.createQuery(all);

        return allQuery.getResultList();
    }

    public InventoryPojo selectById(int id){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InventoryPojo> cq = cb.createQuery(InventoryPojo.class);
        Root<InventoryPojo> productRoot = cq.from(InventoryPojo.class);
        Predicate idPredicate = cb.equal(productRoot.get("id"), id);
        cq.where(idPredicate);

        TypedQuery<InventoryPojo> query = em.createQuery(cq);

        return getSingle(query);
    }

}
