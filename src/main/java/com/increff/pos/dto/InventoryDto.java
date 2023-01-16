package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ExceptionUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class InventoryDto {

    @Autowired
    private InventoryService service;

    @Autowired
    private ProductService pService;

    public void add(InventoryForm form) throws ApiException {

        ValidateUtil.validateForms(form);

        ProductPojo p1 = pService.get(form.getBarcode());
        InventoryPojo p = ConvertUtil.objectMapper(form, InventoryPojo.class);

        if (Objects.isNull(p1)) {
            throw new ApiException("Product does not exist");
        }

        p.setId(p1.getId());

        try {
            service.getCheck(p.getId());
        } catch (ApiException e) {
            service.add(p);
            return;
        }
        InventoryPojo iPojo = service.get(p.getId());
        service.increaseQuantity(iPojo.getId(), p.getQuantity());

    }

    public void bulkAdd(List<InventoryForm> list) throws ApiException{
        JSONArray errorList = new JSONArray();
        List<InventoryPojo> pojoList = new ArrayList<InventoryPojo>();

        HashMap<String, Integer> barcodeToId = new HashMap<String, Integer>();

        for (InventoryForm form : list) {

            try {
                ValidateUtil.validateForms(form);
                ;
            } catch (ConstraintViolationException e) {
                JSONObject error = new JSONObject(new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create()
                        .toJson(form));
                error.put("error", ExceptionUtil.getValidationMessage(e));
                errorList.put(error);
                continue;
            }

            InventoryPojo pojo = ConvertUtil.objectMapper(form, InventoryPojo.class);

            if (barcodeToId.containsKey(form.getBarcode())) {
                pojo.setId(barcodeToId.get(form.getBarcode()));
            } else {

                ProductPojo product = new ProductPojo();

                try {
                    product = pService.get(form.getBarcode());
                } catch (ApiException e) {
                    JSONObject error = new JSONObject(new GsonBuilder()
                            .excludeFieldsWithoutExposeAnnotation()
                            .create()
                            .toJson(form));
                    error.put("error", "Brand and category does not exist");
                    errorList.put(error);
                    continue;
                } finally {
                    barcodeToId.put(product.getBarcode(), product.getId());
                    pojo.setId(product.getId());
                }
            }

            pojoList.add(pojo);
        }

        service.bulkAdd(pojoList);

        if (!errorList.isEmpty()) {
            throw new ApiException(errorList.toString());
        }
    }

    public InventoryData get(String barcode) throws ApiException {

        ProductPojo p = pService.get(barcode);
        InventoryPojo p1 = service.get(p.getId());

        InventoryData d = ConvertUtil.objectMapper(p1, InventoryData.class);
        d.setBarcode(barcode);
        return d;
    }

    public SelectData<InventoryData> getAll(Integer start, Integer length, Integer draw, Optional<String> searchValue) throws ApiException {
        List<InventoryData> list = new ArrayList<InventoryData>();
        List<InventoryPojo> list1 = new ArrayList<InventoryPojo>();


        if(searchValue.isPresent() && !searchValue.get().isBlank()){

            Integer totalProds = pService.getTotalEntries();
            List<ProductPojo> pList = pService.getByQueryString(0, totalProds, searchValue.get());
            
            for(ProductPojo prod: pList){
                InventoryPojo inv = new InventoryPojo();
                try{
                    inv = service.get(prod.getId());
                }
                catch(ApiException e){
                    continue;
                }
                list1.add(inv);
            }
        }
        else{
            list1 = service.getAll(start / length, length);
        }

        for (InventoryPojo p : list1) {
            ProductPojo prodPojo = pService.get(p.getId());
            InventoryData iData = ConvertUtil.objectMapper(p, InventoryData.class);
            iData.setBarcode(prodPojo.getBarcode());
            list.add(iData);
        }

        SelectData<InventoryData> result = new SelectData<InventoryData>();
        result.setData(list);
        result.setDraw(draw);
        Integer totalEntries = service.getTotalEntries();
        result.setRecordsFiltered(totalEntries);
        result.setRecordsTotal(totalEntries);
        return result;
    }

    public void update(InventoryForm form) throws ApiException {
        ValidateUtil.validateForms(form);

        ProductPojo p1 = pService.get(form.getBarcode());
        InventoryPojo p = ConvertUtil.objectMapper(form, InventoryPojo.class);
        p.setId(p1.getId());

        InventoryPojo p2 = service.getCheck(p.getId());
        p2.setQuantity(p.getQuantity());

        service.update(p.getId(), p2);
    }

}
