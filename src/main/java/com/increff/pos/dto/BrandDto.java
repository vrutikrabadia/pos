package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.BrandBulkData;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.BrandSelectData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandBulkPojo;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class BrandDto {
    
    @Autowired
    private BrandService service;

    public void add(BrandForm f) throws ApiException{
        BrandPojo p = ConvertUtil.objectMapper(f, BrandPojo.class);
        StringUtil.normaliseBrand(p);
        ValidateUtil.validateBrand(p);

        try{
            service.getcheck(f.getBrand(), f.getCategory());
        }
        catch(ApiException e){
            service.add(p);
            return;
        }

        throw new ApiException("Brand and category already exists");

    }

    public List<BrandBulkData> bulkAdd(List<BrandForm> list){
        List<BrandBulkData> errorList = new ArrayList<BrandBulkData>();

        List<BrandPojo> pojoList = new ArrayList<BrandPojo>();

        for(BrandForm form: list){
            BrandPojo pojo = ConvertUtil.objectMapper(form, BrandPojo.class);

            try{
                ValidateUtil.validateBrand(pojo);
            }
            catch(ApiException e){
                BrandBulkData error = new BrandBulkData();
                error = ConvertUtil.objectMapper(pojo, BrandBulkData.class);
                error.setError(e.getMessage());
                errorList.add(error);
                continue;
            }
            pojoList.add(pojo);
        }

        List<BrandBulkPojo> serviceErrors = service.bulkAdd(pojoList);
        
        for(BrandBulkPojo error: serviceErrors){
            errorList.add(ConvertUtil.objectMapper(error, BrandBulkData.class));
        }


        return errorList;
    }


    public BrandData get(Integer id) throws ApiException{
        BrandPojo p = service.get(id);
        return ConvertUtil.objectMapper(p, BrandData.class);
    }

    public BrandSelectData getAll(Integer pageNo, Integer pageSize, Integer draw){
        List<BrandPojo> list = service.getAll(pageNo, pageSize);
        List<BrandData> list1 = new ArrayList<BrandData>();
        for(BrandPojo p:list){
            list1.add(ConvertUtil.objectMapper(p, BrandData.class));
        }
        
        Integer totalEntries = service.getTotalEntries();

        BrandSelectData result = new BrandSelectData();
        result.setData(list1);
        result.setDraw(draw);
        result.setRecordsFiltered(totalEntries);
        result.setRecordsTotal(totalEntries);
        return result;

    }

    public BrandData get(String brand, String category) throws ApiException{
        BrandPojo p = service.get(brand, category);

        return ConvertUtil.objectMapper(p, BrandData.class);
    }

    public void update(Integer id, BrandForm f) throws ApiException{
        BrandPojo p = ConvertUtil.objectMapper(f, BrandPojo.class);
        StringUtil.normaliseBrand(p);
        ValidateUtil.validateBrand(p);
        service.update(id, p);
    }

}
