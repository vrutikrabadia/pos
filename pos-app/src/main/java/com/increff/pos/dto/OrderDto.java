package com.increff.pos.dto;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.increff.pdf.generator.InvoiceGenerator;
import com.increff.pdf.model.data.InvoiceData;
import com.increff.pdf.model.data.InvoiceItemsData;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemsData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.OrderItemsForm;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemsService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.Base64Util;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidateUtil;

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

    // @Autowired
    // private RestTemplate restTemplate;

    @Value("${cache.location}")
    private String cacheLocation;

    @Value("${pdfapp.url}")
    private String pdfAppUrl;

    public OrderData add(List<OrderItemsForm> list) throws ApiException {
        List<OrderItemsPojo> list1 = new ArrayList<OrderItemsPojo>();
        JSONArray errorList = new JSONArray();
        Set<String> barcodeSet = new HashSet<String>();
        StringUtil.normaliseList(list, OrderItemsForm.class);
        ValidateUtil.validateForms(list);

        for (OrderItemsForm f : list) {

            if (barcodeSet.contains(f.getBarcode())) {
                JSONObject error = new JSONObject(new Gson().toJson(f));

                error.put("error", "Duplicate products in the order.");
                errorList.put(error);
            }

            barcodeSet.add(f.getBarcode());

            OrderItemsPojo p = ConvertUtil.objectMapper(f, OrderItemsPojo.class);

            ProductPojo product = new ProductPojo();
            try {
                product = pService.get(f.getBarcode());
            } catch (ApiException e) {
                JSONObject error = new JSONObject(new Gson().toJson(f));

                error.put("error", e.getMessage());
                errorList.put(error);
            }
            p.setProductId(product.getId());

            try {
                invService.checkInventory(product.getId(), f.getQuantity());
            } catch (ApiException e) {
                JSONObject error = new JSONObject(new Gson().toJson(f));

                error.put("error", e.getMessage()+" for "+f.getBarcode());
                errorList.put(error);
            }

            list1.add(p);
        }

        if (errorList.length() > 0) {
            throw new ApiException(errorList.toString());
        }
        OrderPojo order = service.add(list1);

        OrderData data = ConvertUtil.objectMapper(order, OrderData.class);

        return data;

    }

    public OrderData get(Integer id) throws ApiException {
        OrderPojo p = service.get(id);
        OrderData data = ConvertUtil.objectMapper(p, OrderData.class);

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

        for (OrderPojo p : list1) {
            OrderData data = ConvertUtil.objectMapper(p, OrderData.class);

            list.add(data);
        }

        Integer totalEntries = service.getTotalEntries();
        return new SelectData<OrderData>(list, draw, totalEntries, totalEntries);
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

    public String generateInvoice(Integer orderId) throws ApiException, Exception {

        String filePath = new File(cacheLocation+"/invoice"+orderId+".pdf").getAbsolutePath();
        File file = new File(filePath);

        if (file.exists()){
            return Base64Util.encodeFileToBase64Binary(filePath);
        }

        OrderPojo order = service.get(orderId);

        InvoiceData invoiceData = ConvertUtil.objectMapper(order, InvoiceData.class);
        List<OrderItemsPojo> itemsList = iService.selectByOrderId(orderId);

        List<InvoiceItemsData> invItems = new ArrayList<InvoiceItemsData>();

        for (OrderItemsPojo item : itemsList) {
            InvoiceItemsData itemData = ConvertUtil.objectMapper(item, InvoiceItemsData.class);
            ProductPojo prod = pService.get(item.getProductId());
            itemData.setBarcode(prod.getBarcode());
            itemData.setName(prod.getName());
            invItems.add(itemData);
        }

        invoiceData.setItemsList(invItems);

        // String base64 = "";
        String base64 = new InvoiceGenerator(cacheLocation).generateInvoice(invoiceData);

        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);

        // String apiUrl = pdfAppUrl + "/api/invoices";
        // ResponseEntity<String> apiResponse = restTemplate.postForEntity(apiUrl, invoiceData, String.class);
        // String responseBody = apiResponse.getBody();

        Base64Util.decodeBase64ToFile(base64, filePath);

        return base64;

    }

}
