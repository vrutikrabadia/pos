package com.increff.pos.dao;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.DaySalesPojo;

@Repository
public class DaySalesDao extends AbstractDao<DaySalesPojo>{

    public DaySalesDao() {
        super(DaySalesPojo.class);
    }
    
    public List<DaySalesPojo> selectInDateRange(ZonedDateTime startDate, ZonedDateTime endDate, Integer offset, Integer pageSize){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<DaySalesPojo> cq = cb.createQuery(DaySalesPojo.class);
        Root<DaySalesPojo> root = cq.from(DaySalesPojo.class);

        Predicate datePredicate = cb.between(root.get("date"), startDate, endDate);
        cq.where(datePredicate);
        TypedQuery<DaySalesPojo> query = em.createQuery(cq);

        return query.setFirstResult(offset).setMaxResults(pageSize).getResultList();
    }


    public Integer countTotalEntriesInDateRange(ZonedDateTime startDate, ZonedDateTime endDate){
		CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<DaySalesPojo> root = cq.from(DaySalesPojo.class);
    	cq.select(cb.count(root));
    	Predicate datePredicate = cb.between(root.get("date"), startDate, endDate);
        cq.where(datePredicate);
        
        TypedQuery<Long> query = em.createQuery(cq);

    	return  Math.toIntExact(getSingle(query));

    }
}
