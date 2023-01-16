package com.increff.pos.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemsData;
import com.increff.pos.model.form.OrderItemsForm;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemsService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ExceptionUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class OrderItemsDto {

    @Autowired
    private OrderItemsService service;

    @Autowired
    private ProductService pService;

    @Autowired
    private OrderService oService;

    public OrderData add(List<OrderItemsForm> list) throws ApiException {
        List<OrderItemsPojo> list1 = new ArrayList<OrderItemsPojo>();
        JSONArray errorList = new JSONArray();
        Set<String> barcodeSet = new HashSet<String> (); 

        for (OrderItemsForm f : list) {
            try{
                ValidateUtil.validateForms(f);
            }
            catch(ConstraintViolationException e){
                JSONObject error = new JSONObject();
                error.put("barcode", f.getBarcode());
                error.put("quantity", f.getQuantity());
                error.put("quantity", f.getSellingPrice());
                error.put("error", ExceptionUtil.getValidationMessage(e));
                errorList.put(error);
                
            }

            if(barcodeSet.contains(f.getBarcode())){
                JSONObject error = new JSONObject();
                error.put("barcode", f.getBarcode());
                error.put("quantity", f.getQuantity());
                error.put("quantity", f.getSellingPrice());
                error.put("error", "Duplicate products in the order");
                errorList.put(error);
            }

            barcodeSet.add(f.getBarcode());


            OrderItemsPojo p = ConvertUtil.objectMapper(f, OrderItemsPojo.class);

            
            ProductPojo product = new ProductPojo();
            try{
                product = pService.get(f.getBarcode());
            }
            catch(ApiException e){
                JSONObject error = new JSONObject();
                error.put("barcode", f.getBarcode());
                error.put("quantity", f.getQuantity());
                error.put("quantity", f.getSellingPrice());
                error.put("error", e.getMessage());
                errorList.put(error);
            }
            p.setProductId(product.getId());

            list1.add(p);
        }

        if(errorList.length() > 0){
            throw new ApiException(errorList.toString());
        }
        OrderPojo order = service.add(list1);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(order.getUpdated());
        OrderData data = ConvertUtil.objectMapper(order, OrderData.class);
        data.setUpdated(strDate);

        return data;

    }

    public void addToExisitingOrder(Integer orderId, OrderItemsForm form) throws ApiException {
        OrderItemsPojo item = ConvertUtil.objectMapper(form, OrderItemsPojo.class);

        oService.getCheck(orderId);
        
        ProductPojo product = pService.get(form.getBarcode());
        item.setProductId(product.getId());

        item.setOrderId(orderId);

        service.add(item);

    }

    public OrderItemsData getById(Integer id) throws ApiException {
        OrderItemsPojo p = service.selectById(id);
        ProductPojo product = pService.get(p.getId());
        OrderItemsData data = ConvertUtil.objectMapper(p, OrderItemsData.class);
        data.setBarcode(product.getBarcode());
        return data;
    }

    public List<OrderItemsData> getByOrderId(Integer orderId) throws ApiException {
        List<OrderItemsData> list = new ArrayList<OrderItemsData>();
        List<OrderItemsPojo> list1 = service.selectByOrderId(orderId);

        for (OrderItemsPojo p : list1) {

            ProductPojo product = pService.get(p.getProductId());
            OrderItemsData data = ConvertUtil.objectMapper(p, OrderItemsData.class);
            data.setBarcode(product.getBarcode());

            list.add(data);
        }

        return list;

    }

    public void update(Integer id, OrderItemsForm f) throws ApiException {
        ValidateUtil.validateForms(f);

        OrderItemsPojo p = ConvertUtil.objectMapper(f, OrderItemsPojo.class);


        ProductPojo product = pService.get(f.getBarcode());
        p.setProductId(product.getId());

        service.update(id, p);
    }
}
