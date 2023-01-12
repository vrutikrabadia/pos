package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.InventoryBulkData;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.InventorySelectData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class InventoryDto {

    @Autowired
    private InventoryService service;

    @Autowired
    private ProductService pService;

    public void add(InventoryForm form) throws ApiException {

        ProductPojo p1 = pService.get(form.getBarcode());
        InventoryPojo p = ConvertUtil.objectMapper(form, InventoryPojo.class);

        if (Objects.isNull(p1)) {
            throw new ApiException("Product does not exist");
        }

        p.setId(p1.getId());

        ValidateUtil.validateInventory(p);

        try {
            service.getCheck(p.getId());
        } catch (ApiException e) {
            service.add(p);
            return;
        }
        InventoryPojo iPojo = service.get(p.getId());
        service.increaseQuantity(iPojo.getId(), p.getQuantity());

    }

    public List<InventoryBulkData> bulkAdd(List<InventoryForm> list) {
        List<InventoryBulkData> errorList = new ArrayList<InventoryBulkData>();
        List<InventoryPojo> pojoList = new ArrayList<InventoryPojo>();

        HashMap<String, Integer> barcodeToId = new HashMap<String, Integer>();

        for (InventoryForm form : list) {
            InventoryPojo pojo = ConvertUtil.objectMapper(form, InventoryPojo.class);

            if (barcodeToId.containsKey(form.getBarcode())) {
                pojo.setId(barcodeToId.get(form.getBarcode()));
            } else {

                ProductPojo product = new ProductPojo();

                try {
                    product = pService.get(form.getBarcode());
                } catch (ApiException e) {
                    InventoryBulkData error = new InventoryBulkData();
                    error = ConvertUtil.objectMapper(form, InventoryBulkData.class);
                    error.setError(e.getMessage());
                    errorList.add(error);
                    continue;
                } finally {
                    barcodeToId.put(product.getBarcode(), product.getId());
                    pojo.setId(product.getId());
                }
            }

            try {
                ValidateUtil.validateInventory(pojo);
            } catch (ApiException e) {
                InventoryBulkData error = new InventoryBulkData();
                error = ConvertUtil.objectMapper(form, InventoryBulkData.class);
                error.setError(e.getMessage());
                errorList.add(error);
                continue;
            }
            pojoList.add(pojo);
        }

        service.bulkAdd(pojoList);

        return errorList;
    }

    public InventoryData get(String barcode) throws ApiException {

        ProductPojo p = pService.get(barcode);
        InventoryPojo p1 = service.get(p.getId());

        InventoryData d = ConvertUtil.objectMapper(p1, InventoryData.class);
        d.setBarcode(barcode);
        return d;
    }

    public InventorySelectData getAll(Integer pageNo, Integer pageSize, Integer draw) throws ApiException {
        List<InventoryData> list = new ArrayList<InventoryData>();
        List<InventoryPojo> list1 = service.getAll(pageNo, pageSize);

        for (InventoryPojo p : list1) {
            ProductPojo prodPojo = pService.get(p.getId());
            InventoryData iData = ConvertUtil.objectMapper(p, InventoryData.class);
            iData.setBarcode(prodPojo.getBarcode());
            list.add(iData);
        }

        InventorySelectData result = new InventorySelectData();
        result.setData(list);
        result.setDraw(draw);
        Integer totalEntries = service.getTotalEntries();
        result.setRecordsFiltered(totalEntries);
        result.setRecordsTotal(totalEntries);
        return result;
    }

    public void update(InventoryForm form) throws ApiException {

        ProductPojo p1 = pService.get(form.getBarcode());
        InventoryPojo p = ConvertUtil.objectMapper(form, InventoryPojo.class);
        p.setId(p1.getId());

        ValidateUtil.validateInventory(p);

        InventoryPojo p2 = service.getCheck(p.getId());
        p2.setQuantity(p.getQuantity());

        service.update(p.getId(), p2);
    }

}
