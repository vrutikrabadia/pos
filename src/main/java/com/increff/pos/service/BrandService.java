package com.increff.pos.service;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandService {
    
    @Autowired
    private BrandDao dao;

     
    public void add(BrandPojo p) {
        dao.insert(p);
    }

    public void bulkAdd(List<BrandPojo> list) throws ApiException{
        JSONArray errorList1 = new JSONArray();
        
        for(BrandPojo brand: list){
            try{
                getcheck(brand.getBrand(), brand.getCategory());
            }
            catch(ApiException e){
                add(brand);
                continue;
            }
            JSONObject error1 = new JSONObject();
            error1.put("brand", brand.getBrand());
            error1.put("category", brand.getCategory());
            error1.put("error", "Brand and category already exists");
            errorList1.put(error1);
        }

        if(errorList1.length() > 0){
            throw new ApiException(errorList1.toString());
        }

    }
    
    public BrandPojo get(Integer id) throws ApiException{
        return getCheck(id);
    }

    public List<BrandPojo> getAll(Integer pageNo, Integer pageSize){
        Integer offset = pageNo*pageSize;
        return dao.selectAll(offset, pageSize, BrandPojo.class);
    }

    public BrandPojo get(String brand, String category) throws ApiException{
        return getcheck(brand, category);
    }

    public void update(Integer id,BrandPojo p) throws ApiException{
        BrandPojo b1 = getCheck(id);
        b1.setBrand(p.getBrand());
        b1.setCategory(p.getCategory());
    }
    
    public BrandPojo getcheck(String brand, String category) throws ApiException{
        BrandPojo pojo = dao.selectByBrandCategory(brand, category);
        if(Objects.isNull(pojo)){
            throw new ApiException("brand/category does not exist: "+brand+"/"+category);
        }
        return pojo;
    }

    public Integer getTotalEntries(){
        return dao.getTotalEntries(BrandPojo.class);
    }
    
    private BrandPojo getCheck(Integer id) throws ApiException{
        BrandPojo b = dao.selectById(id);
        if(Objects.isNull(b)){
            throw new ApiException("brand/category with given ID does not exist, id: "+id);
        }
        return b;
    }
}
