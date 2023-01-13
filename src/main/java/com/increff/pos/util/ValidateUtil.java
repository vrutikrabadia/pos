package com.increff.pos.util;

import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.SchedulerPojo;
import com.increff.pos.service.ApiException;

public class ValidateUtil {
    
    public static <T> void validateForms(T form){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(form);
        System.out.println(violations.size());
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
    

    public static void validateProduct(ProductPojo p) throws ApiException{
        if (StringUtil.isEmpty(p.getName())) {
            throw new ApiException("Product name cannot be empty");
        }
        if (StringUtil.isEmpty(p.getBarcode()) || p.getBarcode().length() != 8) {
            throw new ApiException("Please provide a valid barcode(length 8)");
        }

        if (p.getMrp() < 0) {
            throw new ApiException("MRP should be non negative");
        }
    }

    public static void validateInventory(InventoryPojo p) throws ApiException{
        if(p.getQuantity() < 0){
            throw new ApiException("Quantity should be non negative");
        }
        
    }

    public static void validateOrderItem(OrderItemsPojo p) throws ApiException{
        if(p.getQuantity() < 1){
            throw new ApiException("minimum quantity 1 required for order");
        }
        if(p.getSellingPrice() < 0){
            throw new ApiException("selling price should be non negative");
        }
    }

    public static void validateScheduler(SchedulerPojo p) throws ApiException{
        if(Objects.isNull(p.getDate())){
            throw new ApiException("Date cannot be null");
        }
        if(Objects.isNull(p.getItemsCount()) || p.getItemsCount() < 1){
            throw new ApiException("Items count should be posiitve quantity");
        }
        if(Objects.isNull(p.getTotalRevenue()) || p.getTotalRevenue() < 0){
            throw new ApiException("Revenue should be non negative quantity");
        }
    }
}


