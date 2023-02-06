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
        List<DaySalesData> dataList = new ArrayList<DaySalesData>();

        List<PosDaySales> pojoList = service.getAllPaginated(start, length);
    
        for(PosDaySales pojo: pojoList){
            DaySalesData data = ConvertUtil.objectMapper(pojo, DaySalesData.class); 
            
            dataList.add(data);
        }

        Integer totalEntries = service.getTotalEntries();
       
        return new SelectData<DaySalesData>(dataList, totalEntries, totalEntries, draw);
    }

    public SelectData<DaySalesData> getInDateRange(Integer start, Integer length, Integer draw, ZonedDateTime startDate, ZonedDateTime endDate) throws ApiException{
        List<DaySalesData> dataList = new ArrayList<DaySalesData>();
        ZonedDateTime sDate = startDate;
        ZonedDateTime eDate = endDate;
        

        if(sDate.compareTo(eDate)>0){
            throw new ApiException("start date should be less than end date");
        }
        List<PosDaySales> pojoList = service.getByDateRange(sDate, eDate,start, length);

        
        for(PosDaySales pojo: pojoList){
            DaySalesData data = ConvertUtil.objectMapper(pojo, DaySalesData.class);
            
            dataList.add(data);
            
        }

        Integer totalEntries = service.getTotalEntriesinDateRange(sDate, eDate);
     
        return new SelectData<DaySalesData>(dataList, totalEntries, totalEntries, draw);
    }

    
    
    @Scheduled(fixedDelay = 1000*60*60*24)
    public void scheduler(){
        //get start of day and end of day of previous day
        ZonedDateTime start = DateTimeProvider.getInstance().timeNow().minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime end = DateTimeProvider.getInstance().timeNow().minusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        PosDaySales daySalesPojo = service.getByDate(start);

        Integer currentItemCount = 0;
        Double currentTotalRevenue = 0.0;
        Integer currentOrderCount = 0;
        if(daySalesPojo!=null){
            currentItemCount = daySalesPojo.getInvoicedItemsCount();
            currentTotalRevenue = daySalesPojo.getTotalRevenue();
            currentOrderCount = daySalesPojo.getInvoicedOrderCount();
        }

        List<OrderPojo> orderList = oService.getInDateRange(start, end);

        List<Integer> orderId = orderList.stream().map(OrderPojo::getId).collect(Collectors.toList());

        List<OrderItemsPojo> itemsList = oItemsService.getInColumn(Arrays.asList("orderId"), Arrays.asList(orderId));
        
        Integer itemCount = itemsList.stream().collect(Collectors.summingInt(OrderItemsPojo::getQuantity));
        Double totalRevenue = itemsList.stream().mapToDouble(i->i.getQuantity()*i.getSellingPrice()).sum();

        PosDaySales daySales = new PosDaySales();

        daySales.setDate(start);
        daySales.setInvoicedItemsCount(itemCount-currentItemCount);
        daySales.setTotalRevenue(totalRevenue-currentTotalRevenue);
        daySales.setInvoicedOrderCount(orderId.size()-currentOrderCount);

        service.add(daySales);

    }
}
