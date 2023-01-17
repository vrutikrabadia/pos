package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/ui")
public class AppUiController extends AbstractUiController {

	@RequestMapping(value = "/home")
	public ModelAndView home() {
		return mav("home.html");
	}


	@RequestMapping(value = "/admin")
	public ModelAndView admin() {
		return mav("user.html");
	}

	@RequestMapping(value = "/brands")
	public ModelAndView brand() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/products")
	public ModelAndView product() {
		return mav("product.html");
	}

	@RequestMapping(value = "/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/orders")
	public ModelAndView orders() {
		return mav("order.html");
	}

	
	@RequestMapping(value = "/day-sales")
	public ModelAndView daySales() {
		return mav("daysales.html");
	}

}
