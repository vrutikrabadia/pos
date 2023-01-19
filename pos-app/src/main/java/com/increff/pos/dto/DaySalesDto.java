package com.increff.pos.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.DaySalesData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.DaySalesForm;
import com.increff.pos.pojo.DaySalesPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.DaySalesService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class DaySalesDto {
    
    @Autowired
    private DaySalesService service;


    public void add(DaySalesForm form) throws ApiException{
        ValidateUtil.validateForms(form);
        
        DaySalesPojo pojo = ConvertUtil.objectMapper(form, DaySalesPojo.class);
           

        service.add(pojo);
    }

    public SelectData<DaySalesData> getAll(Integer start, Integer length, Integer draw, Optional<ZonedDateTime> startDate, Optional<ZonedDateTime> endDate) throws ApiException{
        List<DaySalesData> dataList = new ArrayList<DaySalesData>();

        if(startDate.isPresent() && Objects.nonNull(startDate.get()) && endDate.isPresent() && Objects.nonNull(endDate.get())){
            return this.getInDateRange(start, length, draw, startDate.get(), endDate.get());
        }

        List<DaySalesPojo> pojoList = service.getAllPaginated(start, length);
    
        for(DaySalesPojo pojo: pojoList){
            DaySalesData data = ConvertUtil.objectMapper(pojo, DaySalesData.class); 
            
            dataList.add(data);
        }

        SelectData<DaySalesData> result = new SelectData<DaySalesData>();
        result.setData(dataList);
        Integer totalEntries = service.getTotalEntries();
        result.setRecordsFiltered(totalEntries);
        result.setRecordsTotal(totalEntries);
        result.setDraw(draw);
        return result;
    }

    public SelectData<DaySalesData> getInDateRange(Integer start, Integer length, Integer draw, ZonedDateTime startDate, ZonedDateTime endDate) throws ApiException{
        List<DaySalesData> dataList = new ArrayList<DaySalesData>();
        ZonedDateTime sDate = startDate;
        ZonedDateTime eDate = endDate;
        

        if(sDate.compareTo(eDate)>0){
            throw new ApiException("start date should be less than end date");
        }
        List<DaySalesPojo> pojoList = service.getByDateRange(sDate, eDate,start, length);

        
        for(DaySalesPojo pojo: pojoList){
            DaySalesData data = ConvertUtil.objectMapper(pojo, DaySalesData.class);
            
            dataList.add(data);
            
        }

        SelectData<DaySalesData> result = new SelectData<DaySalesData>();
        result.setData(dataList);
        Integer totalEntries = service.getTotalEntriesinDateRange(sDate, eDate);
        result.setRecordsFiltered(totalEntries);
        result.setRecordsTotal(totalEntries);
        result.setDraw(draw);
        return result;
    }
}
