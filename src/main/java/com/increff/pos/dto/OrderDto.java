package com.increff.pos.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.util.ConvertUtil;

@Component
public class OrderDto {
    
    @Autowired
    private OrderService service;

    public OrderData add(OrderForm f){
        OrderPojo p = new OrderPojo();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
        String strDate = dateFormat.format(p.getUpdated());  
        OrderData data = ConvertUtil.objectMapper(service.add(p), OrderData.class);
        data.setUpdated(strDate);

        return data;
    }

    public OrderData get(Integer id) throws ApiException{
        OrderPojo p =service.get(id);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
        String strDate = dateFormat.format(p.getUpdated());  
        OrderData data = ConvertUtil.objectMapper(p, OrderData.class);
        data.setUpdated(strDate);

        return data;
    }

    public SelectData<OrderData> getAll(Integer start, Integer length, Integer draw, Optional<String> searchValue){
        List<OrderData> list = new ArrayList<OrderData>();
        List<OrderPojo> list1 = new ArrayList<OrderPojo>();
        if(searchValue.isPresent() && !searchValue.get().isBlank()){
            try{
                list1.add(service.get(Integer.valueOf(searchValue.get())));
            } catch(ApiException e){
                list1 = service.getAll(start/length, length);
            }
        }
        else{
            list1 = service.getAll(start/length, length);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
        
        
        for(OrderPojo p : list1){
            String strDate = dateFormat.format(p.getUpdated());  
            OrderData data = ConvertUtil.objectMapper(p, OrderData.class);
            data.setUpdated(strDate);

            list.add(data);
        }


        SelectData<OrderData> result = new SelectData<OrderData>();
        result.setData(list);
        result.setDraw(draw);
        Integer totalEntries = service.getTotalEntries();
        result.setRecordsFiltered(totalEntries);
        result.setRecordsTotal(totalEntries);
        return result;
    }

    public OrderData update(Integer id, OrderForm f) throws ApiException{
        OrderPojo p =service.update(id, null);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
        String strDate = dateFormat.format(p.getUpdated());  
        OrderData data = ConvertUtil.objectMapper(p, OrderData.class);
        data.setUpdated(strDate);

        return data;
    }

    public void finaliseOrder(Integer id) throws ApiException{
        service.finaliseOrder(id);
    }

}
