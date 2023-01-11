package com.increff.pos.dao;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao{
    

    @PersistenceContext
    private EntityManager em;


    public ProductPojo selectById(Integer id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductPojo> cq = cb.createQuery(ProductPojo.class);
        Root<ProductPojo> productRoot = cq.from(ProductPojo.class);

        Predicate iPredicate = cb.equal(productRoot.get("id"), id);
        cq.where(iPredicate);

        TypedQuery<ProductPojo> query = em.createQuery(cq);

        return getSingle(query);
    }

    public ProductPojo selectByBarcode(String barcode){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductPojo> cq = cb.createQuery(ProductPojo.class);
        Root<ProductPojo> productRoot = cq.from(ProductPojo.class);

        Predicate iPredicate = cb.equal(productRoot.get("barcode"), barcode);
        cq.where(iPredicate);

        TypedQuery<ProductPojo> query = em.createQuery(cq);

        return getSingle(query);
    }


}
