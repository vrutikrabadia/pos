package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;

@Service
public class InventoryService {
    
    @Autowired
    private InventoryDao dao;

    @Transactional
    public void add(InventoryPojo p){
        dao.insert(p);
    }

    @Transactional
    public InventoryPojo get(int id) throws ApiException{
        return getCheck(id);
    } 


    @Transactional
    public boolean checkDuplicate(int id) throws ApiException{
        InventoryPojo p = dao.selectById(id);
        if(p == null){
            return false;
        }
        return true;
    }

    @Transactional
    public InventoryPojo getCheck(int id) throws ApiException{
        InventoryPojo p = dao.selectById(id);
        if(p == null){
            throw new ApiException("Inventory does not exist for thr product");
        }
        return p;
    }

    @Transactional
    public List<InventoryPojo> getAll(){
        return dao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id, InventoryPojo p) throws ApiException{
        InventoryPojo p1 = getCheck(id);

        p1.setQuantity(p.getQuantity());

        dao.update(p1);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void reduceQuantity(int id, int quantity) throws ApiException{
        InventoryPojo p = getCheck(id);

        if(p.getQuantity() < quantity){
            throw new ApiException("Insufficienf Inventory");
        }

        p.setQuantity(p.getQuantity()-quantity);

        dao.update(p);
    } 

    @Transactional(rollbackOn = ApiException.class)
    public void increaseQuantity(int id, int quantity) throws ApiException{
        InventoryPojo p = getCheck(id);


        p.setQuantity(p.getQuantity()+quantity);

        dao.update(p);
    } 
}
