package com.increff.pos.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductService {

    @Autowired
    private ProductDao dao;

    public void add(ProductPojo productPojo) {
        dao.insert(productPojo);
    }

    public void bulkAdd(List<ProductPojo> productPojoList) throws ApiException{
        for (ProductPojo product : productPojoList) {
            add(product);
        }
    }

    public ProductPojo getByBarcode(String barcode) {
        return dao.selectByColumn("barcode", barcode);
    }

    public ProductPojo getCheckById(Integer productId) throws ApiException {
        ProductPojo productPojo = getById(productId);
        if (Objects.isNull(productPojo)) {
            throw new ApiException("Product does not exist with id: " + productId);
        }
        return productPojo;
    }

    public ProductPojo getCheckByBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = dao.selectByColumn("barcode", barcode);
        if (Objects.isNull(productPojo)) {

            throw new ApiException("Product with barcode does not exist for barcode: " + barcode);
        }
        return productPojo;
    }

    public ProductPojo getById(Integer productId) {

        return dao.selectByColumn("id",productId);
      
    }

    public List<ProductPojo> getAllPaginated(Integer offset, Integer pageSize) {
        return dao.selectAllPaginated(offset, pageSize);
    }

    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    public void update(ProductPojo productPojo) throws ApiException {
        ProductPojo checkPojo = getCheckById(productPojo.getId());

        checkPojo.setName(productPojo.getName());
        checkPojo.setMrp(productPojo.getMrp());
    }

    public List<ProductPojo> getByBrandCatId(Integer brandCat){
        return dao.selectMultipleEntriesByColumn("brandCat", brandCat);
    }

    public List<ProductPojo> getByQueryString(Integer pageNo, Integer pageSize,String searchQuery){
        List<String> columnList = Arrays.asList("name", "barcode");

        return dao.selectByQueryString(pageNo, pageSize, searchQuery, columnList);

    }

    public Integer getTotalEntries() {
        return dao.countTotalEntries();
    }

    public <T> List<ProductPojo> getInColumns(List<String> column, List<List<T>>values) throws ApiException{
        if(column.size() != values.size()){
            throw new ApiException("Column and Values size mismatch");
        }   
        return dao.selectInColumns(column, values);
    }

    
}
