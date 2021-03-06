package com.java.vakapu.services;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.vakapu.dao.TeamMemberTeamProjectDAO;
import com.java.vakapu.entity.TeamMemberTeamProject;

@Service
@Transactional
public class TeamMemberTeamProjectServices {

	@Autowired
	private TeamMemberTeamProjectDAO teamMemberTeamProjectDAO;
	
	public List<TeamMemberTeamProject> findByIdProject(int id)
	{
		return teamMemberTeamProjectDAO.findByIdProject(id);
	}
	
	public TeamMemberTeamProject findByEmailUser(String email, int id) {
		return teamMemberTeamProjectDAO.findByEmailUser(email, id);
	}
	
	public TeamMemberTeamProject create(TeamMemberTeamProject memberProject)
	{
		return teamMemberTeamProjectDAO.create(memberProject);
	}
	
	public TeamMemberTeamProject delete(TeamMemberTeamProject memberProject)
	{
		return teamMemberTeamProjectDAO.delete(memberProject);
	}
	
	public List<TeamMemberTeamProject> findByIdTeamMember(int id){
		return teamMemberTeamProjectDAO.findByIdTeamMember(id);
	}
}
