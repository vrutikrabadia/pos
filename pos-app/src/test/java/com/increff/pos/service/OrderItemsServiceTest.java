package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.config.TestUtil;
import com.increff.pos.pojo.OrderItemPojo;

public class OrderItemsServiceTest extends AbstractUnitTest{
    
    @Autowired
    private OrderItemsService service;
    
    @Autowired
    private TestUtil testUtil;

    @Test
    public void testAddAndGet() throws ApiException {
        Integer orderId = testUtil.addOrder();

        List<OrderItemPojo> itemsList = new ArrayList<OrderItemPojo>();

        for(int i=1; i<=5; i++){
            Integer prodId = testUtil.addBrandAndProduct("b"+i, "c"+i, "abcdefg"+i, "p"+i, 10.00);
            testUtil.addInventory(prodId,10);
            OrderItemPojo item = testUtil.getItemsPojo(orderId, prodId, 5, 15.00);
            itemsList.add(item);
        }

        service.add(testUtil.getItemsPojo(orderId, 1, 5, 15.00));

        service.add(orderId,itemsList);

        List<OrderItemPojo> itemsList1 = service.getByOrderId(orderId);

        assertEquals(6, itemsList1.size());

    }

    @Test
    public void testSelectById() throws  ApiException{
        Integer orderId = testUtil.addOrder();

        List<OrderItemPojo> itemsList = new ArrayList<OrderItemPojo>();

        for(int i=1; i<=5; i++){
            Integer prodId = testUtil.addBrandAndProduct("b"+i, "c"+i, "abcdefg"+i, "p"+i, 10.00);
            testUtil.addInventory(prodId,10);
            OrderItemPojo item = testUtil.getItemsPojo(orderId, prodId, 5, 15.00);
            itemsList.add(item);
        }


        service.add(orderId,itemsList);

        List<OrderItemPojo> itemsList1 = service.getByOrderId(orderId);

        List<Integer> itemsId = itemsList1.stream().map(OrderItemPojo::getId).collect(Collectors.toList());

        OrderItemPojo item = service.getById(itemsId.get(1));

        assertEquals(itemsId.get(1), item.getId());
    }

    @Test
    public void testGetInColumn() throws ApiException{
        Integer orderId = testUtil.addOrder();

        List<OrderItemPojo> itemsList = new ArrayList<OrderItemPojo>();

        for(int i=1; i<=5; i++){
            Integer prodId = testUtil.addBrandAndProduct("b"+i, "c"+i, "abcdefg"+i, "p"+i, 10.00);
            testUtil.addInventory(prodId,10);
            OrderItemPojo item = testUtil.getItemsPojo(orderId, prodId, 5, 15.00);
            itemsList.add(item);
        }

        service.add(orderId,itemsList);

        List<OrderItemPojo> resList = service.getInColumn(Arrays.asList("orderId"), Arrays.asList(Arrays.asList(orderId)));

        assertEquals(itemsList, resList);
    }


}
