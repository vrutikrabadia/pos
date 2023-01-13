package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.BrandSelectData;
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
        ValidateUtil.validateForms(f);
        BrandPojo p = ConvertUtil.objectMapper(f, BrandPojo.class);
        StringUtil.normaliseBrand(p);

        try {
            service.getcheck(f.getBrand(), f.getCategory());
        } catch (ApiException e) {
            service.add(p);
            return;
        }

        throw new ApiException("Brand and category already exists");

    }

    public void bulkAdd(List<BrandForm> list) throws ApiException {
        JSONArray errorList1 = new JSONArray();

        List<BrandPojo> pojoList = new ArrayList<BrandPojo>();

        for (BrandForm form : list) {
            try {
                ValidateUtil.validateForms(form);
            } catch (ConstraintViolationException e) {
                JSONObject error1 = new JSONObject();
                error1.put("brand", form.getBrand());
                error1.put("category", form.getCategory());
                error1.put("error", ExceptionUtil.getValidationMessage(e));
                errorList1.put(error1);
                continue;
            }
            BrandPojo pojo = ConvertUtil.objectMapper(form, BrandPojo.class);
            pojoList.add(pojo);
        }

        
        try{
            service.bulkAdd(pojoList);
        }
        catch(ApiException e){
            JSONArray serviceErrors1 = new JSONArray(e.getMessage());
            errorList1.putAll(serviceErrors1);

        }

        if(!errorList1.isEmpty()){
            throw new ApiException(errorList1.toString());
        }
        
    }

    public BrandData get(Integer id) throws ApiException {
        BrandPojo p = service.get(id);
        return ConvertUtil.objectMapper(p, BrandData.class);
    }

    public BrandSelectData getAll(Integer pageNo, Integer pageSize, Integer draw) {
        List<BrandPojo> list = service.getAll(pageNo, pageSize);
        List<BrandData> list1 = new ArrayList<BrandData>();
        for (BrandPojo p : list) {
            list1.add(ConvertUtil.objectMapper(p, BrandData.class));
        }

        Integer totalEntries = service.getTotalEntries();

        BrandSelectData result = new BrandSelectData();
        result.setData(list1);
        result.setDraw(draw);
        result.setRecordsFiltered(totalEntries);
        result.setRecordsTotal(totalEntries);
        return result;

    }

    public BrandData get(String brand, String category) throws ApiException {
        BrandPojo p = service.get(brand, category);

        return ConvertUtil.objectMapper(p, BrandData.class);
    }

    public void update(Integer id, BrandForm f) throws ApiException {
        ValidateUtil.validateForms(f);
        BrandPojo p = ConvertUtil.objectMapper(f, BrandPojo.class);
        StringUtil.normaliseBrand(p);
        service.update(id, p);
    }

}
