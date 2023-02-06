package com.increff.pos.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

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

    public void bulkAdd(List<BrandPojo> list) {

        for (BrandPojo brand : list) {
                add(brand); 
        }
    }

    public BrandPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    public List<BrandPojo> getAllPaginated(Integer offset, Integer pageSize) {
        return dao.selectAllPaginated(offset, pageSize);
    }

    public List<BrandPojo> getAll() {
        return dao.selectAll();
    }

    public BrandPojo get(String brand, String category) throws ApiException {
        return getcheck(brand, category);
    }

    public void update(Integer id, BrandPojo p) throws ApiException {
        BrandPojo b1 = getCheck(id);
        b1.setBrand(p.getBrand());
        b1.setCategory(p.getCategory());
    }

    public BrandPojo getcheck(String brand, String category) throws ApiException {
        BrandPojo pojo = dao.selectByBrandCategory(brand, category);
        if (Objects.isNull(pojo)) {
            throw new ApiException("brand/category does not exist: " + brand + "/" + category);
        }
        return pojo;
    }

    public Integer getTotalEntries() {
        return dao.getTotalEntries();
    }

    public List<BrandPojo> searchQueryString(Integer pageNo, Integer pageSize,String queryString){
        List<String> columnList = Arrays.asList("brand", "category");
        return dao.selectQueryString(pageNo, pageSize,queryString, columnList );
    }

    public <T> List<BrandPojo> getInColumn(List<String> column,List<List<T>> values){
        return dao.selectByColumnUsingIn(column, values);
    }

    protected BrandPojo getCheck(Integer id) throws ApiException {
        BrandPojo b = dao.selectByColumn("id", id);
        if (Objects.isNull(b)) {
            throw new ApiException("brand/category with given ID does not exist, id: " + id);
        }
        return b;
    }


}
