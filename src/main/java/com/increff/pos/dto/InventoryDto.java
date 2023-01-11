package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.InventoryData;
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

    public void add(InventoryForm form) throws ApiException{
        
        ProductPojo p1 = pService.get(form.getBarcode());
        InventoryPojo p = ConvertUtil.objectMapper(form, InventoryPojo.class);
        p.setId(p1.getId());

        ValidateUtil.validateInventory(p);

        if(Objects.isNull(p1)){
            throw new ApiException("Product does not exist");
        }

        try{
            service.getCheck(p.getId());
        }
        catch(ApiException e){
            service.add(p);
            return;
        }
        
        InventoryPojo iPojo = service.get(p.getId());

        service.increaseQuantity(iPojo.getId(), p.getQuantity());
        
    }

    public InventoryData get(String barcode) throws ApiException{

        ProductPojo p = pService.get(barcode);
        InventoryPojo p1 = service.get(p.getId()); 

        InventoryData d = ConvertUtil.objectMapper(p1, InventoryData.class);
        d.setBarcode(barcode);
        return d;
    }

    public List<InventoryData> getAll(Integer pageNo, Integer pageSize) throws ApiException{
        List<InventoryData> list = new ArrayList<InventoryData>();
        List<InventoryPojo> list1 = service.getAll(pageNo, pageSize);
        
        for(InventoryPojo p: list1){
            ProductPojo prodPojo = pService.get(p.getId());
            InventoryData iData = ConvertUtil.objectMapper(p, InventoryData.class);
            iData.setBarcode(prodPojo.getBarcode());
            list.add(iData);
        }

        return list;
    }

    public void update(InventoryForm form) throws ApiException{

        ProductPojo p1 = pService.get(form.getBarcode());
        InventoryPojo p = ConvertUtil.objectMapper(form, InventoryPojo.class);
        p.setId(p1.getId());

        ValidateUtil.validateInventory(p);

        InventoryPojo p2 = service.getCheck(p.getId());
        p2.setQuantity(p.getQuantity());
        
        service.update(p.getId(), p2);
    }


}
