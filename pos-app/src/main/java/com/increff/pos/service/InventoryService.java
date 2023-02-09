package com.increff.pos.service;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryService {
    
    @Autowired
    private InventoryDao dao;

     
    public void add(InventoryPojo inventoryPojo){
        dao.insert(inventoryPojo);
    }

    public void bulkAdd(List<InventoryPojo> inventoryPojoList){
        for(InventoryPojo inventory: inventoryPojoList){
            try{
                getCheck(inventory.getProductId());
            }
            catch(ApiException apiException){
                add(inventory);
                continue;
            }

            try{
                increaseQuantity(inventory.getProductId(), inventory.getQuantity());
            }
            catch(ApiException e){
            }
        }
    }

    public InventoryPojo get(Integer productId){
        return dao.selectByColumn("productId", productId);
    }

    public InventoryPojo getCheck(Integer productId) throws ApiException{
        InventoryPojo inventoryPojo = get(productId);
        if(Objects.isNull(inventoryPojo)){
            throw new ApiException("Inventory does not exist for thr product");
        }
        return inventoryPojo;
    }
    
     
    public List<InventoryPojo> getAllPaginated(Integer offset, Integer pageSize){
        return dao.selectAllPaginated(offset, pageSize);
    }

    public List<InventoryPojo> getAll(){
        return dao.selectAll();
    }

    public void update(Integer productId, InventoryPojo inventoryPojo) throws ApiException{
        InventoryPojo checkPojo = getCheck(productId);
        checkPojo.setQuantity(inventoryPojo.getQuantity());
    }


    public void checkInventory(Integer productId, Integer quantity) throws ApiException{
        InventoryPojo inventory = getCheck(productId);
        if(inventory.getQuantity() < quantity){
            throw new ApiException("Insufficient Inventory");
        }
    }
     
    public void reduceQuantity(Integer productId, Integer quantity) throws ApiException{
        InventoryPojo inventoryPojo = getCheck(productId);
        if(inventoryPojo.getQuantity() < quantity){
            throw new ApiException("Insufficienf Inventory");
        }
        inventoryPojo.setQuantity(inventoryPojo.getQuantity()-quantity);
    } 

     
    public void increaseQuantity(Integer productId, Integer quantity) throws ApiException{
        InventoryPojo inventoryPojo = getCheck(productId);
        inventoryPojo.setQuantity(inventoryPojo.getQuantity()+quantity);
    } 

    public Integer getTotalEntries(){
        return dao.selectTotalEntries();
    }
}
