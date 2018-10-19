package com.java.vakapu.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.java.vakapu.entity.User;
import com.java.vakapu.model.TeamModel;
import com.java.vakapu.entity.Friendship;
import com.java.vakapu.entity.TaskTeamProject;
import com.java.vakapu.entity.Team;
import com.java.vakapu.entity.TeamMember;
import com.java.vakapu.entity.TeamMemberTaskTeamProject;
import com.java.vakapu.entity.TeamMemberTeamProject;
import com.java.vakapu.entity.TeamProject;
import com.java.vakapu.services.AccountServices;
import com.java.vakapu.services.DateServices;
import com.java.vakapu.services.FriendshipServices;
import com.java.vakapu.services.UserServices;
import com.java.vakapu.services.ProjectServices;
import com.java.vakapu.services.TaskServices;
import com.java.vakapu.services.TeamMemberServices;
import com.java.vakapu.services.TeamServices;

@Controller
@RequestMapping("/manage")
public class ManageController {

	@Autowired
	private TeamServices teamServices;
	
	@Autowired
	private AccountServices accountServices;

	@Autowired
	private ProjectServices projectServices;

	@Autowired
	private UserServices userServices;
	
	@Autowired
	private FriendshipServices friendshipServices;
	
	@Autowired
	private TeamMemberServices teamMemberServices;
	
	@Autowired
	private TaskServices taskServices;
	
	@Autowired
	private DateServices dateServices;

	@GetMapping
	public String getInfoProject(Model model) throws java.text.ParseException {
		String emailUser = accountServices.getEmailUser();
		User user = userServices.findByEmail(emailUser);
		List<TeamMember> team = teamMemberServices.findByEmai(emailUser);
		List<TeamMember> memberStore = teamMemberServices.findAll();
		List<TeamMemberTeamProject> teamProject = projectServices.findByEmail(emailUser);
		for(TeamMemberTeamProject t: teamProject)
		{
			  TeamProject teamPro = projectServices.find(t.getTeamProject().getId());
			  teamPro.setDue(dateServices.caculatorDue(t.getTeamProject().getEndDate()));
			  projectServices.updateProject(teamPro);
		}
		List<TeamMemberTeamProject> userProjectStore = projectServices.findAll();
		List<Friendship> myFriend = friendshipServices.findFriend(emailUser, 1);
		List<TeamMemberTaskTeamProject> task = taskServices.findByEmailUser(emailUser);
		List<TeamMemberTaskTeamProject> userTaskStore = taskServices.findAll();
		Set<Integer> listProject = new HashSet<>();
		List<TaskTeamProject> taskTeam = new ArrayList<>();
		for(TeamMemberTaskTeamProject t: task)
		{
			listProject.add(t.getTaskTeamProject().getId());
		}
		for(Integer p:listProject)
		{
			taskTeam.add(taskServices.findById(p));
		}
		TeamModel teamModel = new TeamModel();
		model.addAttribute("teamAdd", teamModel);
		model.addAttribute("friend", myFriend);
		model.addAttribute("member", memberStore);
		model.addAttribute("userPro", userProjectStore);
		model.addAttribute("emailUser", emailUser);
		model.addAttribute("team", team);
		model.addAttribute("project", teamProject);
		model.addAttribute("profile", user);
		model.addAttribute("userTask", userTaskStore);
		model.addAttribute("task", taskTeam);
		
		return "manage";
	}

	@PostMapping
	public String handleProject(Model model) {

		return "redirect:/manage";
	}
	
	
	@RequestMapping(value="/createTeam",method=RequestMethod.POST)
	public String createTeam(@ModelAttribute("addMember") TeamModel teamModel,@ModelAttribute("teamAdd") Team team,Model model) throws ParseException
	{
		String[] email = teamModel.getEmail();
		String emailUser = accountServices.getEmailUser();
		User user = userServices.findByEmail(emailUser);
		team.setOwner(emailUser);
		Team team2 =teamServices.createTeam(team);
		TeamMember teamMemberAdmin = new TeamMember();
		teamMemberAdmin.setMember(user);
		teamMemberAdmin.setTeam(team);
		teamMemberAdmin.setRole("Admin");
		teamMemberServices.create(teamMemberAdmin);
		int i =0;
		for(String e:email)
		{
			i++;
			User adduser = userServices.findByEmail(e);
			TeamMember teamMember = new TeamMember();
			teamMember.setMember(adduser);
			teamMember.setTeam(team2);
			teamMember.setRole("Member");
			teamMemberServices.create(teamMember);
		}
		team2.setMemberAmount(i);
		teamServices.updateTeam(team2);
	
		return "redirect:/manage";
	}
}
