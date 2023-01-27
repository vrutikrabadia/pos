package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SiteUiController extends AbstractUiController {

	// WEBSITE PAGES
	@RequestMapping(value = "")
	public ModelAndView index() {
		return mav("redirect:/site/login");
	}

	@RequestMapping(value = "/site/login")
	public ModelAndView login() {
		return mav("login.html");
	}

	@RequestMapping(value = "/site/signup")
	public ModelAndView signup() {
		return mav("signup.html");
	}


	@RequestMapping(value = "/site/logout")
	public ModelAndView logout() {
		return mav("redirect:/site/login");
	}


}
