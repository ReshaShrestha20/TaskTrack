package com.tasktrack.model;

import java.time.LocalDate;

public class ProjectModel {
	private int id;
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public ProjectModel() {
		super();
	}

	public ProjectModel(int id, String name, LocalDate startDate, LocalDate endDate) {
		super();
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	

	public ProjectModel(String name) {
		super();
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	
}
