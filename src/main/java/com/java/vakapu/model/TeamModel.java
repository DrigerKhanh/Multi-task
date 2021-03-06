package com.java.vakapu.model;


import com.java.vakapu.entity.Team;

public class TeamModel {

	private int idTeam;

	private String name;

	private String description;

	private String owner;

	private int memberAmount;

	private int projectAmount;
	
	private String dateCreate;
	
	private String[] email;
	
	private String[] email2;

	public void fromTeam(Team team)
	{
		this.setName(team.getName());
		this.setDescription(team.getDescription());
		this.setIdTeam(team.getIdTeam());
		this.setOwner(team.getOwner());
		this.setMemberAmount(team.getMemberAmount());
		this.setProjectAmount(team.getProjectAmount());
		this.setDateCreate(team.getDateCreate());
	}
	
	public Team toTeam()
	{
		Team team = new Team();
		team.setIdTeam(this.getIdTeam());
		team.setDescription(this.getDescription());
		team.setName(this.getName());
		team.setMemberAmount(this.getMemberAmount());
		team.setProjectAmount(this.getProjectAmount());
		team.setOwner(this.getOwner());
		team.setDateCreate(this.getDateCreate());
		return team;
	}
	
	
	
	public String getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(String dateCreate) {
		this.dateCreate = dateCreate;
	}

	public String[] getEmail2() {
		return email2;
	}

	public void setEmail2(String[] email2) {
		this.email2 = email2;
	}

	public int getIdTeam() {
		return idTeam;
	}

	public void setIdTeam(int idTeam) {
		this.idTeam = idTeam;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getMemberAmount() {
		return memberAmount;
	}

	public void setMemberAmount(int memberAmount) {
		this.memberAmount = memberAmount;
	}

	public int getProjectAmount() {
		return projectAmount;
	}

	public void setProjectAmount(int projectAmount) {
		this.projectAmount = projectAmount;
	}

	public String[] getEmail() {
		return email;
	}

	public void setEmail(String[] email) {
		this.email = email;
	}
}
