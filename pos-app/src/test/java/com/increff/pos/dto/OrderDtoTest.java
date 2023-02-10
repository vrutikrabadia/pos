package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.increff.pos.model.form.OrderForm;
import org.hibernate.criterion.Order;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.config.TestUtil;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemsData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.OrderItemsForm;
import com.increff.pos.util.ApiException;

public class OrderDtoTest extends AbstractUnitTest {
    @Autowired
    private OrderDto dto;

    @Autowired
    private TestUtil testUtil;

    @Autowired
    private InventoryDto inventoryDto;


    @Test
    public void testAddAndGet() throws ApiException {

        testUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
        testUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n1", 10.00, 10);

        List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();

        itemsList.add(testUtil.getItemsForm("abcdefgh", 5, 15.00));
        itemsList.add(testUtil.getItemsForm("abcdefg1", 5, 15.00));

        OrderForm orderForm = new OrderForm();
        orderForm.setOrderItems(itemsList);

        dto.add(orderForm);

        SelectData<OrderData> orderList = dto.getAll(0, 5, 1, null);

        assertEquals(1, orderList.getData().size());

        List<OrderItemsData> itemsList1 = dto.getByOrderId(orderList.getData().get(0).getId());

        assertEquals(2, itemsList1.size());

        Integer expectedQuantity = 5;

        Integer inventory1 = inventoryDto.get("abcdefgh").getQuantity();
        Integer inventory2 = inventoryDto.get("abcdefg1").getQuantity();

        assertEquals(expectedQuantity, inventory1);
        assertEquals(expectedQuantity, inventory2);
    }

    @Test
    public void testDuplicateProductAddition() {

        testUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
        testUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n1", 10.00, 10);


        List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();

        itemsList.add(testUtil.getItemsForm("abcdefgh", 5, 15.00));
        itemsList.add(testUtil.getItemsForm("abcdefgh", 5, 15.00));

        try {
            OrderForm orderForm = new OrderForm();
            orderForm.setOrderItems(itemsList);

            dto.add(orderForm);
        } catch (ApiException e) {
            return;
        }

        fail();

    }

    @Test
    public void testBarCodeNotExists() {

        testUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);

        List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();

        itemsList.add(testUtil.getItemsForm("abcdefgh", 5, 15.00));
        itemsList.add(testUtil.getItemsForm("abcdefg1", 5, 15.00));

        try {
            OrderForm orderForm = new OrderForm();
            orderForm.setOrderItems(itemsList);

            dto.add(orderForm);
        } catch (ApiException e) {
            return;
        }

        fail();
    }


    @Test
    public void testQuantityNotAvailable() {

        testUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
        testUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n1", 10.00, 10);

        List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();

        itemsList.add(testUtil.getItemsForm("abcdefgh", 15, 15.00));
        itemsList.add(testUtil.getItemsForm("abcdefg1", 5, 15.00));

        try {
            OrderForm orderForm = new OrderForm();
            orderForm.setOrderItems(itemsList);

            dto.add(orderForm);
        } catch (ApiException e) {
            return;
        }

        fail();
    }

    @Test
    public void testGenerateInvoice() throws ApiException {

        testUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
        testUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n1", 10.00, 10);

        List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();

        itemsList.add(testUtil.getItemsForm("abcdefgh", 5, 15.00));
        itemsList.add(testUtil.getItemsForm("abcdefg1", 5, 15.00));

        OrderForm orderForm = new OrderForm();
        orderForm.setOrderItems(itemsList);

        dto.add(orderForm);

        String base64 = null;

        SelectData<OrderData> orderList = dto.getAll(0, 5, 1, null);

        try {
            base64 = dto.generateInvoice(orderList.getData().get(0).getId());
        } catch (Exception e) {

        }

        assertEquals(true, Objects.nonNull(base64));

    }

    @Test
    public void testGetById() throws ApiException {

        testUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
        testUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n1", 10.00, 10);

        List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();

        itemsList.add(testUtil.getItemsForm("abcdefgh", 5, 15.00));
        itemsList.add(testUtil.getItemsForm("abcdefg1", 5, 15.00));

        try {
            OrderForm orderForm = new OrderForm();
            orderForm.setOrderItems(itemsList);

            dto.add(orderForm);
        } catch (ApiException e) {
            fail();
        }

        SelectData<OrderData> oData = dto.getAll(0, 5, 1, null);

        Integer orderId = oData.getData().get(0).getId();

        OrderData orderData = dto.get(orderId);

        assertEquals(orderId, orderData.getId());
    }

    @Test
    public void testSearchByValue() {

        testUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
        testUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n1", 10.00, 10);

        List<OrderItemsForm> itemsList = new ArrayList<OrderItemsForm>();

        itemsList.add(testUtil.getItemsForm("abcdefgh", 5, 15.00));
        itemsList.add(testUtil.getItemsForm("abcdefg1", 5, 15.00));

        try {
            OrderForm orderForm = new OrderForm();
            orderForm.setOrderItems(itemsList);

            dto.add(orderForm);
        } catch (ApiException e) {
            fail();
        }

        SelectData<OrderData> oData = dto.getAll(0, 5, 1, null);

        Integer orderId = oData.getData().get(0).getId();

        SelectData<OrderData> orderData = dto.getAll(0, 5, 1, orderId.toString());

        assertEquals(1, orderData.getData().size());
    }


}
