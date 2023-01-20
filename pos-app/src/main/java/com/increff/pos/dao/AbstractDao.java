package com.increff.pos.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public abstract class AbstractDao<T> {

	@PersistenceContext
	protected EntityManager em;

	protected <R> R getSingle(TypedQuery<R> query) {
		return query.getResultList().stream().findFirst().orElse(null);
	}

	protected TypedQuery<T> getQuery(String jpql, Class<T> clazz) {
		return em.createQuery(jpql, clazz);
	}

	protected EntityManager em() {
		return em;
	}

	public T insert(T p) {
		em.persist(p);
		return p;
	}

	public void update(T p) {

	}

	public List<T> selectAllPaginated(Integer offset, Integer pageSize, Class<T> pojoClass) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(pojoClass);
		Root<T> root = cq.from(pojoClass);
		CriteriaQuery<T> all = cq.select(root);
		TypedQuery<T> allQuery = em.createQuery(all);
		return allQuery.setFirstResult(offset).setMaxResults(pageSize).getResultList();
	}

	public List<T> selectAll(Class<T> pojoClass) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(pojoClass);
		Root<T> root = cq.from(pojoClass);
		CriteriaQuery<T> all = cq.select(root);
		TypedQuery<T> allQuery = em.createQuery(all);
		return allQuery.getResultList();
	}

	public Integer getTotalEntries(Class<T> pojoClass) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		criteria.select(builder.count(criteria.from(pojoClass)));
		TypedQuery<Long> query = em.createQuery(criteria);

		return Math.toIntExact(getSingle(query));

	}

	public List<T> selectQueryString(Integer offset, Integer pageSize, String queryString, List<String> columns,
			Class<T> pojoClass) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<T> cq = cb.createQuery(pojoClass);
		Root<T> brandCat = cq.from(pojoClass);

		List<Predicate> preds = new ArrayList<>();
		for (String col : columns) {
			preds.add(cb.like(brandCat.get(col), queryString + "%"));
		}
		cq.where(cb.or(preds.toArray(new Predicate[] {})));
		TypedQuery<T> query = em.createQuery(cq);

		return query.setFirstResult(offset).setMaxResults(pageSize).getResultList();
	}

	public <R> List<T> selectMultiple(String column, R value, Class<T> pojoClass) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(pojoClass);
		Root<T> root = cq.from(pojoClass);
		cq.where(cb.equal(root.get(column), value));
		TypedQuery<T> query = em.createQuery(cq);
		return query.getResultList();
	}

	public <R> T selectByColumn(String column, R value, Class<T> pojoClass) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(pojoClass);
		Root<T> root = cq.from(pojoClass);
		cq.where(cb.equal(root.get(column), value));
		TypedQuery<T> query = em.createQuery(cq);
		return getSingle(query);
	}

	public <R> List<T> selectByColumnUsingIn(List<String> columns, List<List<R>> values, Class<T> pojoClass) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(pojoClass);
		Root<T> root = cq.from(pojoClass);

		List<Predicate> preds = new ArrayList<>();
		for(int i=0; i<columns.size(); i++){
			In<R> inClause = cb.in(root.get(columns.get(i)));
			for (R val : values.get(i)) {
				inClause.value(val);
			}
			preds.add(inClause);
		}
		cq.where(cb.and(preds.toArray(new Predicate[] {})));
		TypedQuery<T> query = em.createQuery(cq);
		return query.getResultList();
	}

}
