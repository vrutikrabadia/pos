package com.increff.pos.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandService {

    @Autowired
    private BrandDao dao;

    public void add(BrandPojo brandPojo) {
        dao.insert(brandPojo);
    }

    public void bulkAdd(List<BrandPojo> list) {
        for (BrandPojo brand : list) {
                add(brand); 
        }
    }

    public BrandPojo getById(Integer bCatId) throws ApiException {
        return dao.selectByColumn("id", bCatId);

    }

    public BrandPojo getByBrandAndCategory(String brand, String category) {
        
        return dao.selectByBrandCategory(brand, category);
    }

    public List<BrandPojo> getAllPaginated(Integer offset, Integer pageSize) {
        return dao.selectAllPaginated(offset, pageSize);
    }

    public List<BrandPojo> getAll() {
        return dao.selectAll();
    }

    public void update(Integer id, BrandPojo brandPojo) throws ApiException {
        BrandPojo checkPojo = getCheckById(id);
        checkPojo.setBrand(brandPojo.getBrand());
        checkPojo.setCategory(brandPojo.getCategory());
    }


    public Integer getTotalEntries() {
        return dao.countTotalEntries();
    }

    public List<BrandPojo> searchQueryString(Integer pageNo, Integer pageSize,String queryString){
        List<String> columnList = Arrays.asList("brand", "category");
        return dao.selectByQueryString(pageNo, pageSize,queryString, columnList );
    }

    public <T> List<BrandPojo> getInColumns(List<String> columnList,List<List<T>> valueList) {
        return dao.selectInColumns(columnList, valueList);
    }

    
    public BrandPojo getCheckByBrandCategory(String brand, String category) throws ApiException {
        BrandPojo pojo = getByBrandAndCategory(brand, category);
        if (Objects.isNull(pojo)) {
            throw new ApiException("Brand and Category does not exist: " + brand + "/" + category);
        }
        return pojo;
    }

    public BrandPojo getCheckById(Integer bCatId) throws ApiException {
        BrandPojo brandPojo = getById(bCatId);    
        if (Objects.isNull(brandPojo)) {
            throw new ApiException("Brand and Category with given ID does not exist, id: " + bCatId);
        }
        return brandPojo;
    }

}
