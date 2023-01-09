package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

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

@Component
public class InventoryDto {
    
    @Autowired
    private InventoryService service;

    @Autowired
    private ProductService pService;

    public void add(InventoryForm f) throws ApiException{
        InventoryPojo p = ConvertUtil.convertInventoryFormtoPojo(f);

        ProductPojo p1 = pService.getCheck(p.getId());

        if(p.getQuantity() < 0){
            throw new ApiException("Quantity should be non negative");
        }
        if(p1==null){
            throw new ApiException("Product does not exist");
        }

        if(service.checkDuplicate(p.getId())){
            throw new ApiException("Inventory for the product already exist");
        }

        service.add(p);
    }

    public InventoryData get(String barCode) throws ApiException{
        InventoryForm f = new InventoryForm();
        f.setBarCode(barCode);
        InventoryPojo p = ConvertUtil.convertInventoryFormtoPojo(f);
        InventoryPojo p1 = service.get(p.getId()); 
        return ConvertUtil.convertInventoryPojoToData(p1);
    }

    public List<InventoryData> getAll() throws ApiException{
        List<InventoryData> list = new ArrayList<InventoryData>();
        List<InventoryPojo> list1 = service.getAll();
        
        for(InventoryPojo p: list1){
            list.add(ConvertUtil.convertInventoryPojoToData(p));
        }

        return list;
    }

    public void update(InventoryForm f) throws ApiException{

        InventoryPojo p = ConvertUtil.convertInventoryFormtoPojo(f);

        if(p.getQuantity() < 0){
            throw new ApiException("Quantity should be non negative");
        }

        InventoryPojo p2 = service.getCheck(p.getId());
        p2.setQuantity(p.getQuantity());
        
        service.update(p.getId(), p2);
    }


}
