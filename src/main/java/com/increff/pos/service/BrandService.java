package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;

@Service
public class BrandService {
    
    @Autowired
    private BrandDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(BrandPojo b) throws ApiException{
        normalize(b);
        if(StringUtil.isEmpty(b.getBrand()) || StringUtil.isEmpty(b.getCategory())){
            throw new ApiException("Brand of category cannot be empty");
        }
        if(checkDuplicate(b)){
            throw new ApiException("brand/category already exist: "+b.getBrand()+"/"+b.getCategory());
        }
        dao.insert(b);
    }

    @Transactional
    public boolean checkDuplicate(BrandPojo b){
        BrandPojo b1 = dao.select(b.getBrand(), b.getCategory());
        if(b1!=null){
            return true;
        }
        return false;
    }

    @Transactional
    public void delete(int id){
        dao.delete(id);
    }       

    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    @Transactional
    public BrandPojo getCheck(int id) throws ApiException{
        BrandPojo b = dao.select(id);
        if(b==null){
            throw new ApiException("brand/category with given ID does not exist, id: "+id);
        }
        return b;
    }

    @Transactional
    public List<BrandPojo> getAll(){
        return dao.selectAll();
    }

    
    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo get(String brand, String category) throws ApiException{
        return getcheck(brand, category);
    }
    
    @Transactional 
    public BrandPojo getcheck(String brand, String category) throws ApiException{
        BrandPojo b = dao.select(brand, category);
        if(b==null){
            throw new ApiException("brand/category does not exist: "+brand+"/"+category);
        }
        return b;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id,BrandPojo b) throws ApiException{
        normalize(b);
        if(checkDuplicate(b)){
            throw new ApiException("brand/category already exist: "+b.getBrand()+"/"+b.getCategory());
        }
        BrandPojo b1 = getCheck(id);
        b1.setBrand(b.getBrand());
        b1.setCategory(b.getCategory());
        dao.update(b1);
    }

    protected static void normalize(BrandPojo b){
        b.setBrand(StringUtil.toLowerCase(b.getBrand().trim()));
    }
}
