package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class ProductDto {

    @Autowired
    private ProductService service;
    @Autowired
    private BrandService bService;

    public void add(ProductForm form) throws ApiException {
        StringUtil.normalise(form, ProductForm.class);
        ValidateUtil.validateForms(form);

        BrandPojo brandP = bService.get(form.getBrand(), form.getCategory());

        ProductPojo p = ConvertUtil.objectMapper(form, ProductPojo.class);
        p.setBrandCat(brandP.getId());


        if (service.checkBarCode(0, p.getBarcode())) {
            throw new ApiException("DUPLICATE BARCODE: Product with barcode already exists.");
        }

        service.add(p);
    }
    
    public void bulkAdd(List<ProductForm> list) throws ApiException {
        StringUtil.normaliseList(list, ProductForm.class);
        ValidateUtil.validateList(list);

        checkFileDuplications(list);

        List<ProductPojo> pojoList = checkAndConvertBrandAndCategory(list);
        
        checkDbDuplicate(pojoList);

        service.bulkAdd(pojoList);

    }

    protected List<ProductPojo> checkAndConvertBrandAndCategory(List<ProductForm> list) throws ApiException{
        JSONArray errorList = new JSONArray();
        List<ProductPojo> pojoList = new ArrayList<ProductPojo>();
        int errorCount = 0;

        for(ProductForm e : list){
            BrandPojo brandP = null;
            try {
                brandP = bService.get(e.getBrand(), e.getCategory());
            } catch (ApiException e1) {
                JSONObject error = new JSONObject(new Gson().toJson(e));      
                errorCount+=1;
                error.put("error", e1.getMessage());
                errorList.put(error);
                continue;
            }

            JSONObject error = new JSONObject(new Gson().toJson(e));
            error.put("error", "");
            errorList.put(error);

            ProductPojo p = ConvertUtil.objectMapper(e, ProductPojo.class);
            p.setBrandCat(brandP.getId());
            pojoList.add(p);
        };

        if(errorCount>0){
            throw new ApiException(errorList.toString());
        }

        return pojoList;
    }

    protected void checkFileDuplications(List<ProductForm> formList) throws ApiException {
        JSONArray errorList = new JSONArray();
        Set<String> fileSet = new HashSet<String>();


        Set<ProductForm> repeatSet = formList.stream().filter(e -> !fileSet.add(e.getBarcode()))
                .collect(Collectors.toSet());


        if (repeatSet.size() > 0) {
            Set<ProductForm> formSet = formList.stream().filter(e->!repeatSet.contains(e)).collect(Collectors.toSet());
            
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

    protected void checkDbDuplicate(List<ProductPojo> pojoList) throws ApiException{
        JSONArray errorList = new JSONArray();
        
        List<String> barcodeList = pojoList.stream().map(ProductPojo::getBarcode).collect(Collectors.toList());
        List<ProductPojo> currentExixting = service.getInColumn(Arrays.asList("barcode"),Arrays.asList(barcodeList));
        System.out.println(currentExixting.toString());
        Set<String> dbSet = new HashSet<String>();
        dbSet = currentExixting.stream().flatMap(pojo -> Stream.of(pojo.getBarcode()))
                .collect(Collectors.toSet());
        System.out.println(dbSet);
        Set<String> finalDbSet = dbSet;
        Set<ProductPojo> repeatSet = pojoList.stream().filter(e -> !finalDbSet.add(e.getBarcode()))
                .collect(Collectors.toSet());
        
        System.out.println(repeatSet);
        

        if (repeatSet.size() > 0) {
            Set<ProductPojo> formSet = pojoList.stream().filter(e->!repeatSet.contains(e)).collect(Collectors.toSet());
            formSet.forEach(e->{
                ProductForm repeated = ConvertUtil.objectMapper(e, ProductForm.class);
                JSONObject error = new JSONObject(new Gson().toJson(repeated));
                error.put("error", "");
                errorList.put(error);
            });

            repeatSet.forEach(e -> {
                ProductForm repeated = ConvertUtil.objectMapper(e, ProductForm.class);
                JSONObject error = new JSONObject(new Gson().toJson(repeated));
                error.put("error", "DUPLICATE: already exists in db");
                errorList.put(error);
            });

            throw new ApiException(errorList.toString());
        }
    }

    public ProductData get(Integer id) throws ApiException {
        ProductPojo p = service.get(id);

        BrandPojo brandP = bService.get(p.getBrandCat());
        ProductData pData = ConvertUtil.objectMapper(p, ProductData.class);

        pData.setBrand(brandP.getBrand());
        pData.setCategory(brandP.getCategory());

        return pData;
    }

    public SelectData<ProductData> getAll(Integer start, Integer length, Integer draw, Optional<String> searchValue) throws ApiException {
        List<ProductPojo> list = new ArrayList<ProductPojo>();
        List<ProductData> list1 = new ArrayList<ProductData>();

        if(searchValue.isPresent() && !searchValue.get().isBlank()){
            Integer brandSize = bService.getTotalEntries();
            
            list = service.getByQueryString(start, length, searchValue.get());
            
            List<BrandPojo> bList = bService.searchQueryString(0, brandSize, StringUtil.toLowerCase(searchValue.get()));

            for(BrandPojo bPojo: bList){
                list.addAll(service.getByBrandCat(bPojo.getId()));
            }
        }
        else{
            list = service.getAllPaginated(start, length);
        }

        for (ProductPojo p : list) {

            BrandPojo brandP = bService.get(p.getBrandCat());
            ProductData pData = ConvertUtil.objectMapper(p, ProductData.class);

            pData.setBrand(brandP.getBrand());
            pData.setCategory(brandP.getCategory());

            list1.add(pData);
        }

        SelectData<ProductData> result = new SelectData<ProductData>();
        result.setData(list1);
        result.setDraw(draw);
        Integer totalRecords = service.getTotalEntries();
        result.setRecordsFiltered(totalRecords);
        result.setRecordsTotal(totalRecords);
        return result;
    }

    public void update(Integer id, ProductForm form) throws ApiException {
        StringUtil.normalise(form, ProductForm.class);
        ValidateUtil.validateForms(form);

        BrandPojo brandP = bService.get(form.getBrand(), form.getCategory());

        ProductPojo p = ConvertUtil.objectMapper(form, ProductPojo.class);
        p.setBrandCat(brandP.getId());


        if (service.checkBarCode(id, p.getBarcode())) {
            throw new ApiException("DUPLICATE BARCODE: Product with barcode already exists.");
        }

        service.update(id, p);
    }

}
