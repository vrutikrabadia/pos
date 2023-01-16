package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ExceptionUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class BrandDto {

    @Autowired
    private BrandService service;

    public void add(BrandForm f) throws ApiException {
        
        StringUtil.normalise(f, BrandForm.class);
        
        ValidateUtil.validateForms(f);
        BrandPojo p = ConvertUtil.objectMapper(f, BrandPojo.class);
    

        try {
            service.getcheck(f.getBrand(), f.getCategory());
        } catch (ApiException e) {
            service.add(p);
            return;
        }

        throw new ApiException("Brand and category already exists");

    }

    public void bulkAdd(List<BrandForm> list) throws ApiException {
        JSONArray errorList = new JSONArray();

        List<BrandPojo> pojoList = new ArrayList<BrandPojo>();

        for (BrandForm form : list) {
            StringUtil.normalise(form, BrandForm.class);
            try {
                ValidateUtil.validateForms(form);
            } catch (ConstraintViolationException e) {
                JSONObject error = new JSONObject(new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create()
                        .toJson(form));
                error.put("error", ExceptionUtil.getValidationMessage(e));
                errorList.put(error);
                continue;
            }
            BrandPojo pojo = ConvertUtil.objectMapper(form, BrandPojo.class);
            pojoList.add(pojo);
        }

        JSONArray serviceErrors = service.bulkAdd(pojoList);
        errorList.putAll(serviceErrors);

        if (!errorList.isEmpty()) {
            throw new ApiException(errorList.toString());
        }

    }

    public BrandData get(Integer id) throws ApiException {
        BrandPojo p = service.get(id);
        return ConvertUtil.objectMapper(p, BrandData.class);
    }

    public SelectData<BrandData> getAll(Integer start, Integer length, Integer draw, Optional<String> searchValue) {
        
        List<BrandPojo> list = new ArrayList<BrandPojo>();
        List<BrandData> list1 = new ArrayList<BrandData>();
        
    
        if(searchValue.isPresent() && !searchValue.get().isBlank()){
            list = service.searchQueryString(start/length, length,StringUtil.toLowerCase(searchValue.get()));
        }
        else{
            list = service.getAll(start/length, length);
        }

        for (BrandPojo p : list) {
            list1.add(ConvertUtil.objectMapper(p, BrandData.class));
        }

        Integer totalEntries = service.getTotalEntries();

        SelectData<BrandData> result = new SelectData<BrandData>();
        result.setData(list1);
        result.setDraw(draw);
        result.setRecordsFiltered(totalEntries);
        result.setRecordsTotal(totalEntries);
        return result;

    }

    public BrandData get(String brand, String category) throws ApiException {
        brand = StringUtil.toLowerCase(brand);
        category = StringUtil.toLowerCase(category);
        BrandPojo p = service.get(brand, category);

        return ConvertUtil.objectMapper(p, BrandData.class);
    }

    public void update(Integer id, BrandForm f) throws ApiException {
        
        StringUtil.normalise(f, BrandForm.class);
        ValidateUtil.validateForms(f);
        BrandPojo p = ConvertUtil.objectMapper(f, BrandPojo.class);
        
        service.update(id, p);
    }

}
