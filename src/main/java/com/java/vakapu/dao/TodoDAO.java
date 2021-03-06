package com.java.vakapu.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.java.vakapu.entity.Todo;

@Repository
public class TodoDAO {

	@Autowired
	public SessionFactory sessionFactory;
	
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public List<Todo> findAll(){
		TypedQuery<Todo> query = getSession().createQuery("FROM Todo", Todo.class);
		List<Todo> contact = query.getResultList();
		return contact;
	}
	public List<Todo> findByIdTask(int id) {
		TypedQuery<Todo> query = getSession().createQuery("select a from Todo a where a.taskTeamProject.id=:id",Todo.class);
		query.setParameter("id", id);
		return query.getResultList();
	}
	
	public long findTodoDoneByIdTask(int id) {
		Query<Long> query = getSession().createQuery("select count(*) from Todo a where a.taskTeamProject.id=:id and a.completed like '1'",Long.class);
		query.setParameter("id", id);
		return query.uniqueResult();
	}
	public Todo find(int id) {
		return getSession().find(Todo.class, id);
	}
	
	public Todo create(Todo Todo) {
		getSession().save(Todo);
		return Todo;
	}
	
	public Todo delete(Todo Todo) {
		getSession().delete(Todo);
		return Todo;
	}
	
	public Todo update(Todo Todo) {
		getSession().update(Todo);
		return Todo;
	}
}
