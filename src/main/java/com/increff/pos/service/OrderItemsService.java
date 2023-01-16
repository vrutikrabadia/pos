package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderItemsDao;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.SchedulerPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemsService {
    
    @Autowired
    private OrderItemsDao dao;

    @Autowired 
    private InventoryService iService;

    @Autowired
    private OrderService oService;

    @Autowired 
    SchedulerService sService;

     
    public OrderPojo add(List<OrderItemsPojo> list) throws ApiException{
        OrderPojo order = oService.add(new OrderPojo());
        Double revenue = 0.0;
        Integer totalQuantity = 0;
        for(OrderItemsPojo p: list){
            revenue = revenue + (p.getQuantity()*p.getSellingPrice());
            totalQuantity += p.getQuantity();
            iService.reduceQuantity(p.getProductId(), p.getQuantity());
            p.setOrderId(order.getId());
            dao.insert(p);
        }

        SchedulerPojo sPojo = new SchedulerPojo();
        sPojo.setItemsCount(totalQuantity);
        sPojo.setTotalRevenue(revenue);
        sService.add(sPojo);

        return order;
    }

    public void add(OrderItemsPojo item){
        dao.insert(item);
    }

     
    public OrderItemsPojo selectById(Integer id){
        return dao.selectById(id);
    }

     
    public List<OrderItemsPojo> selectByOrderId(Integer orderId){
        return dao.selectByOrderId(orderId);
    }

     
    public void update(Integer id, OrderItemsPojo p) throws ApiException{
        
        
        OrderItemsPojo p1 = selectById(id);

        if(p1.getProductId() != p.getProductId()){
            throw new ApiException("Mismatch in product barcode provided");
        }

        OrderPojo op = oService.update(p1.getOrderId(), null);

        if(!op.isEditable()){
            throw new ApiException("Order is no longer editable");
        }

        if(p1.getQuantity() > p.getQuantity()){
            iService.increaseQuantity(p1.getProductId(), p1.getQuantity()-p.getQuantity());
        }
        else if(p1.getQuantity() < p.getQuantity()){
            iService.reduceQuantity(p1.getProductId(), p.getQuantity()-p1.getQuantity());
        }

        p1.setQuantity(p.getQuantity());
        p1.setSellingPrice(p.getSellingPrice());
    }
    
}
