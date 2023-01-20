package com.increff.pdf.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pdf.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/test")
public class TestController {
    @ApiOperation(value = "Get brand/category by id")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public String get() throws ApiException {
        return "Hello";
    }
}
