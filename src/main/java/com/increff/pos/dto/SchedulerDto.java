package com.increff.pos.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.SchedulerData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.SchedulerForm;
import com.increff.pos.pojo.SchedulerPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.SchedulerService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class SchedulerDto {
    
    @Autowired
    private SchedulerService service;


    public void add(SchedulerForm form) throws ApiException{
        ValidateUtil.validateForms(form);
        
        SchedulerPojo pojo = ConvertUtil.objectMapper(form, SchedulerPojo.class);
           

        service.add(pojo);
    }

    public SelectData<SchedulerData> getAll(Integer start, Integer length, Integer draw, Optional<String> startDate, Optional<String> endDate) throws ApiException{
        List<SchedulerData> dataList = new ArrayList<SchedulerData>();

        if(startDate.isPresent() && !startDate.get().isBlank() && endDate.isPresent() && !endDate.get().isBlank()){
            return this.getInDateRange(start, length, draw, startDate.get(), endDate.get());
        }

        List<SchedulerPojo> pojoList = service.getAll(start/length, length);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
    
        for(SchedulerPojo pojo: pojoList){
            String strDate = dateFormat.format(pojo.getDate());
            SchedulerData data = ConvertUtil.objectMapper(pojo, SchedulerData.class);  
            data.setDate(strDate);
            
            dataList.add(data);
        }

        SelectData<SchedulerData> result = new SelectData<SchedulerData>();
        result.setData(dataList);
        Integer totalEntries = service.getTotalEntries();
        result.setRecordsFiltered(totalEntries);
        result.setRecordsTotal(totalEntries);
        result.setDraw(draw);
        return result;
    }

    public SelectData<SchedulerData> getInDateRange(Integer start, Integer length, Integer draw, String startDate, String endDate) throws ApiException{
        List<SchedulerData> dataList = new ArrayList<SchedulerData>();

        Date sDate;
        Date eDate;
        
        try{
            sDate = Date.from(LocalDate.parse(startDate).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            eDate = Date.from(LocalDate.parse(endDate).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception e){
            throw new ApiException("Error converting date");
        }
        

        if(sDate.compareTo(eDate)>0){
            throw new ApiException("start date should be less than end date");
        }
        List<SchedulerPojo> pojoList = service.getByDateRange(sDate, eDate,start/length, length);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
        
        for(SchedulerPojo pojo: pojoList){
            String strDate = dateFormat.format(pojo.getDate());
            SchedulerData data = ConvertUtil.objectMapper(pojo, SchedulerData.class);  
            data.setDate(strDate);
            
            dataList.add(data);
            
        }

        SelectData<SchedulerData> result = new SelectData<SchedulerData>();
        result.setData(dataList);
        Integer totalEntries = service.getTotalEntriesinDateRange(sDate, eDate);
        result.setRecordsFiltered(totalEntries);
        result.setRecordsTotal(totalEntries);
        result.setDraw(draw);
        return result;
    }
}
