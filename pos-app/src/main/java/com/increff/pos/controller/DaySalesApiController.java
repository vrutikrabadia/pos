package com.increff.pos.controller;


import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.DaySalesDto;
import com.increff.pos.model.data.DaySalesData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.util.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/day-sales")
public class DaySalesApiController {
    
    @Autowired
    private DaySalesDto dto;

    @ApiOperation(value = "Get entries in date range")
    @RequestMapping(path = "/date-range", method = RequestMethod.GET)
    public SelectData<DaySalesData> getInDateRange(@RequestParam  ZonedDateTime startDate, @RequestParam  ZonedDateTime endDate, @RequestParam Integer draw, @RequestParam Integer start, @RequestParam Integer length) throws ApiException {
        return dto.getInDateRange( start, length,draw, startDate, endDate);
    }

    @ApiOperation(value = "Get all entries")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public SelectData<DaySalesData> get(@RequestParam Integer draw, @RequestParam Integer start, @RequestParam Integer length) throws ApiException {
        return dto.getAll(start, length, draw);
    }

}
