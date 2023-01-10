package com.increff.pos.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;

@Service
public class OrderService {
    
    @Autowired
    private OrderDao dao;

    @Transactional
    public OrderPojo add(OrderPojo p){
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
        String strDate = dateFormat.format(date);  
        p.setDateTime(strDate);
        p.setId(dao.insert(p));
        p.setEditable(true);
        return p;
    }

    @Transactional 
    public OrderPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    @Transactional
    public OrderPojo getCheck(int id) throws ApiException{
        OrderPojo p =dao.selectById(id);
        if(p == null){
            throw new ApiException("Order does not exists");
        }

        return p;
    }

    @Transactional
    public List<OrderPojo> getAll(){
        return dao.selectAll();
    }

    @Transactional
    public OrderPojo update(int id,OrderPojo p) throws ApiException{
        OrderPojo p1 = getCheck(id);
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
        String strDate = dateFormat.format(date);
        p1.setDateTime(strDate);

        dao.update(p1);

        return p1;
    }

    @Transactional
    public void finaliseOrder(int id) throws ApiException{
        OrderPojo p1 = getCheck(id);
        
        p1.setEditable(false);

        dao.update(p1);

    }

    
}
