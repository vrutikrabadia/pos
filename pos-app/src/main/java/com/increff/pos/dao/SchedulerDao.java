package com.increff.pos.dao;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.SchedulerPojo;

@Repository
public class SchedulerDao extends AbstractDao<SchedulerPojo>{
    
    public List<SchedulerPojo> selectInDateRange(ZonedDateTime startDate, ZonedDateTime endDate, Integer offset, Integer pageSize){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<SchedulerPojo> cq = cb.createQuery(SchedulerPojo.class);
        Root<SchedulerPojo> root = cq.from(SchedulerPojo.class);

        Predicate datePredicate = cb.between(root.get("date"), startDate, endDate);
        cq.where(datePredicate);
        TypedQuery<SchedulerPojo> query = em.createQuery(cq);

        return query.setFirstResult(offset).setMaxResults(pageSize).getResultList();
    }

    public SchedulerPojo selectByDate(ZonedDateTime date){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<SchedulerPojo> cq = cb.createQuery(SchedulerPojo.class);
        Root<SchedulerPojo> root = cq.from(SchedulerPojo.class);

        Predicate datePredicate = cb.equal(root.get("date"), date );
        cq.where(datePredicate);
        TypedQuery<SchedulerPojo> query = em.createQuery(cq);

        return getSingle(query);
    }

    public Integer getTotalEntriesInDateRange(ZonedDateTime startDate, ZonedDateTime endDate){

		CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<SchedulerPojo> root = cq.from(SchedulerPojo.class);
    	cq.select(cb.count(root));
    	Predicate datePredicate = cb.between(root.get("date"), startDate, endDate);
        cq.where(datePredicate);
        
        TypedQuery<Long> query = em.createQuery(cq);

    	return  Math.toIntExact(query.getFirstResult());

    }
}
