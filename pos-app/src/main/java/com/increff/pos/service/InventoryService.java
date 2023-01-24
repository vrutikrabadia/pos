package com.increff.pos.service;

import java.util.List;

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

     
    public void add(InventoryPojo p){
        dao.insert(p);
    }

    //REFACTOR: after review
    public void bulkAdd(List<InventoryPojo> list){
        for(InventoryPojo inv: list){
            try{
                getCheck(inv.getId());
            }
            catch(ApiException e){
                add(inv);
                continue;
            }

            try{
                increaseQuantity(inv.getId(), inv.getQuantity());
            }
            catch(ApiException e){
                continue;
            }
        }
    }

    public InventoryPojo get(Integer id) throws ApiException{
        return getCheck(id);
    } 
    
     
    public List<InventoryPojo> getAllPaginated(Integer offset, Integer pageSize){
        return dao.selectAllPaginated(offset, pageSize, InventoryPojo.class);
    }

    public List<InventoryPojo> getAll(){
        return dao.selectAll(InventoryPojo.class);
    }

    public void update(Integer id, InventoryPojo p) throws ApiException{
        InventoryPojo p1 = getCheck(id);
        p1.setQuantity(p.getQuantity());
    }

    
    public InventoryPojo getCheck(Integer id) throws ApiException{
        InventoryPojo p = dao.selectByColumn("id", id, InventoryPojo.class);
        if(p == null){
            throw new ApiException("Inventory does not exist for thr product");
        }
        return p;
    }
     
    public void reduceQuantity(Integer id, Integer quantity) throws ApiException{
        InventoryPojo p = getCheck(id);
        if(p.getQuantity() < quantity){
            throw new ApiException("Insufficienf Inventory");
        }
        p.setQuantity(p.getQuantity()-quantity);
    } 

     
    public void increaseQuantity(Integer id, Integer quantity) throws ApiException{
        InventoryPojo p = getCheck(id);
        p.setQuantity(p.getQuantity()+quantity);
    } 

    public Integer getTotalEntries(){
        return dao.getTotalEntries(InventoryPojo.class);
    }
}
