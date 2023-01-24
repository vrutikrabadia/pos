package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestUtil;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemsData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.OrderItemsForm;
import com.increff.pos.service.ApiException;

public class OrderDtoTest extends AbstractUnitTest{
    @Autowired
    private OrderDto dto;

    private Optional<String> empty = Optional.empty();

    @Test
    public void testAddAndGet() throws ApiException{
        
        TestUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
        TestUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n1", 10.00, 10);

        List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();
        
        itemsList.add(TestUtil.getItemsForm("abcdefgh", 5, 15.00));
        itemsList.add(TestUtil.getItemsForm("abcdefg1", 5, 15.00));
       
        dto.add(itemsList);

        SelectData<OrderData> orderList = dto.getAll(0, 5, 1, empty);

        assertEquals(1, orderList.getData().size());

        List<OrderItemsData> itemsList1 = dto.getByOrderId(orderList.getData().get(0).getId());
    
        assertEquals(2, itemsList1.size());
    }

    @Test
    public void testDuplicateProductAddition(){
        
        TestUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
        TestUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n1", 10.00, 10);

        
        List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();
        
        itemsList.add(TestUtil.getItemsForm("abcdefgh", 5, 15.00));
        itemsList.add(TestUtil.getItemsForm("abcdefgh", 5, 15.00));

        try{
            dto.add(itemsList);
        }catch(ApiException e){
            return;
        }

        fail();

    }

    @Test 
    public void testBarCodeNotExists(){
        
        TestUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);

        List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();
        
        itemsList.add(TestUtil.getItemsForm("abcdefgh", 5, 15.00));
        itemsList.add(TestUtil.getItemsForm("abcdefg1", 5, 15.00));

        try{
            dto.add(itemsList);
        }catch(ApiException e){
            return;
        }

        fail();
    }


    @Test
    public void testQuantityNotAvailable(){
        
        TestUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
        TestUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n1", 10.00, 10);

        List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();
        
        itemsList.add(TestUtil.getItemsForm("abcdefgh", 15, 15.00));
        itemsList.add(TestUtil.getItemsForm("abcdefg1", 5, 15.00));

        try{
            dto.add(itemsList);
        }catch(ApiException e){
            return;
        }

        fail();
    }

    @Test
    public void testGenerateInvoice() throws ApiException{
            
            TestUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
            TestUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n1", 10.00, 10);
    
            List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();
            
            itemsList.add(TestUtil.getItemsForm("abcdefgh", 5, 15.00));
            itemsList.add(TestUtil.getItemsForm("abcdefg1", 5, 15.00));
    
            dto.add(itemsList);
            
            String base64 = null;

            try{
                base64 = dto.generateInvoice(1);
            }catch(Exception e){
                fail();
            }

            assertEquals(true, Objects.nonNull(base64));

    }

    @Test
    public void testGetById() throws ApiException{
            
            TestUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
            TestUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n1", 10.00, 10);
    
            List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();
            
            itemsList.add(TestUtil.getItemsForm("abcdefgh", 5, 15.00));
            itemsList.add(TestUtil.getItemsForm("abcdefg1", 5, 15.00));
        
            try{
                dto.add(itemsList);
            }catch(ApiException e){
                fail();
            }
            
            SelectData<OrderData> oData= dto.getAll(0, 5, 1, empty);

            Integer orderId = oData.getData().get(0).getId();

            OrderData orderData = dto.get(orderId);
    
            assertEquals(orderId, orderData.getId());
    }

    @Test
    public void testSearchByValue(){
            
            TestUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
            TestUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n1", 10.00, 10);
    
            List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();
            
            itemsList.add(TestUtil.getItemsForm("abcdefgh", 5, 15.00));
            itemsList.add(TestUtil.getItemsForm("abcdefg1", 5, 15.00));
        
            try{
                dto.add(itemsList);
            }catch(ApiException e){
                fail();
            }
    
            SelectData<OrderData> oData= dto.getAll(0, 5, 1, empty);
    
            Integer orderId = oData.getData().get(0).getId();
    
            SelectData<OrderData> orderData = dto.getAll(0, 5, 1, Optional.of(orderId.toString()));
    
            assertEquals(1, orderData.getData().size());
    }

   

}
