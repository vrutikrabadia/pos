package com.increff.pos.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api")
public class ReportsApiController {

    @Autowired
    private ReportDto dto;


    @ApiOperation(value = "Get Inventory report")
    @RequestMapping(path = "/reports/inventory", method = RequestMethod.GET)
    public String getInventoryReport() throws ApiException {
        return dto.getInventoryReport();
    }

    @ApiOperation(value = "Get Sales report")
    @RequestMapping(path = "/reports/sales", method = RequestMethod.POST)
    public String getSalesReport(@RequestBody SalesReportForm form) throws ApiException {
        return dto.getSalesReport(form);
    }

    @ApiOperation(value = "Get Brand report")
    @RequestMapping(path = "/reports/brands", method = RequestMethod.GET)
    public String getBrandReport() throws ApiException {
        return dto.getBrandReport();
    }
}
