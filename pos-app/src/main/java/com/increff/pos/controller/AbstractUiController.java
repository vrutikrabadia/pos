package com.increff.pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.model.data.InfoData;
import com.increff.pos.model.data.UserPrincipal;
import com.increff.pos.spring.Properties;
import com.increff.pos.util.SecurityUtil;

@Controller
public abstract class AbstractUiController {

	@Autowired
	private InfoData info;

	@Autowired
	private Properties properties;

	protected ModelAndView mav(String page) {
		// Get current user
		UserPrincipal principal = SecurityUtil.getPrincipal();

		info.setEmail(principal == null ? "" : principal.getEmail());
		info.setRole(principal == null ? "" : principal.getRole());
		if(!info.getShown()) {
			info.setShown(true);
		}
		else{
			info.setMessage("No message");
		}

		// Set info
		ModelAndView mav = new ModelAndView(page);
		mav.addObject("info", info);
		mav.addObject("baseUrl", properties.getBaseUrl());
		return mav;
	}

}
