package com.increff.pos.dao;

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

    public void insert(ProductPojo p){
        em.persist(p);
    }

    public ProductPojo selectById(int id){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<ProductPojo> cq = cb.createQuery(ProductPojo.class);
        Root<ProductPojo> brandCat = cq.from(ProductPojo.class);
        Predicate idPredicate = cb.equal(brandCat.get("id"), id);
        cq.where(idPredicate);

        TypedQuery<ProductPojo> query = em.createQuery(cq);
        return getSingle(query);
    }

    public List<ProductPojo> selectAll(){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductPojo> cq = cb.createQuery(ProductPojo.class);
        Root<ProductPojo> productRoot = cq.from(ProductPojo.class);
        CriteriaQuery<ProductPojo> all = cq.select(productRoot);
        TypedQuery<ProductPojo> allQuery = em.createQuery(all);

        return allQuery.getResultList();
    }

    public ProductPojo selectByBarCode(String barCode){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<ProductPojo> cq = cb.createQuery(ProductPojo.class);
        Root<ProductPojo> brandCat = cq.from(ProductPojo.class);
        Predicate barCodePredicate = cb.equal(brandCat.get("barCode"), barCode);
        cq.where(barCodePredicate);

        TypedQuery<ProductPojo> query = em.createQuery(cq);
        return getSingle(query);
    }

    public void update(ProductPojo p){

    }
}
