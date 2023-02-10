package com.increff.pos.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public abstract class AbstractDao<T> {

	private Class<T> pojoClass;
	private CriteriaBuilder cb;

	AbstractDao(Class<T> pClass){
		pojoClass = pClass;
	}

	@PersistenceContext
	protected EntityManager em;

	@PostConstruct
	public void init() {
		cb = em.getCriteriaBuilder();
	}

	// TODO: AFTER VERIFICATION: throw exception if more than one entry
	protected <R> R getSingle(TypedQuery<R> query){
		return query.getResultList().stream().findFirst().orElse(null);
	}

	public T insert(T p) {
		em.persist(p);
		return p;
	}

	public void update(T p) {

	}

	public List<T> selectAllPaginated(Integer offset, Integer pageSize) {

		
		CriteriaQuery<T> cq = cb.createQuery(pojoClass);
		Root<T> root = cq.from(pojoClass);
		CriteriaQuery<T> all = cq.select(root);
		TypedQuery<T> query = em.createQuery(all);
		return query.setFirstResult(offset).setMaxResults(pageSize).getResultList();
	}

	public List<T> selectAll() {

		
		CriteriaQuery<T> cq = cb.createQuery(pojoClass);
		Root<T> root = cq.from(pojoClass);
		CriteriaQuery<T> all = cq.select(root);
		TypedQuery<T> query = em.createQuery(all);
		return query.getResultList();
	}

	public Integer countTotalEntries()  {

		CriteriaQuery<Long> cqLongClass = cb.createQuery(Long.class);
		cqLongClass.select(cb.count(cqLongClass.from(pojoClass)));
		TypedQuery<Long> query = em.createQuery(cqLongClass);

		return Math.toIntExact(getSingle(query));

	}

	public List<T> selectByQueryString(Integer offset, Integer pageSize, String queryString, List<String> columns) {
		

		CriteriaQuery<T> cq = cb.createQuery(pojoClass);
		Root<T> root = cq.from(pojoClass);

		List<Predicate> preds = new ArrayList<>();
		for (String col : columns) {
			preds.add(cb.like(root.get(col), queryString + "%"));
		}
		cq.where(cb.or(preds.toArray(new Predicate[] {})));
		TypedQuery<T> query = em.createQuery(cq);

		return query.setFirstResult(offset).setMaxResults(pageSize).getResultList();
	}

	public <R> List<T> selectMultipleEntriesByColumn(String column, R value) {
		
		CriteriaQuery<T> cq = cb.createQuery(pojoClass);
		Root<T> root = cq.from(pojoClass);
		cq.where(cb.equal(root.get(column), value));
		TypedQuery<T> query = em.createQuery(cq);
		return query.getResultList();
	}

	public <R> T selectByColumn(String column, R value) {
		
		CriteriaQuery<T> cq = cb.createQuery(pojoClass);
		Root<T> root = cq.from(pojoClass);
		cq.where(cb.equal(root.get(column), value));
		TypedQuery<T> query = em.createQuery(cq);
		return getSingle(query);
	}

	// TODO: AFTER VERIFICATION: validate for empty list in service
	public <R> List<T> selectInColumns(List<String> columnList, List<List<R>> valueList) {

		CriteriaQuery<T> cq = cb.createQuery(pojoClass);
		Root<T> root = cq.from(pojoClass);

		List<Predicate> preds = new ArrayList<>();
		for(int i=0; i<columnList.size(); i++){
			In<R> inClause = cb.in(root.get(columnList.get(i)));
			for (R val : valueList.get(i)) {
				inClause.value(val);
			}
			preds.add(inClause);
		}
		cq.where(cb.and(preds.toArray(new Predicate[] {})));
		TypedQuery<T> query = em.createQuery(cq);
		return query.getResultList();
	}

}
