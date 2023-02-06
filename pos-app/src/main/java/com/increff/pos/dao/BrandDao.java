package com.increff.pos.dao;


import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandPojo;

@Repository
public class BrandDao extends AbstractDao<BrandPojo>{

    public BrandDao() {
        super(BrandPojo.class);
    }

    public BrandPojo selectByBrandCategory(String brand, String category){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<BrandPojo> cq = cb.createQuery(BrandPojo.class);
        Root<BrandPojo> brandCat = cq.from(BrandPojo.class);

        Predicate bPredicate = cb.equal(brandCat.get("brand"), brand);
        Predicate cPredicate = cb.equal(brandCat.get("category"), category);
        cq.where(cb.and(bPredicate, cPredicate));
        TypedQuery<BrandPojo> query = em.createQuery(cq);

        return query.getResultList().stream().findFirst().orElse(null);
    }

    

}
