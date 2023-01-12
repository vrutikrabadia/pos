package com.increff.pos.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.BrandSelectData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Api
@RestController
@Validated
@RequestMapping(path = "/api")
public class BrandApiController {

    @Autowired
    private BrandDto dto;

    @ApiOperation(value = "Adds brand/category")
    @RequestMapping(path = "/brands", method = RequestMethod.POST)
    public void add(@Valid @RequestBody BrandForm form) throws ApiException {

        // ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        // Validator validator = validatorFactory.getValidator();

        // // sets

        // Set<ConstraintViolation<BrandForm>> violations = validator.validate(form);
        // if(violations.size() > 0){
        //     throw new ApiException("Form invalid");
        // }
        dto.add(form);
    }

    @ApiOperation(value = "Get brand/category by id")
    @RequestMapping(path = "/brands/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable Integer id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Get all brand/category")
    @RequestMapping(path = "/brands", method = RequestMethod.GET)
    public BrandSelectData get(@RequestParam Integer draw, @RequestParam Integer start, @RequestParam Integer length)
            throws ApiException {
        return dto.getAll(start / length, length, draw);
    }

    @ApiOperation(value = "Get brand/category by brand/category")
    @RequestMapping(path = "/brands/{brand}/categories/{category}", method = RequestMethod.GET)
    public BrandData get(@PathVariable String brand, @PathVariable String category) throws ApiException {
        return dto.get(brand, category);
    }

    @ApiOperation(value = "Update brand/category by id")
    @RequestMapping(path = "/brands/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody BrandForm form) throws ApiException {
        dto.update(id, form);
    }
}
