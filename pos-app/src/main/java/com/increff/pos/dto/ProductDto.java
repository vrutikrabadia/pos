package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ExceptionUtil;
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
        JSONArray errorList = new JSONArray();
        List<ProductPojo> pojoList = new ArrayList<ProductPojo>();

        HashMap<String, Integer> brandCatMap = new HashMap<String, Integer>();
        HashMap<Integer, List<String>> idToBrandCat = new HashMap<Integer, List<String>>();

        for (ProductForm form : list) {
            StringUtil.normalise(form, ProductForm.class);
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

            ProductPojo pojo = ConvertUtil.objectMapper(form, ProductPojo.class);

            if (brandCatMap.containsKey(form.getBrand() + form.getCategory())) {
                pojo.setBrandCat(brandCatMap.get(form.getBrand() + form.getCategory()));
            }

            else {
                BrandPojo brandP = new BrandPojo();
                try {
                    brandP = bService.get(form.getBrand(), form.getCategory());
                } catch (ApiException e) {
                    JSONObject error = new JSONObject(new GsonBuilder()
                            .excludeFieldsWithoutExposeAnnotation()
                            .create()
                            .toJson(form));
                    error.put("error", "Brand and category does not exist");
                    errorList.put(error);
                    continue;
                } finally {
                    brandCatMap.put(brandP.getBrand() + brandP.getCategory(), brandP.getId());
                    idToBrandCat.put(brandP.getId(), Arrays.asList(brandP.getBrand(), brandP.getCategory()));
                    pojo.setBrandCat(brandP.getId());
                }
            }


            pojoList.add(pojo);

        }

        JSONArray serviceErrors = service.bulkAdd(pojoList);
        
        for(int i=0; i<serviceErrors.length(); i++){
            JSONObject obj = serviceErrors.getJSONObject(i);
            List<String> brandCat = idToBrandCat.get(obj.get("brandCat"));
            obj.put("brand", brandCat.get(0));
            obj.put("category", brandCat.get(1));
            obj.remove("brandCat");

        }

        errorList.putAll(serviceErrors);

        if (!errorList.isEmpty()) {
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
