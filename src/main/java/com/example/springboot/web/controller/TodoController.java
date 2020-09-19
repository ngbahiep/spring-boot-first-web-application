package com.example.springboot.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.springboot.web.model.Todo;
import com.example.springboot.web.service.TodoService;

@Controller
public class TodoController {
	
	@Autowired
	TodoService service;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Date - dd/MM/yyyy
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	@RequestMapping(value = "/list-todos", method = RequestMethod.GET)
	public String showTodosList(ModelMap model){
		String name = getLoggedInUsername(model);
		model.put("todos", service.retrieveTodos(name));
		return "list-todos";
	}

	private String getLoggedInUsername(ModelMap model) {
		Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principle instanceof UserDetails) {
			return ((UserDetails)principle).getUsername();
		}
		
		return principle.toString();
	}
	
	@RequestMapping(value = "/add-todo", method = RequestMethod.GET)
	public String showAddTodoPage(ModelMap model){
		model.addAttribute("todo", new Todo(0, getLoggedInUsername(model), "Default", new Date(), false));
		return "todo";
	}
	
	@RequestMapping(value = "/add-todo", method = RequestMethod.POST)
	public String addTodo(ModelMap model, @Valid Todo todo, BindingResult result){
		if (result.hasErrors()) {
			return "todo";
		}
		service.addTodo(getLoggedInUsername(model), todo.getDesc(), todo.getTargetDate(), false);
		return "redirect:/list-todos";
	}
	
	@RequestMapping(value = "/delete-todo", method = RequestMethod.GET)
	public String deleteTodoPage(@RequestParam int id){
		if (id == 0) {
			throw new RuntimeException("Something went wrong!");
		}
		service.deleteTodo(id);
		return "redirect:/list-todos";
	}
	
	@RequestMapping(value = "/update-todo", method = RequestMethod.GET)
	public String showUpdateTodoPage(@RequestParam int id, ModelMap model){
		System.out.println("showUpdateTodoPage: " + id);
		Todo todo = service.retrieveTodo(id);
		model.put("todo", todo);
		return "todo";
	}
	
	@RequestMapping(value = "/update-todo", method = RequestMethod.POST)
	public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result){
		
		if (result.hasErrors()) {
			return "todo";
		}
		
		todo.setUser(getLoggedInUsername(model));
		
		service.updateTodo(todo);
		
		return "redirect:/list-todos";
	}
}
