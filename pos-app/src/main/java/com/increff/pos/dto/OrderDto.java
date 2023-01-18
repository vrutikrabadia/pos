package com.increff.pos.dto;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.google.gson.Gson;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemsData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.OrderItemsForm;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemsService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.PdfUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidateUtil;
import com.increff.pos.util.XmlUtils;

@Component
public class OrderDto {

    @Autowired
    private OrderService service;

    @Autowired
    private OrderItemsService iService;

    @Autowired
    private ProductService pService;


    public OrderData add(List<OrderItemsForm> list) throws ApiException {
        List<OrderItemsPojo> list1 = new ArrayList<OrderItemsPojo>();
        JSONArray errorList = new JSONArray();
        Set<String> barcodeSet = new HashSet<String> (); 
        StringUtil.normaliseList(list, OrderItemsForm.class);
        ValidateUtil.validateForms(list);

        for (OrderItemsForm f : list) {
            

            if(barcodeSet.contains(f.getBarcode())){
                JSONObject error = new JSONObject(new Gson().toJson(f));

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
                JSONObject error = new JSONObject(new Gson().toJson(f));
                
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z");
        String strDate = order.getUpdated().format(formatter);
        OrderData data = ConvertUtil.objectMapper(order, OrderData.class);
        data.setUpdated(strDate);

        return data;

    }

    public OrderData get(Integer id) throws ApiException {
        OrderPojo p = service.get(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z");
        String strDate = p.getUpdated().format(formatter);
        OrderData data = ConvertUtil.objectMapper(p, OrderData.class);
        data.setUpdated(strDate);

        return data;
    }

    public SelectData<OrderData> getAll(Integer start, Integer length, Integer draw, Optional<String> searchValue) {
        List<OrderData> list = new ArrayList<OrderData>();
        List<OrderPojo> list1 = new ArrayList<OrderPojo>();
        if (searchValue.isPresent() && !searchValue.get().isBlank()) {
            try {
                list1.add(service.get(Integer.valueOf(searchValue.get())));
            } catch (ApiException e) {
                list1 = service.getAllPaginated(start, length);
            }
        } else {
            list1 = service.getAllPaginated(start, length);
        }
        
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z");

        for (OrderPojo p : list1) {
            String strDate =  p.getUpdated().format(formatter);
            OrderData data = ConvertUtil.objectMapper(p, OrderData.class);
            data.setUpdated(strDate);

            list.add(data);
        }

        SelectData<OrderData> result = new SelectData<OrderData>();
        result.setData(list);
        result.setDraw(draw);
        Integer totalEntries = service.getTotalEntries();
        System.out.println(totalEntries);
        result.setRecordsFiltered(totalEntries);
        result.setRecordsTotal(totalEntries);
        return result;
    }

    public List<OrderItemsData> getByOrderId(Integer orderId) throws ApiException {
        List<OrderItemsData> list = new ArrayList<OrderItemsData>();
        List<OrderItemsPojo> list1 = iService.selectByOrderId(orderId);

        for (OrderItemsPojo p : list1) {

            ProductPojo product = pService.get(p.getProductId());
            OrderItemsData data = ConvertUtil.objectMapper(p, OrderItemsData.class);
            data.setBarcode(product.getBarcode());

            list.add(data);
        }

        return list;

    }

    public OrderData update(Integer id, OrderForm f) throws ApiException {
        OrderPojo p = service.update(id, null);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z");
        String strDate = p.getUpdated().format(formatter);
        OrderData data = ConvertUtil.objectMapper(p, OrderData.class);
        data.setUpdated(strDate);

        return data;
    }

    public void generateInvoice(Integer orderId, HttpServletResponse response) throws ApiException, Exception {

        OrderPojo order = service.get(orderId);

        JSONObject invoiceData = new JSONObject(new Gson().toJson(order));

        JSONArray itemArray = new JSONArray();

        List<OrderItemsPojo> itemsList = iService.selectByOrderId(orderId);

        for (OrderItemsPojo item : itemsList) {
            JSONObject itemData = new JSONObject(new Gson().toJson(item));
            ProductPojo prod = pService.get(item.getProductId());
            itemData.put("productName", prod.getName());
            itemData.put("barcode", prod.getBarcode());
            itemArray.put(itemData);
        }

        invoiceData.put("items", itemArray);

        try {
            XmlUtils.generateInvoiceXml(invoiceData);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        try {
            PdfUtil.generateInvoicePdf(order.getId());
        } catch (Exception e) {
            throw new ApiException("Error generating PDF");
        }

        try {

            File file = new File(new File("src/main/resources/com/increff/pos/invoice"+orderId.toString()+".pdf").getAbsolutePath());

            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            String mimeType = URLConnection.guessContentTypeFromStream(inputStream);

            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));

            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            throw new ApiException("Unable to download the file");
        }
    }

    public void finaliseOrder(Integer id) throws ApiException {
        service.finaliseOrder(id);
    }

}
