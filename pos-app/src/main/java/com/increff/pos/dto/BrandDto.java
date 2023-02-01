package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConvertUtil;
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
        StringUtil.normaliseList(list, BrandForm.class);
        ValidateUtil.validateList(list);
        checkFileDuplications(list);

        List<BrandPojo> pojoList = list.stream().map(e->ConvertUtil.objectMapper(e, BrandPojo.class)).collect(Collectors.toList());

        checkDbDuplicate(pojoList);

        service.bulkAdd(pojoList);

    }
    //TODO: ,ake it private

    protected void checkFileDuplications(List<BrandForm> formList) throws ApiException {
        JSONArray errorList = new JSONArray();
        Set<String> fileSet = new HashSet<String>();


        Set<BrandForm> repeatSet = formList.stream().filter(e -> !fileSet.add(e.getBrand() +"#"+ e.getCategory()))
                .collect(Collectors.toSet());


        if (repeatSet.size() > 0) {
            Set<BrandForm> formSet = formList.stream().filter(e->!repeatSet.contains(e)).collect(Collectors.toSet());
            
            repeatSet.forEach(e -> {
                JSONObject error = new JSONObject(new Gson().toJson(e));
                error.put("error", "DUPLICATE entries in file");
                errorList.put(error);
            });


            formSet.forEach(e->{
                JSONObject error = new JSONObject(new Gson().toJson(e));
                error.put("error", "");
                errorList.put(error);
            });


            throw new ApiException(errorList.toString());
        }
    }

    protected void checkDbDuplicate(List<BrandPojo> pojoList) throws ApiException{
        JSONArray errorList = new JSONArray();
        
        List<String> brandList = pojoList.stream().map(BrandPojo::getBrand).collect(Collectors.toList());
        List<BrandPojo> currentExixting = service.getInColumn(Arrays.asList("brand"),Arrays.asList(brandList));
        Set<String> dbSet = new HashSet<String>();
        dbSet = currentExixting.stream().flatMap(pojo -> Stream.of(pojo.getBrand() + pojo.getCategory()))
                .collect(Collectors.toSet());
        Set<String> finalDbSet = dbSet;
        Set<BrandPojo> repeatSet = pojoList.stream().filter(e -> !finalDbSet.add(e.getBrand() + e.getCategory()))
                .collect(Collectors.toSet());

        

        if (repeatSet.size() > 0) {
            Set<BrandPojo> formSet = pojoList.stream().filter(e->!repeatSet.contains(e)).collect(Collectors.toSet());
            formSet.forEach(e->{
                BrandForm repeated = ConvertUtil.objectMapper(e, BrandForm.class);
                JSONObject error = new JSONObject(new Gson().toJson(repeated));
                error.put("error", "");
                errorList.put(error);
            });

            repeatSet.forEach(e -> {
                BrandForm repeated = ConvertUtil.objectMapper(e, BrandForm.class);
                JSONObject error = new JSONObject(new Gson().toJson(repeated));
                error.put("error", "DUPLICATE: already exists in db");
                errorList.put(error);
            });

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

        if (searchValue.isPresent() && !searchValue.get().isBlank()) {
            list = service.searchQueryString(start, length, StringUtil.toLowerCase(searchValue.get()));
        } else {
            list = service.getAllPaginated(start, length);
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

        BrandPojo p1 = new BrandPojo();
        try{
            p1 = service.get(p.getBrand(), p.getCategory());
            
        }catch(ApiException e){

        }
        
        if(Objects.nonNull(p1) && p1.getId()!=p.getId()){
            throw new ApiException("DUPLICATE: brand/category already exists");
        }
        
        service.update(id, p);
    }

}
