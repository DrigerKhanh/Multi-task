package com.java.vakapu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.java.vakapu.entity.User;
import com.java.vakapu.entity.Member;
import com.java.vakapu.entity.Project;
import com.java.vakapu.entity.Team;
import com.java.vakapu.entity.TeamMember;
import com.java.vakapu.services.AccountServices;
import com.java.vakapu.services.MemberServices;
import com.java.vakapu.services.ProjectServices;
import com.java.vakapu.services.ProjectUserServices;
import com.java.vakapu.services.TeamMemberServices;
import com.java.vakapu.services.TeamServices;

@Controller
@SessionAttributes("idTeam")
@RequestMapping("/team")
public class TeamController {

	@Autowired
	private AccountServices accountServices;

	@Autowired
	private ProjectUserServices proUserServices;

	@Autowired
	private ProjectServices proServices;

	@Autowired
	private TeamServices teamServices;

	@Autowired
	private MemberServices memberServices;
	
	@Autowired
	private TeamMemberServices teamMemberServices;

	@GetMapping
	public String getInfoProject(@RequestParam("idTeam") int idTeam,Model model, ModelMap modelMap) {
		String emailUser = accountServices.getEmailUser();
		Team team = teamServices.findById(idTeam);
		System.out.println("Vao teammmmmm::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		List<TeamMember> member = teamMemberServices.findByIdTeam(idTeam);
		for(TeamMember t: member)
		{
			System.out.println("Ket:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::"+t.getMember().getName());
		}
		List<Project> project = proServices.findByIdTeam(idTeam);
		model.addAttribute("emailUser", emailUser);
		model.addAttribute("project", project);
		model.addAttribute("member", member);
		model.addAttribute("team", team);
		
		return "team";
	}

	@PostMapping
	public String handleProject(Model model) {

		return "redirect:/manage";
	}
}