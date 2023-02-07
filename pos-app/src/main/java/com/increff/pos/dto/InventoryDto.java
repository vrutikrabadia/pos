package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class InventoryDto {

    @Autowired
    private InventoryService service;

    @Autowired
    private ProductService pService;

    public void add(InventoryForm form) throws ApiException {
        StringUtil.normalise(form, InventoryForm.class);
        ValidateUtil.validateForms(form);

        ProductPojo p1 = pService.get(form.getBarcode());
        InventoryPojo p = ConvertUtil.objectMapper(form, InventoryPojo.class);

        if (Objects.isNull(p1)) {
            throw new ApiException("Product does not exist.");
        }

        p.setProductId(p1.getId());

        try {
            service.getCheck(p.getProductId());
        } catch (ApiException e) {
            service.add(p);
            return;
        }
        InventoryPojo iPojo = service.get(p.getProductId());
        service.increaseQuantity(iPojo.getProductId(), p.getQuantity());

    }

    public void bulkAdd(List<InventoryForm> list) throws ApiException{

        StringUtil.normaliseList(list, InventoryForm.class);
        ValidateUtil.validateList(list);

        List<InventoryPojo> pojoList = checkProductandConvert(list);


        service.bulkAdd(pojoList);

        
    }

    public List<InventoryPojo> checkProductandConvert(List<InventoryForm> list) throws ApiException {
        List<InventoryPojo> pojoList = new ArrayList<InventoryPojo>();
        JSONArray errorList = new JSONArray();
        Integer errorCount = 0;
        for(InventoryForm form: list){

            ProductPojo p1;
            try {
                p1 = pService.get(form.getBarcode());
            } catch (ApiException e) {
                JSONObject error = new JSONObject(new Gson().toJson(form));
                error.put("error", e.getMessage());
                errorList.put(error);
                errorCount++;
                continue;
            }

            JSONObject error = new JSONObject(new Gson().toJson(form));
            error.put("error", "");

            InventoryPojo p = ConvertUtil.objectMapper(form, InventoryPojo.class);

            p.setProductId(p1.getId());

            pojoList.add(p);
        }

        if(errorCount > 0){
            throw new ApiException(errorList.toString());
        }
        return pojoList;
    }

    

    public InventoryData get(String barcode) throws ApiException {
        barcode = StringUtil.toLowerCase(barcode);
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
            List<ProductPojo> pList = pService.getByQueryString(0, totalProds, StringUtil.toLowerCase(searchValue.get()));
            
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
            list1 = service.getAllPaginated(start , length);
        }

        for (InventoryPojo p : list1) {
            ProductPojo prodPojo = pService.get(p.getProductId());
            InventoryData iData = ConvertUtil.objectMapper(p, InventoryData.class);
            iData.setBarcode(prodPojo.getBarcode());
            list.add(iData);
        }
;
        Integer totalEntries = service.getTotalEntries();

        return new SelectData<InventoryData>(list, draw, totalEntries, totalEntries);
    }

    public void update(InventoryForm form) throws ApiException {
        StringUtil.normalise(form, InventoryForm.class);
        ValidateUtil.validateForms(form);

        ProductPojo p1 = pService.get(form.getBarcode());
        InventoryPojo p = ConvertUtil.objectMapper(form, InventoryPojo.class);
        p.setProductId(p1.getId());

        InventoryPojo p2 = service.getCheck(p.getProductId());
        p2.setQuantity(p.getQuantity());

        service.update(p.getProductId(), p2);
    }

}
