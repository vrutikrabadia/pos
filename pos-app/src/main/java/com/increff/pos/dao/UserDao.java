package com.increff.pos.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.UserPojo;

@Repository
public class UserDao extends AbstractDao<UserPojo> {
	
    public UserDao() {
        super(UserPojo.class);
    }

	public Integer deleteById(Integer id) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaDelete<UserPojo> cq = cb.createCriteriaDelete(UserPojo.class);
		Root<UserPojo> root = cq.from(UserPojo.class);
		Predicate idPredicate = cb.equal(root.get("id"), id);

		cq.where(idPredicate);
		return em.createQuery(cq).executeUpdate();
	}


}
