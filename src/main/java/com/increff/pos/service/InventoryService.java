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

    public InventoryPojo get(Integer id) throws ApiException{
        return getCheck(id);
    } 

    public boolean checkDuplicate(Integer id) throws ApiException{
        InventoryPojo p = dao.selectById(id);
        if(p == null){
            return false;
        }
        return true;
    }

    public InventoryPojo getCheck(Integer id) throws ApiException{
        InventoryPojo p = dao.selectById(id);
        if(p == null){
            throw new ApiException("Inventory does not exist for thr product");
        }
        return p;
    }
     
    public List<InventoryPojo> getAll(){
        return dao.selectAll();
    }

    public void update(Integer id, InventoryPojo p) throws ApiException{
        InventoryPojo p1 = getCheck(id);
        p1.setQuantity(p.getQuantity());
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
}
