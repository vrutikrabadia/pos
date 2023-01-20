package com.increff.pos.controller;


import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.DaySalesDto;
import com.increff.pos.model.data.DaySalesData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.DaySalesForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@Validated
@RequestMapping(path = "/api")
public class DaySalesApiController {
    
    @Autowired
    DaySalesDto dto;

    
    @ApiOperation(value = "Adds entry to scheduler")
    @RequestMapping(path = "/day-sales", method = RequestMethod.POST)
    public void add(@RequestBody DaySalesForm form) throws ApiException {
        dto.add(form);
    }

    @ApiOperation(value = "Get entries in date range")
    @RequestMapping(path = "/day-sales/dateRange", method = RequestMethod.GET)
    public SelectData<DaySalesData> getInDateRange(@RequestParam  ZonedDateTime startDate, @RequestParam  ZonedDateTime endDate, @RequestParam Integer draw, @RequestParam Integer start, @RequestParam Integer length) throws ApiException {
        return dto.getInDateRange( start, length,draw, startDate, endDate);
    }

    @ApiOperation(value = "Get all entries")
    @RequestMapping(path = "/day-sales", method = RequestMethod.GET)
    public SelectData<DaySalesData> get(@RequestParam Integer draw, @RequestParam Integer start, @RequestParam Integer length, @RequestParam  Optional<ZonedDateTime> optionalStartDate, @RequestParam Optional<ZonedDateTime> optionalEndDate) throws ApiException {
        return dto.getAll(start, length, draw, optionalStartDate, optionalEndDate);
    }

}
