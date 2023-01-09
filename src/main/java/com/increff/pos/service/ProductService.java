package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;

@Service
public class ProductService {
    

    @Autowired
    private ProductDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo p) throws ApiException{
        
        if(checkBarCode(p.getBarCode())){
            throw new ApiException("DUPLICATE BARCODE: Product with barcode already exists.");
        }

        dao.insert(p);
    }


    @Transactional
    public ProductPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    @Transactional
    public List<ProductPojo> getAll(){
        return dao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id, ProductPojo p) throws ApiException{
        ProductPojo p1 = getCheck(id);

        if(checkBarCode(p.getBarCode())){
            throw new ApiException("DUPLICATE BARCODE: Product with barcode already exists.");
        }
        
        p1.setBarCode(p.getBarCode());
        p1.setName(p.getName());
        p1.setBrandCat(p.getBrandCat());
        p1.setMrp(p.getMrp());
    }
    
    @Transactional
    private ProductPojo getCheck(int id) throws ApiException{
        ProductPojo p = dao.selectById(id);
        if(p==null){
            throw new ApiException("product does not exist with id: "+id);
        }
        return p;
    }

    @Transactional
    private boolean checkBarCode(String barCode){
        ProductPojo p = dao.selectByBarCode(barCode);
        if(p==null){
            return false;
        }
        return true;
    }
}
