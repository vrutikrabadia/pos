package com.increff.pos.service;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductService {
    

    @Autowired
    private ProductDao dao;

     
    public void add(ProductPojo p){
        dao.insert(p);
    }

    public ProductPojo get(Integer id) throws ApiException{
        return getCheck(id);
    }

      
    public ProductPojo get(String barcode) throws ApiException{
        ProductPojo p1 = dao.selectByBarcode(barcode);
        if(Objects.isNull(p1)){
            throw new ApiException("Product with bar code does not exist");
        }
        return p1;
    }

    public List<ProductPojo> getAll(Integer pageNo, Integer pageSize){
        Integer offset = pageNo*pageSize;
        return dao.selectAll(offset, pageSize, ProductPojo.class);
    }
 
    public void update(Integer id, ProductPojo p) throws ApiException{
        ProductPojo p1 = getCheck(id);

        p1.setBarcode(p.getBarcode());
        p1.setName(p.getName());
        p1.setBrandCat(p.getBrandCat());
        p1.setMrp(p.getMrp());
    }
    
     
    public ProductPojo getCheck(Integer id) throws ApiException{
        
        ProductPojo p = dao.selectById(id);
        if(Objects.isNull(p)){
            throw new ApiException("product does not exist with id: "+id);
        }
        return p;
    }

    public boolean checkBarCode(Integer id, String barcode){        
        ProductPojo p = dao.selectByBarcode(barcode);
        if(Objects.isNull(p)){
            return false;
        }
        if(p.getId() == id){
            return false;
        }
        return true;
    }
}
