package com.java.vakapu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="todo")
public class Todo {
	
	@Id
	@Column(name="idtodo")
	private int id;
	
	@Column(name="todo")
	private String todo;
	
	@Column(name="completed")
	private int completed;
	
	@Column(name="note")
	private String note;
	
	@Column(name="titlenote")
	private String titleNote;
	
	@ManyToOne
	@JoinColumn(name="task")
	private Task task;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTodo() {
		return todo;
	}

	public void setTodo(String todo) {
		this.todo = todo;
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getTitleNote() {
		return titleNote;
	}

	public void setTitleNote(String titleNote) {
		this.titleNote = titleNote;
	}
	
	

}