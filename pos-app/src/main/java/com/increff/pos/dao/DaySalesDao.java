package com.increff.pos.dao;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.PosDaySales;

@Repository
public class DaySalesDao extends AbstractDao<PosDaySales>{

    public DaySalesDao() {
        super(PosDaySales.class);
    }
    
    public List<PosDaySales> selectInDateRange(ZonedDateTime startDate, ZonedDateTime endDate, Integer offset, Integer pageSize){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<PosDaySales> cq = cb.createQuery(PosDaySales.class);
        Root<PosDaySales> root = cq.from(PosDaySales.class);

        Predicate datePredicate = cb.between(root.get("date"), startDate, endDate);
        cq.where(datePredicate);
        TypedQuery<PosDaySales> query = em.createQuery(cq);

        return query.setFirstResult(offset).setMaxResults(pageSize).getResultList();
    }


    public Integer selectTotalEntriesInDateRange(ZonedDateTime startDate, ZonedDateTime endDate){
		CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<PosDaySales> root = cq.from(PosDaySales.class);
    	cq.select(cb.count(root));
    	Predicate datePredicate = cb.between(root.get("date"), startDate, endDate);
        cq.where(datePredicate);
        
        TypedQuery<Long> query = em.createQuery(cq);

    	return  Math.toIntExact(getSingle(query));

    }
}
