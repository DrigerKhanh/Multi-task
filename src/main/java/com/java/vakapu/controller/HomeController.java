package com.java.vakapu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.java.vakapu.entity.Friendship;
import com.java.vakapu.entity.NotificationSystem;
import com.java.vakapu.entity.TeamMember;
import com.java.vakapu.entity.User;
import com.java.vakapu.model.TeamModel;
import com.java.vakapu.services.AccountServices;
import com.java.vakapu.services.FriendshipServices;
import com.java.vakapu.services.NotificationsSystemServices;
import com.java.vakapu.services.TeamMemberServices;
import com.java.vakapu.services.UserServices;

@Controller
@SessionAttributes({ "idteam","idproject", "idtask" })
@RequestMapping("/home")
public class HomeController {
	@Autowired
	private AccountServices accountServices;
	
	@Autowired
	private NotificationsSystemServices notificationsSystemServices ;
	
	@Autowired
	private FriendshipServices friendshipServices;
	
	@Autowired
	private TeamMemberServices teamMemberServices;
	
	@Autowired
	private UserServices userServices;
	
	@GetMapping
	public String home(Model model,ModelMap modelMap) {
		String emailUser = accountServices.getEmailUser();
		User user = userServices.findByEmail(emailUser);
		List<NotificationSystem> listMes = notificationsSystemServices.findByEmail(emailUser);
		modelMap.put("idteam", 0);
		modelMap.put("idproject", 0);
		modelMap.put("idtask", 0);
		int i=0;
		for(NotificationSystem l:listMes)
		{
			i++;
			if(i<3 && l.getStatus() == 0)
			{
				model.addAttribute("checkNotification", "yes");
				break;
			}
		}
		List<Friendship> myFriend = friendshipServices.findFriend(emailUser, 1);
		List<TeamMember> team = teamMemberServices.findByEmai(emailUser);
		List<TeamMember> memberStore = teamMemberServices.findAll();
//		for(Friendship f:myFriend)
//		{
//			System.out.println("vietanh:"+f.getEmailFriend().getName());
//		}
		TeamModel teamModel = new TeamModel();
		model.addAttribute("teamAdd", teamModel);
		model.addAttribute("team", team);
		model.addAttribute("emailUser", emailUser);
		model.addAttribute("messages", listMes);
		model.addAttribute("friend", myFriend);
		model.addAttribute("member", memberStore);
		model.addAttribute("user", user);
		model.addAttribute("path", "home");
		return "home";
	}

	@PostMapping
	public String handelHome(Model model)  {

		return "redirect:/home";
	}
	
	@RequestMapping(value = "/seen", method = RequestMethod.GET)
	public String seenNoti(@RequestParam("idNotifications") int idNotification,@RequestParam("path") String path,@ModelAttribute("idteam") int idTeam, @ModelAttribute("idproject") int idProject, @ModelAttribute("idtask") int idTask,Model model) {
		NotificationSystem noti = notificationsSystemServices.find(idNotification);
		noti.setStatus(1);
		notificationsSystemServices.update(noti);
		
		if(path.equals("team"))
		{
			return "redirect:/"+path+"?idTeam="+idTeam;
		}
		else if(path.equals("team-project"))
		{
			return "redirect:/"+path+"?idProject="+idProject;
		}
		else if(path.equals("task-todo"))
		{
			return "redirect:/"+path+"?idTask="+idTask;
		}
		return "redirect:/"+path;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteNoti(@RequestParam("idNotifications") int idNotification,@RequestParam("path") String path,@ModelAttribute("idteam") int idTeam, @ModelAttribute("idproject") int idProject, @ModelAttribute("idtask") int idTask,Model model) {
		NotificationSystem noti = notificationsSystemServices.find(idNotification);
		notificationsSystemServices.delete(noti);
		
		if(path.equals("team"))
		{
			return "redirect:/"+path+"?idTeam="+idTeam;
		}
		else if(path.equals("team-project"))
		{
			return "redirect:/"+path+"?idProject="+idProject;
		}
		else if(path.equals("task-todo"))
		{
			return "redirect:/"+path+"?idTask="+idTask;
		}
		return "redirect:/"+path;
	}
}
