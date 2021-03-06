package com.java.vakapu.controller;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.java.vakapu.entity.NotificationSystem;
import com.java.vakapu.entity.ProjectHistory;
import com.java.vakapu.entity.TaskTeamProject;
import com.java.vakapu.entity.TeamMember;
import com.java.vakapu.entity.TeamMemberTaskTeamProject;
import com.java.vakapu.entity.TeamMemberTeamProject;
import com.java.vakapu.entity.TeamProject;
import com.java.vakapu.entity.Todo;
import com.java.vakapu.entity.User;
import com.java.vakapu.model.TaskModel;
import com.java.vakapu.model.TeamProjectModel;
import com.java.vakapu.services.AccountServices;
import com.java.vakapu.services.DateServices;
import com.java.vakapu.services.HistoryServices;
import com.java.vakapu.services.NotificationsSystemServices;
import com.java.vakapu.services.ProjectServices;
import com.java.vakapu.services.TaskServices;
import com.java.vakapu.services.TeamMemberServices;
import com.java.vakapu.services.TeamMemberTeamProjectServices;
import com.java.vakapu.services.TodoServices;
import com.java.vakapu.services.UserServices;

import utils.Activity;

@Controller
@SessionAttributes({ "idproject", "idteam"})
@RequestMapping("/team-project")
public class ProjectTeamController {

	@Autowired
	private AccountServices accountServices;

	@Autowired
	private TeamMemberServices teamMemberServices;

	@Autowired
	private ProjectServices proServices;

	@Autowired
	private TeamMemberTeamProjectServices teamProServices;

	@Autowired
	private TaskServices taskServices;

	@Autowired
	private UserServices userServices;

	@Autowired
	private TeamMemberTeamProjectServices teamMemberTeamProjectDAO;

	@Autowired
	private HistoryServices historyServices;

	@Autowired
	private NotificationsSystemServices notificationsSystemServices;

	@Autowired
	private DateServices dateServices;
	
	@Autowired
	private TodoServices todoServices;

	@GetMapping
	public String teamProject(@ModelAttribute("idteam") int idTeam, @RequestParam("idProject") int idProject,
			Model model, ModelMap modelMap) throws ParseException {
		String emailUser = accountServices.getEmailUser();
		User user = userServices.findByEmail(emailUser);
		TeamProject teamProject = proServices.find(idProject);
		teamProject.setDue(dateServices.caculatorDue(teamProject.getEndDate()));
		proServices.updateProject(teamProject);
		modelMap.put("idproject", idProject);

		List<ProjectHistory> proHis = historyServices.findByIdProject(idProject);

		List<TeamMemberTeamProject> userStore = teamProServices.findByIdProject(idProject);
		List<TeamMember> member = teamMemberServices.findByIdTeam(idTeam);
		for(TeamMember t:member)
		{
			if(emailUser.equals(t.getMember().getEmail()) && t.getRole().equals("Admin"))
			{
				model.addAttribute("checkAdmin", "yes");
			}
		}
		List<TeamMember> remove = new ArrayList<TeamMember>();
		for (TeamMemberTeamProject u : userStore) {
			for (TeamMember t : member) {
				if (t.getMember().getEmail().equals(u.getTeamMember().getMember().getEmail())) {
					remove.add(t);
				}
			}
		}
		if (!remove.isEmpty()) {
			member.removeAll(remove);
		}

		List<Integer> task = taskServices.findTaskByIdProjectAll2(idProject);
		List<TaskTeamProject> taskTeam = new ArrayList<>();
		for (Integer p : task) {
			taskTeam.add(taskServices.findById(p));
		}

		List<TeamMemberTaskTeamProject> userTaskStore = taskServices.findAll();

		int taskDone = taskServices.findTaskDoneByIdProject(idProject);
		teamProject.setTaskDone(taskDone);
		teamProject.setTotalTask(taskTeam.size());
		proServices.updateProject(teamProject);
		TeamProjectModel edit = new TeamProjectModel();
		TeamProjectModel manageUser = new TeamProjectModel();
		TaskModel taskModel = new TaskModel();
		edit.fromProject(teamProject);
		List<NotificationSystem> listMes = notificationsSystemServices.findByEmail(emailUser);
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
		
		model.addAttribute("messages", listMes);
		model.addAttribute("editProject", edit);
		model.addAttribute("manageUser", manageUser);
		model.addAttribute("history", proHis);
		model.addAttribute("taskModel", taskModel);
		model.addAttribute("userTask", userTaskStore);
		model.addAttribute("task", taskTeam);
		model.addAttribute("member", member);
		model.addAttribute("user", userStore);
		model.addAttribute("project", teamProject);
		model.addAttribute("emailUser", emailUser);
		model.addAttribute("idTeam", idTeam);
		model.addAttribute("userLogin", user);
		model.addAttribute("path", "team-project");
		return "project-team";
	}

	@PostMapping
	public String handalTeamProject(Model model) {

		return "redirect:/team-project";
	}

	@RequestMapping(value = "/create-task", method = RequestMethod.POST)
	public String createTeam(@ModelAttribute("idproject") int idProject, @ModelAttribute("idteam") int idTeam,
			@ModelAttribute("taskModel") TaskModel taskModel, BindingResult result, Model model) throws ParseException {
		if (taskModel.getEmail().length == 0) {
			return "redirect:/team-project?idProject=" + idProject + "&idTeam=" + idTeam;
		}
		String emailUser = accountServices.getEmailUser();
		User user = userServices.findByEmail(emailUser);
		TaskTeamProject task = taskModel.toTask();
		task.setOwner(emailUser);
		task.setCompleted(0);
		task.setDue(-2);
		TaskTeamProject task2 = taskServices.create(task);
		String[] emailStore = taskModel.getEmail();
		TeamProject pro = proServices.find(idProject);
		ProjectHistory proHis = new ProjectHistory();
		proHis.setUser(user.getName());
		proHis.setActivity(Activity.ADDTASK);
		proHis.setLast(task2.getName());
		proHis.setTeamProject(pro);
		proHis.setId_user(emailUser);
		proHis.setId_last(task2.getId());
		historyServices.create(proHis);

		for (String t : emailStore) {
			TeamMemberTeamProject memberPro = teamMemberTeamProjectDAO.findByEmailUser(t, idProject);
			TeamMemberTaskTeamProject taskTeam = new TeamMemberTaskTeamProject();
			taskTeam.setTeamMemberTeamProject(memberPro);
			taskTeam.setTaskTeamProject(task2);
			taskServices.createMemberTask(taskTeam);
		}
		return "redirect:/team-project?idProject=" + idProject + "&idTeam=" + idTeam;
	}

	@RequestMapping(value = "/edit-project", method = RequestMethod.POST)
	public String editProject(@ModelAttribute("idteam") int idTeam, @ModelAttribute("idproject") int idProject,
			@ModelAttribute("editProject") TeamProjectModel editProject, Model model, BindingResult result)
			throws ParseException {
		if (result.hasErrors()) {
			return "redirect:/team-project";
		}
		String emailUser = accountServices.getEmailUser();
		User user = userServices.findByEmail(emailUser);
		TeamProject a = editProject.toProject();
		proServices.updateProject(a);
		String[] email = editProject.getEmail();
		List<TeamMemberTeamProject> userStore = teamProServices.findByIdProject(idProject);
		List<TeamMemberTeamProject> userRemove = new ArrayList<TeamMemberTeamProject>();
		for (String e : email) {
			for (TeamMemberTeamProject t : userStore) {
				if (e.equals(t.getTeamMember().getMember().getEmail())) {
					userRemove.add(t);

				}
			}
		}
		userStore.removeAll(userRemove);
		for (TeamMemberTeamProject u : userStore) {
			TeamMemberTeamProject memPro = teamMemberTeamProjectDAO
					.findByEmailUser(u.getTeamMember().getMember().getEmail(), idProject);
			List<TeamMemberTaskTeamProject> memTask = taskServices.findByIdMemberProject(memPro.getId());
			for (TeamMemberTaskTeamProject taskteam : memTask) {
				taskServices.deleteTaskTeamPro(taskteam);
			}
			teamMemberTeamProjectDAO.delete(memPro);
		}

		String[] email2 = editProject.getEmail2();
		DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime local = LocalDateTime.now();
		String time = date.format(local);
		String idTeamString = Integer.toString(idTeam);
		if (email2 != null) {
			for (String e : email2) {
				User user2 = userServices.findByEmail(e);
				NotificationSystem mess = new NotificationSystem();
				mess.setToUser(user2);
				mess.setUserFrom(user);
				mess.setStatus(0);
				mess.setDate(time);
				NotificationSystem mess2 = notificationsSystemServices.create(mess);
				String messa = "Hello " + user2.getName() + ",You have invitation to join the team from "
						+ user.getName() + "<br>Do you agree?<br>";
				String messe = String.format(
						"<a class=\"btn btn-primary btn-sm\" href=\"team/joinTeam?idTeam=%s&idNotifications=%s\">Agree</a>"
								+ "<a class=\"btn btn-primary btn-sm\" href=\"team/disJoinTeam?idTeam=%s&idNotifications=%s\">DisAgree</a>",
						idTeamString, mess2.getId(), idTeamString, mess2.getId());
				mess2.setMessages(messa + messe + "<br>Your message: " + "Wellcome ^^");
				notificationsSystemServices.update(mess2);
			}
		}
		return "redirect:/team-project?idProject=" + a.getId();
	}

	@RequestMapping(value = "/manage-user-project", method = RequestMethod.POST)
	public String manageUser(@ModelAttribute("idteam") int idTeam, @ModelAttribute("idproject") int idProject,
			@ModelAttribute("manageUser") TeamProjectModel editProject, Model model, BindingResult result) {
		String[] email = editProject.getEmail();
		List<TeamMemberTeamProject> userStore = teamProServices.findByIdProject(idProject);
		List<TeamMemberTeamProject> userRemove = new ArrayList<TeamMemberTeamProject>();
		for (String e : email) {
			for (TeamMemberTeamProject t : userStore) {
				if (e.equals(t.getTeamMember().getMember().getEmail())) {
					userRemove.add(t);

				}
			}
		}
		userStore.removeAll(userRemove);
		for (TeamMemberTeamProject u : userStore) {
			TeamMemberTeamProject memPro = teamMemberTeamProjectDAO
					.findByEmailUser(u.getTeamMember().getMember().getEmail(), idProject);
			List<TeamMemberTaskTeamProject> memTask = taskServices.findByIdMemberProject(memPro.getId());
			for (TeamMemberTaskTeamProject taskteam : memTask) {
				taskServices.deleteTaskTeamPro(taskteam);
			}
			teamMemberTeamProjectDAO.delete(memPro);
		}

		String[] email2 = editProject.getEmail2();
		if (email2 != null) {
			TeamProject b = proServices.find(idProject);
			for (String e : email2) {
				TeamMemberTeamProject c = new TeamMemberTeamProject();
				c.setTeamMember(teamMemberServices.getUserTeam(idTeam, e));
				c.setTeamProject(b);
				teamMemberTeamProjectDAO.create(c);
			}
		}

		return "redirect:/team-project?idProject=" + idProject;
	}

	@RequestMapping(value = "/join-to-project", method = RequestMethod.GET)
	public String joinToProject(@ModelAttribute("idteam") int idTeam, @RequestParam("idProject") int idProject,
			Model model, BindingResult result) {
		String email = accountServices.getEmailUser();
		List<TeamMemberTeamProject> tmp = teamProServices.findByIdProject(idProject);
		for(TeamMemberTeamProject t:tmp)
		{
			if(email.equals(t.getTeamMember().getMember().getEmail()))
			{
				return "redirect:/team-project?idProject=" + idProject;
			}
		}
		TeamMemberTeamProject c = new TeamMemberTeamProject();
		c.setTeamMember(teamMemberServices.getUserTeam(idTeam, email));
		c.setTeamProject(proServices.find(idProject));
		teamMemberTeamProjectDAO.create(c);
		return "redirect:/team-project?idProject=" + idProject;
	}
	
	@RequestMapping(value="MaskAsDone",method=RequestMethod.GET)
	public String MarkAsDone(@RequestParam(name="idTask") int idtask, @ModelAttribute("idproject") int idProject, @ModelAttribute("idteam") int idTeam, BindingResult result, Model model) throws ParseException
	{
		if(result.hasErrors())
		{
			return "redirect:/team-project?idProject=" + idProject;
		}
		List<Todo> todo= taskServices.findTodoByIdTask(idtask);
		for(Todo a : todo)
		{
			a.setCompleted(1);
			taskServices.update(a);
		}
		TaskTeamProject task=taskServices.findById(idtask);
		taskServices.update(task);
		
		List<Todo> listTodo = taskServices.findTodoByIdTask(idtask);
		int completedAmount = taskServices.findTodoDoneByIdTask(idtask);
		int due = dateServices.caculatorDue(task.getEndDate());
		task.setCompletedAmount(completedAmount);
		task.setTotalTask(listTodo.size());
		task.setDue(due);
		if(task.getCompletedAmount() == task.getTotalTask() && task.getTotalTask()!=0)
		{
			task.setCompleted(1);
		}
		else
		{
			task.setCompleted(0);
		}
		taskServices.update(task);
		
		if(idTeam == 0 || idProject == 0)
		{
			return "redirect:/manage";
		}
		return "redirect:/team-project?idProject="+idProject;
		
	}
	
	@RequestMapping(value="MarkAsComplete",method=RequestMethod.GET)
	public String MarkAsComplete(@ModelAttribute("idteam") int idteam, @ModelAttribute("idproject") int idproject ,BindingResult result) throws ParseException
	{
		List<TeamMemberTaskTeamProject> listTeam= taskServices.findAllTaskByID(idproject);
		for(TeamMemberTaskTeamProject a: listTeam)
		{
//			System.out.println("..........................................");
//			System.out.println(a.getTaskTeamProject().getName());
			TaskTeamProject task=a.getTaskTeamProject();
			List<Todo> listTodo=taskServices.findTodoByIdTask(a.getTaskTeamProject().getId());
			for(Todo b :listTodo)
			{
				b.setCompleted(1);
				taskServices.update(b);
			}
//			List<Todo> listTodo = taskServices.findTodoByIdTask(idtask);
			int completedAmount = taskServices.findTodoDoneByIdTask(a.getTaskTeamProject().getId());
			int due = dateServices.caculatorDue(task.getEndDate());
			task.setCompletedAmount(completedAmount);
			task.setTotalTask(listTodo.size());
			task.setDue(due);
			if(task.getCompletedAmount() == task.getTotalTask() && task.getTotalTask()!=0)
			{
				task.setCompleted(1);
			}
			else
			{
				task.setCompleted(0);
			}
			taskServices.update(task);
		}
		if(idproject == 0 || idteam == 0)
		{
			return "redirect:/manage";
		}
		return "redirect:/team-project?idProject="+idproject;
	}

	@RequestMapping(value = "/leaveTask", method = RequestMethod.GET)
	public String leaveTask(@ModelAttribute("idproject") int idProject, @RequestParam("idTask") int idTask,
			Model model, BindingResult result) {
		String email = accountServices.getEmailUser();
		TeamMemberTaskTeamProject task = taskServices.findByIdTaskEmail(email, idTask);
		taskServices.deleteTaskTeamPro(task);
		if(idProject == 0)
		{
			return "redirect:/manage";
		}
//		System.out.println("Viet Anhhh:" +task.getTaskTeamProject().getName());
		return "redirect:/team-project?idProject="+idProject;
	}
}
