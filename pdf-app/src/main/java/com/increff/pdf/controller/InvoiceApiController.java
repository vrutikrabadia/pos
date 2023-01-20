package com.increff.pdf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pdf.dto.InvoiceDto;
import com.increff.pdf.model.form.InvoiceForm;
import com.increff.pdf.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/invoices")
public class InvoiceApiController {

    @Autowired
    private InvoiceDto dto;

    @ApiOperation(value = "Generate Order invoice")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public String get(@RequestBody InvoiceForm form) throws ApiException {
        return dto.generateInvoice(form);
    }
}
