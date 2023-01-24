package com.increff.pos.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.DaySalesData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.pojo.DaySalesPojo;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.DaySalesService;
import com.increff.pos.service.OrderItemsService;
import com.increff.pos.service.OrderService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.DateTimeProvider;

@Component

public class DaySalesDto {
    
    @Autowired
    private DaySalesService service;

    @Autowired
    private OrderService oService;

    @Autowired
    private OrderItemsService oItemsService;


    public SelectData<DaySalesData> getAll(Integer start, Integer length, Integer draw) throws ApiException{
        List<DaySalesData> dataList = new ArrayList<DaySalesData>();

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

    
    
    @Scheduled(cron = "0 44 11 * * *")
    public void scheduler(){
        //get start of day and end of day of previous day
        ZonedDateTime start = DateTimeProvider.getInstance().timeNow().minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime end = DateTimeProvider.getInstance().timeNow().minusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        
        //get total sales of previous day
        List<OrderPojo> orderList = oService.getInDateRange(start, end);

        List<Integer> orderId = orderList.stream().map(OrderPojo::getId).collect(Collectors.toList());

        List<OrderItemsPojo> itemsList = oItemsService.getInColumn(Arrays.asList("orderId"), Arrays.asList(orderId));
        System.out.println(itemsList.stream().map(OrderItemsPojo::getId).collect(Collectors.toList()));
        
        Integer itemCount = itemsList.stream().collect(Collectors.summingInt(OrderItemsPojo::getQuantity));
        Double totalRevenue = itemsList.stream().mapToDouble(i->i.getQuantity()*i.getSellingPrice()).sum();

        DaySalesPojo daySales = new DaySalesPojo();

        daySales.setDate(start);
        daySales.setItemsCount(itemCount);
        daySales.setTotalRevenue(totalRevenue);
        daySales.setOrderCount(orderId.size());

        service.add(daySales);

    }
}
