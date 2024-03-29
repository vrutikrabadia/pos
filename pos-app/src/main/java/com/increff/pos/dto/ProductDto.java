package com.increff.pos.dto;

import com.google.gson.Gson;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ExceptionUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidateUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

@Component
public class ProductDto {

    @Autowired
    private ProductService service;
    @Autowired
    private BrandService bService;

    public void add(ProductForm productForm) throws ApiException {
        StringUtil.normalise(productForm, ProductForm.class);
        ValidateUtil.validateForms(productForm);

        BrandPojo brandPojo = bService.getCheckByBrandCategory(productForm.getBrand(), productForm.getCategory());

        ProductPojo productPojo = ConvertUtil.objectMapper(productForm, ProductPojo.class);
        productPojo.setBrandCat(brandPojo.getId());

        ProductPojo check = service.getByBarcode(productPojo.getBarcode());
        if (Objects.nonNull(check)) {
            throw new ApiException("DUPLICATE BARCODE: Product with barcode already exists.");
        }

        service.add(productPojo);
    }
    
    public void bulkAdd(List<ProductForm> productFormList) throws ApiException {
        StringUtil.normaliseList(productFormList, ProductForm.class);
        ValidateUtil.validateList(productFormList);

        checkListDuplications(productFormList);

        List<ProductPojo> productPojoList = checkAndConvertBrandAndCategory(productFormList);
        
        checkDbDuplicate(productPojoList);

        service.bulkAdd(productPojoList);

    }

    protected List<ProductPojo> checkAndConvertBrandAndCategory(List<ProductForm> productFormList) throws ApiException{
        JSONArray errorList = new JSONArray();
        List<ProductPojo> productPojoList = new ArrayList<ProductPojo>();
        int errorCount = 0;

        for(ProductForm productForm : productFormList){
            BrandPojo brandPojo = null;
            try {
                brandPojo = bService.getCheckByBrandCategory(productForm.getBrand(), productForm.getCategory());
            } catch (ApiException apiException) {
                errorCount+=1;
                errorList.put(ExceptionUtil.generateJSONErrorObject(apiException.getMessage(), productForm));
                continue;
            }

            errorList.put(ExceptionUtil.generateJSONErrorObject("", productForm));

            ProductPojo productPojo = ConvertUtil.objectMapper(productForm, ProductPojo.class);
            productPojo.setBrandCat(brandPojo.getId());
            productPojoList.add(productPojo);
        };

        if(errorCount>0){
            throw new ApiException(errorList.toString());
        }

        return productPojoList;
    }

    protected void checkListDuplications(List<ProductForm> productFormList) throws ApiException {
        Set<String> recievedBarcodeSet = new HashSet<String>();


        Set<ProductForm> repeatSet = productFormList.stream().filter(e -> !recievedBarcodeSet.add(e.getBarcode()))
                .collect(Collectors.toSet());


        if (repeatSet.size() > 0) {
            ExceptionUtil.generateBulkAddExceptionList("DUPLICATE: Entry already exist in same file.", productFormList, repeatSet);
        }
    }

    protected void checkDbDuplicate(List<ProductPojo> productPojoList) throws ApiException{
        
        List<String> barcodeList = productPojoList.stream().map(ProductPojo::getBarcode).collect(Collectors.toList());
        List<ProductPojo> currentExistingProductList = service.getInColumns(singletonList("barcode"), singletonList(barcodeList));
        Set<String> existingBarcodeSet = new HashSet<String>();
        existingBarcodeSet = currentExistingProductList.stream().flatMap(pojo -> Stream.of(pojo.getBarcode()))
                .collect(Collectors.toSet());
        Set<String> finalDbSet = existingBarcodeSet;
        Set<ProductPojo> repeatSet = productPojoList.stream().filter(e -> !finalDbSet.add(e.getBarcode()))
                .collect(Collectors.toSet());
        
        if (repeatSet.size() > 0) {
           ExceptionUtil.generateBulkAddExceptionPojo("DUPLICATE: Product already exist with same Barcode.", productPojoList, repeatSet, ProductForm.class);
        }
    }

    public ProductData get(Integer productId) throws ApiException {
        ProductPojo productPojo = service.getCheckById(productId);

        BrandPojo brandPojo = bService.getCheckById(productPojo.getBrandCat());
        ProductData productData = ConvertUtil.objectMapper(productPojo, ProductData.class);

        productData.setBrand(brandPojo.getBrand());
        productData.setCategory(brandPojo.getCategory());

        return productData;
    }

    public SelectData<ProductData> getAll(Integer start, Integer length, Integer draw, String searchValue) throws ApiException {
        List<ProductPojo> productPojoList = new ArrayList<ProductPojo>();
        List<ProductData> productDataList = new ArrayList<ProductData>();

        if(Objects.nonNull(searchValue)&&!searchValue.isEmpty()){
            Integer totalBrands = bService.getTotalEntries();
            
            productPojoList = service.getByQueryString(start, length, searchValue);
            
            List<BrandPojo> brandPojoList = bService.searchQueryString(0, totalBrands, StringUtil.toLowerCase(searchValue));

            for(BrandPojo brandPojo: brandPojoList){
                productPojoList.addAll(service.getByBrandCatId(brandPojo.getId()));
            }
        }
        else{
            productPojoList = service.getAllPaginated(start, length);
        }

        for (ProductPojo productPojo : productPojoList) {

            BrandPojo brandPojo = bService.getCheckById(productPojo.getBrandCat());
            ProductData productData = ConvertUtil.objectMapper(productPojo, ProductData.class);

            productData.setBrand(brandPojo.getBrand());
            productData.setCategory(brandPojo.getCategory());

            productDataList.add(productData);
        }

        Integer totalProducts = service.getTotalEntries();
        return new SelectData<ProductData>(productDataList, draw, totalProducts, totalProducts);
    }

    public void update(ProductForm productForm) throws ApiException {
        StringUtil.normalise(productForm, ProductForm.class);
        ValidateUtil.validateForms(productForm);

        ProductPojo productPojo = ConvertUtil.objectMapper(productForm, ProductPojo.class);

        ProductPojo check = service.getCheckByBarcode(productPojo.getBarcode());
        productPojo.setId(check.getId());
        service.update(productPojo);
    }

}
