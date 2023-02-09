package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ExceptionUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class BrandDto {

    @Autowired
    private BrandService service;

    public void add(BrandForm brandForm) throws ApiException {

        StringUtil.normalise(brandForm, BrandForm.class);
        ValidateUtil.validateForms(brandForm);
        
        BrandPojo brandPojo = ConvertUtil.objectMapper(brandForm, BrandPojo.class);

        if(Objects.isNull(service.getByBrandAndCategory(brandForm.getBrand(), brandForm.getCategory()))){
            service.add(brandPojo);
            return;
        }

        throw new ApiException("Brand and Category already exists.");
    }

    public void bulkAdd(List<BrandForm> brandFormList) throws ApiException {
        StringUtil.normaliseList(brandFormList, BrandForm.class);
        ValidateUtil.validateList(brandFormList);

        checkListDuplications(brandFormList);
        List<BrandPojo> brandPojoList = brandFormList.stream().map(e->ConvertUtil.objectMapper(e, BrandPojo.class)).collect(Collectors.toList());
        checkDbDuplicate(brandPojoList);

        service.bulkAdd(brandPojoList);
    }

    public BrandData getById(Integer bCatId) throws ApiException {
        BrandPojo p = service.getCheckById(bCatId);
        return ConvertUtil.objectMapper(p, BrandData.class);
    }

    public SelectData<BrandData> getAll(Integer start, Integer length, Integer draw, Optional<String> searchValue) {

        List<BrandPojo> pojoList = new ArrayList<BrandPojo>();
        List<BrandData> dataList = new ArrayList<BrandData>();

        if (searchValue.isPresent() && !searchValue.get().isEmpty()) {
            pojoList = service.searchQueryString(start, length, StringUtil.toLowerCase(searchValue.get()));
        } else {
            pojoList = service.getAllPaginated(start, length);
        }

        for (BrandPojo p : pojoList) {
            dataList.add(ConvertUtil.objectMapper(p, BrandData.class));
        }

        Integer totalEntries = service.getTotalEntries();
        return new SelectData<BrandData>(dataList, draw, totalEntries, totalEntries);
    }

    public BrandData get(String brand, String category) throws ApiException {
        brand = StringUtil.toLowerCase(brand);
        category = StringUtil.toLowerCase(category);
        BrandPojo brandPojo = service.getCheckByBrandCategory(brand, category);

        return ConvertUtil.objectMapper(brandPojo, BrandData.class);
    }

    public void update(Integer id, BrandForm brandForm) throws ApiException {

        StringUtil.normalise(brandForm, BrandForm.class);
        ValidateUtil.validateForms(brandForm);
        BrandPojo recievedPojo = ConvertUtil.objectMapper(brandForm, BrandPojo.class);

        BrandPojo fetchedPojo = service.getByBrandAndCategory(recievedPojo.getBrand(), recievedPojo.getCategory());
            
        if(Objects.nonNull(fetchedPojo) && fetchedPojo.getId()!=recievedPojo.getId()){
            throw new ApiException("DUPLICATE: Brand and Category already exists.");
        }
        
        service.update(id, recievedPojo);
    }

    protected void checkListDuplications(List<BrandForm> brandFormList) throws ApiException {
        Set<String> brandCategoryRecievedSet = new HashSet<String>();

        Set<BrandForm> repeatedBrandFormSet = brandFormList.stream().filter(e -> !brandCategoryRecievedSet.add(e.getBrand() +"#"+ e.getCategory()))
                .collect(Collectors.toSet());

        if (repeatedBrandFormSet.size() > 0) {
            ExceptionUtil.generateBulkAddExceptionList( "DUPLICATE entry in same file", brandFormList, repeatedBrandFormSet);
        }
    }

    protected void checkDbDuplicate(List<BrandPojo> brandPojoList) throws ApiException{
        
        List<String> recievedBrandList = brandPojoList.stream().map(BrandPojo::getBrand).collect(Collectors.toList());
        List<BrandPojo> existingBrandList = service.getInColumns(Arrays.asList("brand"),Arrays.asList(recievedBrandList));
        Set<String> existingBrandSet = new HashSet<String>();
        existingBrandSet = existingBrandList.stream().flatMap(pojo -> Stream.of(pojo.getBrand() + pojo.getCategory()))
                .collect(Collectors.toSet());
        Set<String> finalExisitingBrandSet = existingBrandSet;
        Set<BrandPojo> repeatedBrandPojoSet = brandPojoList.stream().filter(e -> !finalExisitingBrandSet.add(e.getBrand() + e.getCategory()))
                .collect(Collectors.toSet());

        if (repeatedBrandPojoSet.size() > 0) {
            ExceptionUtil.generateBulkAddExceptionPojo("DUPLICATE: Brand and Category already exists.", brandPojoList, repeatedBrandPojoSet, BrandForm.class);
        }
    }

    
}
