package com.increff.pos.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.SchedulerDto;
import com.increff.pos.model.data.SchedulerData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.SchedulerForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@Validated
@RequestMapping(path = "/api")
public class SchedulerApiController {
    
    @Autowired
    SchedulerDto dto;

    
    @ApiOperation(value = "Adds entry to scheduler")
    @RequestMapping(path = "/day-sales", method = RequestMethod.POST)
    public void add(@RequestBody SchedulerForm form) throws ApiException {
        dto.add(form);
    }

    @ApiOperation(value = "Get entries in date range")
    @RequestMapping(path = "/day-sales/dateRange", method = RequestMethod.GET)
    public SelectData<SchedulerData> getInDateRange(@RequestParam String startDate, @RequestParam String endDate, @RequestParam Integer draw, @RequestParam Integer start, @RequestParam Integer length) throws ApiException {
        return dto.getInDateRange(startDate, endDate, start, length,draw);
    }

    @ApiOperation(value = "Get all entries")
    @RequestMapping(path = "/day-sales/", method = RequestMethod.GET)
    public SelectData<SchedulerData> get(@RequestParam Integer draw, @RequestParam Integer start, @RequestParam Integer length) throws ApiException {
        return dto.getAll(start, length, draw);
    }

}
