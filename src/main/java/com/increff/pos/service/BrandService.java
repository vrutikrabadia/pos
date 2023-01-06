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
    public void add(BrandPojo p) throws ApiException{
        normalize(p);
        if(StringUtil.isEmpty(p.getBrand()) || StringUtil.isEmpty(p.getCategory())){
            throw new ApiException("Brand of category cannot be empty");
        }
        if(checkDuplicate(p)){
            throw new ApiException("brand/category already exist: "+p.getBrand()+"/"+p.getCategory());
        }
        dao.insert(p);
    }

    @Transactional
    public boolean checkDuplicate(BrandPojo p){
        BrandPojo b1 = dao.selectByBrandCategory(p.getBrand(), p.getCategory());
        if(b1!=null){
            return true;
        }
        return false;
    }
    

    @Transactional
    public BrandPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    @Transactional
    private BrandPojo getCheck(int id) throws ApiException{
        BrandPojo b = dao.selectById(id);
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
        BrandPojo b = dao.selectByBrandCategory(brand, category);
        if(b==null){
            throw new ApiException("brand/category does not exist: "+brand+"/"+category);
        }
        return b;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id,BrandPojo p) throws ApiException{
        normalize(p);
        if(checkDuplicate(p)){
            throw new ApiException("brand/category already exist: "+p.getBrand()+"/"+p.getCategory());
        }
        BrandPojo b1 = getCheck(id);
        b1.setBrand(p.getBrand());
        b1.setCategory(p.getCategory());
        dao.update(b1);
    }

    protected static void normalize(BrandPojo p){
        p.setBrand(StringUtil.toLowerCase(p.getBrand()));
    }
}
