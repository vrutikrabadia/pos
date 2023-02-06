package com.increff.pos.dao;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao<OrderPojo>{
    
    public OrderDao() {
        super(OrderPojo.class);
    }

    public List<OrderPojo> selectInDateRange(ZonedDateTime startDate, ZonedDateTime endDate){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<OrderPojo> cq = cb.createQuery(OrderPojo.class);
        Root<OrderPojo> root = cq.from(OrderPojo.class);

        Predicate datePredicate = cb.between(root.get("updated"), startDate, endDate);
        cq.where(datePredicate);
        TypedQuery<OrderPojo> query = em.createQuery(cq);

        return query.getResultList();
    }
}
