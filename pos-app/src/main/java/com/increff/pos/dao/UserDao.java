package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.UserPojo;

@Repository
public class UserDao extends AbstractDao<UserPojo> {
	
    public UserDao() {
        super(UserPojo.class);
    }

	public Integer delete(Integer id) {
		CriteriaBuilder cb = em().getCriteriaBuilder();

        CriteriaDelete<UserPojo> cq = cb.createCriteriaDelete(UserPojo.class);
		Root<UserPojo> user = cq.from(UserPojo.class);
		Predicate idPredicate = cb.equal(user.get("id"), id);

		cq.where(idPredicate);
		return em().createQuery(cq).executeUpdate();
	}

	public UserPojo select(Integer id) {
		CriteriaBuilder cb = em().getCriteriaBuilder();

        CriteriaQuery<UserPojo> cq = cb.createQuery(UserPojo.class);
        Root<UserPojo> user = cq.from(UserPojo.class);
        Predicate idPredicate = cb.equal(user.get("id"), id);
        cq.where(idPredicate);

        TypedQuery<UserPojo> query = em().createQuery(cq);
        return getSingle(query);

	}

	public UserPojo select(String email) {
		CriteriaBuilder cb = em().getCriteriaBuilder();

        CriteriaQuery<UserPojo> cq = cb.createQuery(UserPojo.class);
        Root<UserPojo> user = cq.from(UserPojo.class);
        Predicate emailPredicate = cb.equal(user.get("email"), email);
        cq.where(emailPredicate);

        TypedQuery<UserPojo> query = em().createQuery(cq);
        return getSingle(query);
	}

	public List<UserPojo> selectAll() {
		CriteriaBuilder cb = em().getCriteriaBuilder();
        CriteriaQuery<UserPojo> cq = cb.createQuery(UserPojo.class);
        Root<UserPojo> user = cq.from(UserPojo.class);
        CriteriaQuery<UserPojo> all = cq.select(user);
        TypedQuery<UserPojo> allQuery = em().createQuery(all);
        return allQuery.getResultList();
	}

	public void update(UserPojo p) {
	}


}
