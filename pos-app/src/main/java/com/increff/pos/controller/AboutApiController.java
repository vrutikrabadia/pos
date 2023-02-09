package com.increff.pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.data.AboutAppData;
import com.increff.pos.service.AboutAppService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/about")
public class AboutApiController {

	@Autowired
	private AboutAppService service;

	@ApiOperation(value = "Get Application Name and Version.")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public AboutAppData getDetails() {
		AboutAppData appDetails = new AboutAppData();
		appDetails.setName(service.getName());
		appDetails.setVersion(service.getVersion());
		return appDetails;
	}
}
