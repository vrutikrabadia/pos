package com.increff.pos.dto;


import com.google.gson.Gson;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.*;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InventoryDto {

    @Autowired
    private InventoryService service;

    @Autowired
    private ProductService pService;

    public void add(InventoryForm inventoryForm) throws ApiException {
        StringUtil.normalise(inventoryForm, InventoryForm.class);
        ValidateUtil.validateForms(inventoryForm);

        ProductPojo productPojo = pService.getCheckByBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo = ConvertUtil.objectMapper(inventoryForm, InventoryPojo.class);

        if (Objects.isNull(productPojo)) {
            throw new ApiException("Product does not exist with barcode: " + inventoryForm.getBarcode());
        }

        inventoryPojo.setProductId(productPojo.getId());

        try {
            service.getCheck(inventoryPojo.getProductId());
        } catch (ApiException apiException) {
            service.add(inventoryPojo);
            return;
        }
        InventoryPojo checkPojo = service.getCheck(inventoryPojo.getProductId());
        service.increaseQuantity(checkPojo.getProductId(), inventoryPojo.getQuantity());

    }

    public void bulkAdd(List<InventoryForm> inventoryFormList) throws ApiException {

        StringUtil.normaliseList(inventoryFormList, InventoryForm.class);
        ValidateUtil.validateList(inventoryFormList);

        checkListDuplications(inventoryFormList);

        List<InventoryPojo> inventoryPojoList = getCheckProductAndConvertToPojo(inventoryFormList);

        service.bulkAdd(inventoryPojoList);

    }
    
    
    
    public List<InventoryPojo> getCheckProductAndConvertToPojo(List<InventoryForm> inventoryFormList) throws ApiException {
        List<InventoryPojo> inventoryPojoList = new ArrayList<InventoryPojo>();
        JSONArray errorList = new JSONArray();
        Integer errorCount = 0;
        for (InventoryForm inventoryForm : inventoryFormList) {

            ProductPojo p1;
            try {
                p1 = pService.getCheckByBarcode(inventoryForm.getBarcode());
            } catch (ApiException apiException) {
                errorList.put(ExceptionUtil.generateJSONErrorObject(apiException.getMessage(), inventoryForm));
                errorCount++;
                continue;
            }

            errorList.put(ExceptionUtil.generateJSONErrorObject("", inventoryForm));

            InventoryPojo inventoryPojo = ConvertUtil.objectMapper(inventoryForm, InventoryPojo.class);

            inventoryPojo.setProductId(p1.getId());

            inventoryPojoList.add(inventoryPojo);
        }

        if (errorCount > 0) {
            throw new ApiException(errorList.toString());
        }
        return inventoryPojoList;
    }


    public InventoryData get(String barcode) throws ApiException {
        barcode = StringUtil.toLowerCase(barcode);
        ProductPojo productPojo = pService.getCheckByBarcode(barcode);
        InventoryPojo checkPojo = service.getCheck(productPojo.getId());

        InventoryData inventoryData = ConvertUtil.objectMapper(checkPojo, InventoryData.class);
        inventoryData.setBarcode(barcode);
        return inventoryData;
    }

    public SelectData<InventoryData> getAll(Integer start, Integer length, Integer draw, String searchValue) throws ApiException {
        List<InventoryData> inventoryDataList = new ArrayList<InventoryData>();
        List<InventoryPojo> inventoryPojoList = new ArrayList<InventoryPojo>();


        if (Objects.nonNull(searchValue) && !searchValue.isEmpty()) {

            Integer totalProducts = pService.getTotalEntries();
            List<ProductPojo> productPojoList = pService.getByQueryString(0, totalProducts, StringUtil.toLowerCase(searchValue));

            for (ProductPojo productPojo : productPojoList) {
                InventoryPojo inventoryPojo = new InventoryPojo();

                inventoryPojo = service.get(productPojo.getId());

                if (Objects.nonNull(inventoryPojo)) {
                    inventoryPojoList.add(inventoryPojo);
                }
            }
        } else {
            inventoryPojoList = service.getAllPaginated(start, length);
        }

        for (InventoryPojo p : inventoryPojoList) {
            ProductPojo productPojo = pService.getCheckById(p.getProductId());
            InventoryData inventoryData = ConvertUtil.objectMapper(p, InventoryData.class);
            inventoryData.setBarcode(productPojo.getBarcode());
            inventoryDataList.add(inventoryData);
        }

        Integer totalEntries = service.getTotalEntries();

        return new SelectData<InventoryData>(inventoryDataList, draw, totalEntries, totalEntries);
    }

    public void update(InventoryForm inventoryForm) throws ApiException {
        StringUtil.normalise(inventoryForm, InventoryForm.class);
        ValidateUtil.validateForms(inventoryForm);

        ProductPojo productPojo = pService.getCheckByBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo = ConvertUtil.objectMapper(inventoryForm, InventoryPojo.class);
        inventoryPojo.setProductId(productPojo.getId());

        InventoryPojo inventoryPojo1 = service.getCheck(inventoryPojo.getProductId());
        inventoryPojo1.setQuantity(inventoryPojo.getQuantity());

        service.update(inventoryPojo.getProductId(), inventoryPojo1);
    }

    protected void checkListDuplications(List<InventoryForm> inventoryFormList) throws ApiException {
        Set<String> barcodeRecievedSet = new HashSet<String>();

        Set<InventoryForm> repeatedProductFormSet = inventoryFormList.stream().filter(e -> !barcodeRecievedSet.add(e.getBarcode()))
                .collect(Collectors.toSet());

        if (repeatedProductFormSet.size() > 0) {
            ExceptionUtil.generateBulkAddExceptionList("DUPLICATE entry in same file", inventoryFormList, repeatedProductFormSet);
        }
    }
}
