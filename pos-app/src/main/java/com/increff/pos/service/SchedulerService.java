package com.increff.pos.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.SchedulerDao;
import com.increff.pos.pojo.SchedulerPojo;
import com.increff.pos.util.ConvertUtil;

@Service
@Transactional(rollbackOn = ApiException.class)
public class SchedulerService {
    
    @Autowired
    private SchedulerDao dao;

    public void add(SchedulerPojo pojo){
        pojo.setDate(ConvertUtil.getDateWithoutTimeUsingCalendar());
        SchedulerPojo check = getByDate(pojo.getDate()); 

        if(Objects.isNull(check)){
            pojo.setOrderCount(1);
            dao.insert(pojo); 
        }
        else{
            update(pojo);
        }      
    }

    public List<SchedulerPojo> getByDateRange(Date startDate, Date endDate, Integer offset, Integer pageSize){
        return dao.selectInDateRange(startDate, endDate, offset, pageSize);
    }

    public List<SchedulerPojo> getAll(Integer offset, Integer pageSize){
        return dao.selectAll(offset, pageSize, SchedulerPojo.class);
    }

    public SchedulerPojo getByDate(Date date){
        return dao.selectByDate(date);
    }

    public void update(SchedulerPojo pojo){
        SchedulerPojo row = dao.selectByDate(pojo.getDate());

        row.setItemsCount(row.getItemsCount()+pojo.getItemsCount());
        row.setOrderCount(row.getOrderCount()+1);
        row.setTotalRevenue(row.getTotalRevenue()+pojo.getTotalRevenue());

    }

    public Integer getTotalEntriesinDateRange(Date startDate, Date endDate){
        return dao.getTotalEntriesInDateRange(startDate, endDate);
    }

    public Integer getTotalEntries(){
        return dao.getTotalEntries(SchedulerPojo.class);
    }
}
