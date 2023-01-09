package com.increff.pos.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
// import javax.persistence.Query;
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


    public List<ProductPojo> selectAll(){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductPojo> cq = cb.createQuery(ProductPojo.class);
        Root<ProductPojo> productRoot = cq.from(ProductPojo.class);
        CriteriaQuery<ProductPojo> all = cq.select(productRoot);
        TypedQuery<ProductPojo> allQuery = em.createQuery(all);

        return allQuery.getResultList();
    }

    public List<ProductPojo> select(ProductPojo p){

        List<Predicate> preds = new ArrayList<Predicate>();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductPojo> cq = cb.createQuery(ProductPojo.class);
        Root<ProductPojo> productRoot = cq.from(ProductPojo.class);

        String barCode = p.getBarCode();
        int id = p.getId();

        if(p!=null){
            preds.add(cb.equal(productRoot.get("barCode"), barCode));
        }

        if(id!=0){
            preds.add( cb.equal(productRoot.get("id"), id));
        }

        for (Predicate pred : preds) {
            cq.where(pred);
        }

        TypedQuery<ProductPojo> query = em.createQuery(cq);

        return query.getResultList();
    }

    public void update(ProductPojo p){

    }
}
