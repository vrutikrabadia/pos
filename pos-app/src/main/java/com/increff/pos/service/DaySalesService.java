package com.increff.pos.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.DaySalesDao;
import com.increff.pos.pojo.DaySalesPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class DaySalesService {
    
    @Autowired
    private DaySalesDao dao;

    public void add(DaySalesPojo pojo){
        DaySalesPojo check = getByDate(pojo.getDate()); 

        if(Objects.isNull(check)){

            dao.insert(pojo); 
        }
        else{
            update(pojo);
        }      
    }

    public List<DaySalesPojo> getByDateRange(ZonedDateTime startDate, ZonedDateTime endDate, Integer offset, Integer pageSize){
        return dao.selectInDateRange(startDate, endDate, offset, pageSize);
    }

    public List<DaySalesPojo> getAllPaginated(Integer offset, Integer pageSize){
        return dao.selectAllPaginated(offset, pageSize, DaySalesPojo.class);
    }

    public DaySalesPojo getByDate(ZonedDateTime date){
        List<DaySalesPojo> list = dao.selectInDateRange(date.withHour(0).withMinute(0).withSecond(0), date.withHour(23).withMinute(59).withSecond(59), 0, 1);
        return list.isEmpty()?null:list.get(0);
    }

    public void update(DaySalesPojo pojo){
        DaySalesPojo row = getByDate(pojo.getDate());

        Integer newItemsCount = row.getItemsCount()+pojo.getItemsCount();
        Double newRevenue = row.getTotalRevenue()+pojo.getTotalRevenue();
        row.setItemsCount(newItemsCount);
        row.setOrderCount(row.getOrderCount()+1);
        row.setTotalRevenue(newRevenue);

    }

    public Integer getTotalEntriesinDateRange(ZonedDateTime startDate, ZonedDateTime endDate){

        return dao.getTotalEntriesInDateRange(startDate, endDate);
    }

    public Integer getTotalEntries(){
        return dao.getTotalEntries(DaySalesPojo.class);
    }

    
}
