package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.model.Roles;
import com.increff.pos.model.data.InfoData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(path = "/site")
public class InitApiController extends AbstractUiController {

	@Autowired
	private UserService service;
	@Autowired
	private InfoData info;

	@ApiOperation(value = "Initializes application")
	@RequestMapping(path = "/init", method = RequestMethod.GET)
	public ModelAndView showPage(UserForm userForm) throws ApiException {
		info.setMessage("");
		return mav("init.html");
	}

	@ApiOperation(value = "Initializes application")
	@RequestMapping(path = "/init", method = RequestMethod.POST)
	public ModelAndView initSite(UserForm userForm) throws ApiException {
		List<UserPojo> list = service.getAll();
		if (list.size() > 0) {
			info.setMessage("Application already initialized. Please use existing credentials");
		} else {
			userForm.setRole("admin");
			UserPojo userPojo = convert(userForm);
			service.add(userPojo);
			info.setMessage("Application initialized");
		}
		return mav("init.html");

	}

	private static UserPojo convert(UserForm userForm) {
		UserPojo userPojo = new UserPojo();
		userPojo.setEmail(userForm.getEmail());
		userPojo.setRole(Roles.operator);
		userPojo.setPassword(userForm.getPassword());
		return userPojo;
	}

}
