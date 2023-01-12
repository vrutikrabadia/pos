package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.ProductBulkData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.data.ProductSelectData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductBulkPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class ProductDto {

    @Autowired
    private ProductService service;
    @Autowired
    private BrandService bService;

    public void add(ProductForm form) throws ApiException {
        BrandPojo brandP = bService.get(form.getBrand(), form.getCategory());

        ProductPojo p = ConvertUtil.objectMapper(form, ProductPojo.class);
        p.setBrandCat(brandP.getId());
        
        StringUtil.normaliseProduct(p);
        
        ValidateUtil.validateProduct(p);

        if (service.checkBarCode(0, p.getBarcode())) {
            throw new ApiException("DUPLICATE BARCODE: Product with barcode already exists.");
        }

        service.add(p);
    }

    public List<ProductBulkData> bulkAdd(List<ProductForm> list){
        List<ProductBulkData> errorList = new ArrayList<ProductBulkData>();
        List<ProductPojo> pojoList = new ArrayList<ProductPojo>();
        
        HashMap<String, Integer> brandCatMap = new HashMap<String, Integer>();
        HashMap<Integer, List<String>> idToBrandCat = new HashMap<Integer, List<String>>();
        
        for(ProductForm form: list){
            ProductPojo pojo = ConvertUtil.objectMapper(form, ProductPojo.class);

            if(brandCatMap.containsKey(form.getBrand()+form.getCategory())){
                pojo.setBrandCat(brandCatMap.get(form.getBrand()+form.getCategory()));
            }

            else{
                BrandPojo brandP = new BrandPojo();  
                try{
                    brandP = bService.get(form.getBrand(), form.getCategory());
                }
                catch(ApiException e){
                    ProductBulkData error = new ProductBulkData();
                    error = ConvertUtil.objectMapper(form, ProductBulkData.class);
                    error.setError(e.getMessage());
                    errorList.add(error);
                    continue;
                }
                finally{
                    brandCatMap.put(brandP.getBrand()+brandP.getCategory(), brandP.getId());
                    idToBrandCat.put(brandP.getId(), Arrays.asList(brandP.getBrand(), brandP.getCategory()));
                    pojo.setBrandCat(brandP.getId());
                }
            }

            StringUtil.normaliseProduct(pojo);
            try{
                ValidateUtil.validateProduct(pojo);
            }
            catch(ApiException e){
                ProductBulkData error = new ProductBulkData();
                error = ConvertUtil.objectMapper(form, ProductBulkData.class);
                error.setError(e.getMessage());
                errorList.add(error);
                continue;
            }

            pojoList.add(pojo);
            
        }

        List<ProductBulkPojo> serviceError = service.bulkAdd(pojoList);

        for(ProductBulkPojo error: serviceError){
            ProductBulkData errorData = ConvertUtil.objectMapper(error, ProductBulkData.class);
            List<String> brandCat = idToBrandCat.get(error.getBrandCat());
            errorData.setBrand(brandCat.get(0));
            errorData.setCategory(brandCat.get(1));
            errorList.add(errorData);
        }

        return errorList;

    }

    public ProductData get(Integer id) throws ApiException {
        ProductPojo p = service.get(id);

        BrandPojo brandP = bService.get(p.getBrandCat());
        ProductData pData = ConvertUtil.objectMapper(p, ProductData.class);

        pData.setBrand(brandP.getBrand());
        pData.setCategory(brandP.getCategory());

        return pData;
    }

    public ProductSelectData getAll(Integer pageNo, Integer pageSize, Integer draw) throws ApiException {
        List<ProductPojo> list = service.getAll(pageNo, pageSize);
        List<ProductData> list1 = new ArrayList<ProductData>();
        for (ProductPojo p : list) {

            BrandPojo brandP = bService.get(p.getBrandCat());
            ProductData pData = ConvertUtil.objectMapper(p, ProductData.class);

            pData.setBrand(brandP.getBrand());
            pData.setCategory(brandP.getCategory());

            list1.add(pData);
        }

        ProductSelectData result = new ProductSelectData();
        result.setData(list1);
        result.setDraw(draw);
        Integer totalRecords = service.getTotalEntries();
        result.setRecordsFiltered(totalRecords);
        result.setRecordsTotal(totalRecords);
        return result;
    }

    public void update(Integer id, ProductForm form) throws ApiException {

        BrandPojo brandP = bService.get(form.getBrand(), form.getCategory());

        ProductPojo p = ConvertUtil.objectMapper(form, ProductPojo.class);
        p.setBrandCat(brandP.getId());

        StringUtil.normaliseProduct(p);

        ValidateUtil.validateProduct(p);

        if (service.checkBarCode(id, p.getBarcode())) {
            throw new ApiException("DUPLICATE BARCODE: Product with barcode already exists.");
        }

        service.update(id, p);
    }

}
