package com.increff.pos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandBulkPojo;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.ConvertUtil;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandService {
    
    @Autowired
    private BrandDao dao;

     
    public void add(BrandPojo p) {
        dao.insert(p);
    }

    public List<BrandBulkPojo> bulkAdd(List<BrandPojo> list){
        List<BrandBulkPojo> errorList = new ArrayList<BrandBulkPojo>();
    
        for(BrandPojo brand: list){
            try{
                getcheck(brand.getBrand(), brand.getCategory());
            }
            catch(ApiException e){
                add(brand);
                continue;
            }
            BrandBulkPojo error = new BrandBulkPojo();
            error = ConvertUtil.objectMapper(brand, BrandBulkPojo.class);
            error.setError("Brand and category already exists");

            errorList.add(error);
        }
    
        return errorList;
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
