package com.increff.pdf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pdf.dto.ReportDto;
import com.increff.pdf.model.form.BrandReportForm;
import com.increff.pdf.model.form.InventoryReportForm;
import com.increff.pdf.model.form.SalesReportForm;
import com.increff.pdf.util.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/reports")
public class ReportApiController {
    
    @Autowired
    private ReportDto dto;

    @ApiOperation(value = "Generate Brand Report")
    @RequestMapping(path = "/brands", method = RequestMethod.POST)
    public String brandReport(@RequestBody List<BrandReportForm> form) throws ApiException {
        return dto.generateBrandReport(form);
    }

    @ApiOperation(value = "Generate Inventory Report")
    @RequestMapping(path = "/inventory", method = RequestMethod.POST)
    public String inventoryReport(@RequestBody List<InventoryReportForm> form) throws ApiException {
        return dto.generateInventoryReport(form);
    }

    @ApiOperation(value = "Generate Sales Report")
    @RequestMapping(path = "/sales", method = RequestMethod.POST)
    public String salesReport(@RequestBody List<SalesReportForm> form) throws ApiException {
        return dto.generateSalesReport(form);
    }
}
