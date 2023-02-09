package com.increff.pos.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.DaySalesData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.pojo.PosDaySales;
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
        List<DaySalesData> daySalesDataList = new ArrayList<DaySalesData>();

        List<PosDaySales> daySalesPojoList = service.getAllPaginated(start, length);
    
        for(PosDaySales daySalesPojo: daySalesPojoList){
            DaySalesData data = ConvertUtil.objectMapper(daySalesPojo, DaySalesData.class); 
            
            daySalesDataList.add(data);
        }

        Integer totalEntries = service.getTotalEntries();
        return new SelectData<DaySalesData>(daySalesDataList, draw, totalEntries, totalEntries);
    }

    public SelectData<DaySalesData> getInDateRange(Integer start, Integer length, Integer draw, ZonedDateTime startDate, ZonedDateTime endDate) throws ApiException{
        List<DaySalesData> daySalesDataList = new ArrayList<DaySalesData>();

        if(startDate.compareTo(endDate)>0){
            throw new ApiException("Start Date should be less than End Date.");
        }
        List<PosDaySales> daySalesPojoList = service.getByDateRange(startDate, endDate,start, length);

        
        for(PosDaySales daySalesPojo: daySalesPojoList){
            DaySalesData data = ConvertUtil.objectMapper(daySalesPojo, DaySalesData.class);
            
            daySalesDataList.add(data);
        }

        Integer totalEntries = service.getTotalEntriesinDateRange(startDate, endDate);
        return new SelectData<DaySalesData>(daySalesDataList, draw, totalEntries, totalEntries);
    }

    
    
    @Scheduled(fixedDelay = 1000*60*60*24)
    public void daySalesScheduler(){
        //get start of day and end of day of previous day
        ZonedDateTime start = DateTimeProvider.getInstance().timeNow().minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime end = DateTimeProvider.getInstance().timeNow().minusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        List<OrderPojo> orderPojoList = oService.getInDateRange(start, end);

        List<Integer> orderIdList = orderPojoList.stream().map(OrderPojo::getId).collect(Collectors.toList());

        List<OrderItemsPojo> itemsPojoList = oItemsService.getInColumn(Arrays.asList("orderId"), Arrays.asList(orderIdList));
        
        Integer itemCount = itemsPojoList.stream().collect(Collectors.summingInt(OrderItemsPojo::getQuantity));
        Double totalRevenue = itemsPojoList.stream().mapToDouble(i->i.getQuantity()*i.getSellingPrice()).sum();

        PosDaySales daySalesPojo = new PosDaySales();

        daySalesPojo.setDate(start);
        daySalesPojo.setInvoicedItemsCount(itemCount);
        daySalesPojo.setTotalRevenue(totalRevenue);
        daySalesPojo.setInvoicedOrderCount(orderIdList.size());

        service.add(daySalesPojo);

    }
}
