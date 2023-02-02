package com.increff.pos.service;

import java.util.Arrays;
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

    public void add(ProductPojo p) {
        dao.insert(p);
    }

    public void bulkAdd(List<ProductPojo> list) throws ApiException{
        
        for (ProductPojo prod : list) {
           
            add(prod);
        }

    }

    public ProductPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    public ProductPojo get(String barcode) throws ApiException {
        ProductPojo p1 = dao.selectByColumn("barcode", barcode, ProductPojo.class);
        if (Objects.isNull(p1)) {
            throw new ApiException("Product with bar code does not exist");
        }
        return p1;
    }

    public List<ProductPojo> getAllPaginated(Integer offset, Integer pageSize) {
        return dao.selectAllPaginated(offset, pageSize, ProductPojo.class);
    }

    public List<ProductPojo> getAll() {
        return dao.selectAll(ProductPojo.class);
    }

    public void update(Integer id, ProductPojo p) throws ApiException {
        ProductPojo p1 = getCheck(id);

        p1.setBarcode(p.getBarcode());
        p1.setName(p.getName());
        p1.setBrandCat(p.getBrandCat());
        p1.setMrp(p.getMrp());
    }

    public List<ProductPojo> getByBrandCat(Integer brandCat){
        return dao.selectMultiple("brandCat", brandCat, ProductPojo.class);
    }

    public List<ProductPojo> getByQueryString(Integer pageNo, Integer pageSize,String searchQuery){
        List<String> columnList = Arrays.asList("name", "barcode");

        return dao.selectQueryString(pageNo, pageSize, searchQuery, columnList, ProductPojo.class);

    }

    public ProductPojo getCheck(Integer id) throws ApiException {

        ProductPojo p = dao.selectByColumn("id",id, ProductPojo.class);
        if (Objects.isNull(p)) {
            throw new ApiException("product does not exist with id: " + id);
        }
        return p;
    }

    public Integer getTotalEntries() {
        return dao.getTotalEntries(ProductPojo.class);
    }

    public <T> List<ProductPojo> getInColumn(List<String> column, List<List<T>>values){
        return dao.selectByColumnUsingIn(column, values, ProductPojo.class);
    }

    public boolean checkBarCode(Integer id, String barcode) {
        ProductPojo p = dao.selectByColumn("barcode", barcode, ProductPojo.class);
        if (Objects.isNull(p)) {
            return false;
        }
        if (p.getId() == id) {
            return false;
        }
        return true;
    }
}
