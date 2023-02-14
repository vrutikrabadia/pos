package com.increff.pos.dto;

import java.io.File;
import java.util.*;

import com.increff.pos.model.form.OrderForm;
import com.increff.pos.util.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pdf.generator.InvoiceGenerator;
import com.increff.pdf.model.data.InvoiceData;
import com.increff.pdf.model.data.InvoiceItemsData;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemsData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.OrderItemsForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemsService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.spring.Properties;
import com.increff.pos.util.Base64Util;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidateUtil;

import javax.transaction.Transactional;

@Component
public class OrderDto {

    @Autowired
    private OrderService service;

    @Autowired
    private OrderItemsService iService;

    @Autowired
    private ProductService pService;

    @Autowired
    private InventoryService invService;

    @Autowired
    private Properties properties;

    @Transactional(rollbackOn = ApiException.class)
    public OrderData add(OrderForm orderForm) throws ApiException {
        List<OrderItemsForm> orderItemsForms = orderForm.getOrderItems();
        StringUtil.normaliseList(orderItemsForms, OrderItemsForm.class);
        ValidateUtil.validateForms(orderItemsForms);

        List<OrderItemPojo> orderItemPojos =  checkAndConvertToPojo(orderItemsForms);
        OrderPojo order = service.add(orderItemPojos);

        for(OrderItemPojo orderItemPojo : orderItemPojos) {
            invService.reduceQuantity(orderItemPojo.getProductId(), orderItemPojo.getQuantity());
        }

        return ConvertUtil.objectMapper(order, OrderData.class);
    }

    public OrderData get(Integer orderId) throws ApiException {
        OrderPojo orderPojo = service.getCheck(orderId);
        return ConvertUtil.objectMapper(orderPojo, OrderData.class);
    }

    public SelectData<OrderData> getAll(Integer start, Integer length, Integer draw, String searchValue) {
        List<OrderData> orderDataList = new ArrayList<OrderData>();
        List<OrderPojo> orderPojoList = new ArrayList<OrderPojo>();
        if (Objects.nonNull(searchValue)&&!searchValue.isEmpty()) {
            try {
                orderPojoList.add(service.getCheck(Integer.valueOf(searchValue)));
            } catch (ApiException e) {
                orderPojoList = service.getAllPaginated(start, length);
            }
        } else {
            orderPojoList = service.getAllPaginated(start, length);
        }

        for (OrderPojo orderPojo : orderPojoList) {
            OrderData orderData = ConvertUtil.objectMapper(orderPojo, OrderData.class);

            orderDataList.add(orderData);
        }

        Integer totalEntries = service.getTotalEntries();

        return new SelectData<OrderData>(orderDataList, draw, totalEntries, totalEntries);
    }

    public List<OrderItemsData> getByOrderId(Integer orderId) throws ApiException {
        List<OrderItemsData> itemsDataList = new ArrayList<OrderItemsData>();
        List<OrderItemPojo> itemsPojoList = iService.getByOrderId(orderId);

        for (OrderItemPojo orderItemPojo : itemsPojoList) {

            ProductPojo product = pService.getCheckById(orderItemPojo.getProductId());
            OrderItemsData data = ConvertUtil.objectMapper(orderItemPojo, OrderItemsData.class);
            data.setBarcode(product.getBarcode());

            itemsDataList.add(data);
        }

        return itemsDataList;

    }

    public String getInvoice(Integer orderId) throws ApiException, com.increff.pdf.service.ApiException {

        String filePath = new File(properties.getCacheLocation()+"/invoice"+orderId+".pdf").getAbsolutePath();
        File file = new File(filePath);

        if (file.exists()){
            return Base64Util.encodeFileToBase64Binary(filePath);
        }
        return generateInvoice(orderId);

    }

    private String generateInvoice(Integer orderId) throws  ApiException, com.increff.pdf.service.ApiException{
        String filePath = new File(properties.getCacheLocation()+"/invoice"+orderId+".pdf").getAbsolutePath();
        OrderPojo order = service.getCheck(orderId);

        InvoiceData invoiceData = ConvertUtil.objectMapper(order, InvoiceData.class);
        List<OrderItemPojo> itemsList = iService.getByOrderId(orderId);

        List<InvoiceItemsData> invoiceItemsData = new ArrayList<InvoiceItemsData>();

        for (OrderItemPojo item : itemsList) {
            InvoiceItemsData itemData = ConvertUtil.objectMapper(item, InvoiceItemsData.class);
            ProductPojo product = pService.getCheckById(item.getProductId());
            itemData.setBarcode(product.getBarcode());
            itemData.setName(product.getName());
            invoiceItemsData.add(itemData);
        }

        invoiceData.setItemsList(invoiceItemsData);

        String base64 = InvoiceGenerator.generateInvoice(invoiceData);

        Base64Util.decodeBase64ToFile(base64, filePath);

        return base64;

    }

    private List<OrderItemPojo> checkAndConvertToPojo(List<OrderItemsForm> orderItemsForms) throws  ApiException {
        List<OrderItemPojo> orderItemPojos = new ArrayList<OrderItemPojo>();
        JSONArray errorList = new JSONArray();
        Set<String> barcodeSet = new HashSet<String>();

        for (OrderItemsForm orderItemsForm : orderItemsForms) {
            if (barcodeSet.contains(orderItemsForm.getBarcode())) {
                errorList.put(ExceptionUtil.generateJSONErrorObject("Duplicate products in the order.", orderItemsForm));
            }
            barcodeSet.add(orderItemsForm.getBarcode());
            OrderItemPojo orderItemPojo = ConvertUtil.objectMapper(orderItemsForm, OrderItemPojo.class);
            ProductPojo product = new ProductPojo();
            try {
                product = pService.getCheckByBarcode(orderItemsForm.getBarcode());
            } catch (ApiException e) {
                errorList.put(ExceptionUtil.generateJSONErrorObject(e.getMessage(), orderItemsForm));
            }
            orderItemPojo.setProductId(product.getId());
            try {
                invService.checkInventory(product.getId(), orderItemsForm.getQuantity());
            } catch (ApiException apiException) {
                errorList.put(ExceptionUtil.generateJSONErrorObject(apiException.getMessage()+" for "+orderItemsForm.getBarcode(), orderItemsForm));
            }
            orderItemPojos.add(orderItemPojo);
        }

        if (errorList.length() > 0) {
            throw new ApiException(errorList.toString());
        }
        return orderItemPojos;
    }

}
