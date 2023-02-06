package com.increff.pos.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.DaySalesDao;
import com.increff.pos.pojo.PosDaySales;

@Service
@Transactional(rollbackOn = ApiException.class)
public class DaySalesService {
    
    @Autowired
    private DaySalesDao dao;

    public void add(PosDaySales pojo){
        PosDaySales check = getByDate(pojo.getDate()); 


        if(Objects.isNull(check)){

            dao.insert(pojo); 
        }
        else{
            update(pojo);
        }      
    }

    public List<PosDaySales> getByDateRange(ZonedDateTime startDate, ZonedDateTime endDate, Integer offset, Integer pageSize){
        return dao.selectInDateRange(startDate, endDate, offset, pageSize);
    }

    public List<PosDaySales> getAllPaginated(Integer offset, Integer pageSize){
        return dao.selectAllPaginated(offset, pageSize);
    }

    public PosDaySales getByDate(ZonedDateTime date){
        List<PosDaySales> list = dao.selectInDateRange(date.withHour(0).withMinute(0).withSecond(0), date.withHour(23).withMinute(59).withSecond(59), 0, 1);
        return list.isEmpty()?null:list.get(0);
    }

    public void update(PosDaySales pojo){
        PosDaySales row = getByDate(pojo.getDate());

        Integer newItemsCount = row.getInvoicedItemsCount()+pojo.getInvoicedItemsCount();
        Double newRevenue = row.getTotalRevenue()+pojo.getTotalRevenue();
        Integer newOrderCount = row.getInvoicedOrderCount()+pojo.getInvoicedOrderCount();
        row.setInvoicedItemsCount(newItemsCount);
        row.setInvoicedOrderCount(newOrderCount);
        row.setTotalRevenue(newRevenue);

    }

    public Integer getTotalEntriesinDateRange(ZonedDateTime startDate, ZonedDateTime endDate){

        return dao.getTotalEntriesInDateRange(startDate, endDate);
    }

    public Integer getTotalEntries(){
        return dao.getTotalEntries();
    }

    
}
