package com.increff.pos.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.DaySalesDao;
import com.increff.pos.pojo.DaySalesPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class DaySalesService {
    
    @Autowired
    private DaySalesDao dao;

    public void add(DaySalesPojo daySalesPojo){
        DaySalesPojo check = getByDate(daySalesPojo.getDate());

        if(Objects.isNull(check)){

            dao.insert(daySalesPojo); 
        }
        else{
            update(daySalesPojo);
        }      
    }

    public List<DaySalesPojo> getByDateRange(ZonedDateTime startDate, ZonedDateTime endDate, Integer offset, Integer pageSize){
        return dao.selectInDateRange(startDate, endDate, offset, pageSize);
    }

    public List<DaySalesPojo> getAllPaginated(Integer offset, Integer pageSize){
        return dao.selectAllPaginated(offset, pageSize);
    }

    public DaySalesPojo getByDate(ZonedDateTime date){

        // TODO: use select by date : NOT POSSIBLE NOW: sql timezone not consistent and keeps changing
        List<DaySalesPojo> daySalesList = dao.selectInDateRange(date.withHour(0).withMinute(0).withSecond(0), date.withHour(23).withMinute(59).withSecond(59), 0, 1);
        return daySalesList.isEmpty()?null:daySalesList.get(0);
    }

    public void update(DaySalesPojo pojo){
        DaySalesPojo currentRow = getByDate(pojo.getDate());
        
        currentRow.setInvoicedItemCount(pojo.getInvoicedItemCount());
        currentRow.setInvoicedOrderCount(pojo.getInvoicedOrderCount());
        currentRow.setTotalRevenue(pojo.getTotalRevenue());

    }

    public Integer getTotalEntriesInDateRange(ZonedDateTime startDate, ZonedDateTime endDate){
        return dao.countTotalEntriesInDateRange(startDate, endDate);
    }

    public Integer getTotalEntries() {
        return dao.countTotalEntries();
    }

}
