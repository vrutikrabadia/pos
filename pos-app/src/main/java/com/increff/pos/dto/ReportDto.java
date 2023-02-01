package com.increff.pos.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemsService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class ReportDto {

    @Autowired
    private InventoryService iService;

    @Autowired
    private ProductService pService;

    @Autowired
    private BrandService bService;

    @Autowired
    private OrderService oService;

    @Autowired
    private OrderItemsService oItemsService;

    @Autowired
    private BrandDto bDto;

    @Autowired
    private RestTemplate restTemplate;


    //functions for inventory report

    public String getInventoryReport() throws ApiException {

        List<InventoryPojo> iList = iService.getAll();
        HashMap<Integer, Integer> prodIdtoBrandCat = getProductIdToBrandCatMap(iList);
        List<Integer> brandCatIds = prodIdtoBrandCat.values().stream().distinct().collect(Collectors.toList());
        HashMap<Integer, Integer> resultMap = getbrandCatIdToQuantityMap(prodIdtoBrandCat, iList, brandCatIds);

        return invReportFromMap(resultMap, brandCatIds);
    }

    private String invReportFromMap(HashMap<Integer, Integer> resultMap, List<Integer> brandCatIds) {
        List<InventoryReportData> result = new ArrayList<InventoryReportData>();

        List<BrandPojo> brandList = bService.getInColumn(Arrays.asList("id"), Arrays.asList(brandCatIds));

        HashMap<Integer, BrandPojo> idtoBrandCat = (HashMap<Integer, BrandPojo>) brandList.stream()
                .collect(Collectors.toMap(BrandPojo::getId, e -> e));

        for (Integer key : resultMap.keySet()) {
            InventoryReportData res = new InventoryReportData();
            res.setQuantity(resultMap.get(key));

            res.setBrand(idtoBrandCat.get(key).getBrand());
            res.setCategory(idtoBrandCat.get(key).getCategory());

            result.add(res);
        }
        
        return getReportFile(result, "/inventory");
    }

    protected HashMap<Integer, Integer> getProductIdToBrandCatMap(List<InventoryPojo> iList) {
        List<Integer> idList = iList.stream().map(InventoryPojo::getProductId).collect(Collectors.toList());
        List<ProductPojo> productList = pService.getInColumn(Arrays.asList("id"), Arrays.asList(idList));

        HashMap<Integer, Integer> prodIdtoBrandCat = (HashMap<Integer, Integer>) productList.stream()
                .collect(Collectors.toMap(ProductPojo::getId, ProductPojo::getBrandCat));
        return prodIdtoBrandCat;
    }

    protected HashMap<Integer, Integer> getbrandCatIdToQuantityMap(HashMap<Integer, Integer> prodIdtoBrandCat,
            List<InventoryPojo> iList, List<Integer> brandCatIds) {

        HashMap<Integer, Integer> resultMap = (HashMap<Integer, Integer>) brandCatIds.stream()
                .collect(Collectors.toMap(Function.identity(), i -> 0));

        for (InventoryPojo inv : iList) {

            Integer newQuantity = resultMap.get(prodIdtoBrandCat.get(inv.getProductId())) + inv.getQuantity();
            resultMap.put(prodIdtoBrandCat.get(inv.getProductId()), newQuantity);

        }

        return resultMap;
    }

    //functions for sales report

    public String getSalesReport(SalesReportForm form) throws ApiException {

        StringUtil.normalise(form, SalesReportForm.class);
        ValidateUtil.validateForms(form);
        
        ZonedDateTime sDate = form.getStartDate();
        ZonedDateTime eDate = form.getEndDate();
        

        if (sDate.compareTo(eDate) > 0) {
            throw new ApiException("start date should be less than end date");
        }

        if ((Objects.nonNull(form.getBrand()) && !form.getBrand().isEmpty())
                || (Objects.nonNull(form.getCategory()) && !form.getCategory().isEmpty())) {

            return generateSalesReportWithBrandandCategory(form.getBrand(), form.getCategory(), sDate, eDate);
        } else {
            return generateSalesReportOnlyDates(sDate, eDate);
        }

    }

    private String generateSalesReportWithBrandandCategory(String brand, String category,
            ZonedDateTime sDate, ZonedDateTime eDate) throws ApiException {

        List<BrandPojo> brandList = new ArrayList<BrandPojo>();

        if ((Objects.nonNull(brand) && !brand.isEmpty())
                && (Objects.nonNull(category) && !category.isEmpty())) {
            BrandPojo bPojo = bService.get(brand, category);
            brandList.add(bPojo);
        } else if ((Objects.nonNull(brand) && !brand.isEmpty())
                || (Objects.nonNull(category) && !category.isEmpty())) {

            if ((Objects.nonNull(brand) && !brand.isEmpty())) {
                brandList = bService.getInColumn(Arrays.asList("brand"), Arrays.asList(Arrays.asList(brand)));
            } else {
                brandList = bService.getInColumn(Arrays.asList("category"), Arrays.asList(Arrays.asList(category)));
            }
        }

        List<Integer> brandCatIds = brandList.stream().map(BrandPojo::getId).collect(Collectors.toList());

        List<ProductPojo> prodList = pService.getInColumn(Arrays.asList("brandCat"), Arrays.asList(brandCatIds));

        List<Integer> prodIds = prodList.stream().map(ProductPojo::getId).collect(Collectors.toList());

        List<OrderItemsPojo> itemsList = getOrderItems(sDate, eDate, prodIds);

        return generateSalesReport(itemsList, brandList, prodList);

    }

    private String generateSalesReportOnlyDates(ZonedDateTime sDate, ZonedDateTime eDate) {

        List<OrderItemsPojo> itemsList = getOrderItems(sDate, eDate, null);

        List<Integer> prodIds = new ArrayList<Integer>(itemsList.stream().map(OrderItemsPojo::getProductId).collect(Collectors.toSet()));
        
        List<ProductPojo> prodList = pService.getInColumn(Arrays.asList("id"), Arrays.asList(prodIds));

        List<Integer> brandCatIds = new ArrayList<Integer>(prodList.stream().map(ProductPojo::getBrandCat).collect(Collectors.toSet()));
        
        List<BrandPojo> brandList = bService.getInColumn(Arrays.asList("id"), Arrays.asList(brandCatIds));

        return generateSalesReport(itemsList, brandList, prodList);
    }

    protected List<OrderItemsPojo> getOrderItems(ZonedDateTime sDate, ZonedDateTime eDate, List<Integer> prodIds) {
        List<OrderPojo> orderList = oService.getInDateRange(sDate, eDate);

        List<Integer> orderIds = orderList.stream().map(OrderPojo::getId).collect(Collectors.toList());
     
        if (Objects.nonNull(prodIds) && !prodIds.isEmpty()) {
            return oItemsService.getInColumn(Arrays.asList("productId", "orderId"), Arrays.asList(prodIds, orderIds));
        }

        else {
            return oItemsService.getInColumn(Arrays.asList("orderId"), Arrays.asList(orderIds));
        }

    }

    private String generateSalesReport(List<OrderItemsPojo> itemlsList, List<BrandPojo> brandList,
            List<ProductPojo> prodList) {

        HashMap<Integer, Integer> prodIdToBrandCatId = (HashMap<Integer, Integer>) prodList.stream()
                .collect(Collectors.toMap(ProductPojo::getId, ProductPojo::getBrandCat));
      
        HashMap<Integer, BrandPojo> idtoBrandCat = (HashMap<Integer, BrandPojo>) brandList.stream()
                .collect(Collectors.toMap(BrandPojo::getId, e -> e));

        HashMap<Integer, Double> brandCatToRevenue = (HashMap<Integer, Double>) brandList.stream()
                .collect(Collectors.toMap(BrandPojo::getId, e -> 0.0));

        HashMap<Integer, Integer> brandCatToQuantity = (HashMap<Integer, Integer>) brandList.stream()
                .collect(Collectors.toMap(BrandPojo::getId, e -> 0));

        for (OrderItemsPojo item : itemlsList) {
            Integer brandCatId = prodIdToBrandCatId.get(item.getProductId());
            Integer newQuantity = brandCatToQuantity.get(brandCatId) + item.getQuantity();
            Double newRevenue = brandCatToRevenue.get(brandCatId) + (item.getQuantity()*item.getSellingPrice());
            
            brandCatToQuantity.put(brandCatId, newQuantity);
            brandCatToRevenue.put(brandCatId, newRevenue);
        }

        List<SalesReportData> result = new ArrayList<SalesReportData>();

        for(Integer key: brandCatToRevenue.keySet()){
            SalesReportData data = new SalesReportData();

            data.setBrand(idtoBrandCat.get(key).getBrand());
            data.setCategory(idtoBrandCat.get(key).getCategory());
            data.setQuantity(brandCatToQuantity.get(key));
            data.setRevenue(brandCatToRevenue.get(key));

            result.add(data);
        }

        return getReportFile(result, "/sales");
    }

    //functions for brand report

    public String getBrandReport() {

        Integer totalBrands = bService.getTotalEntries();
        List<BrandData> brandList = bDto.getAll(0, totalBrands, 1, Optional.empty()).getData();

        return getReportFile(brandList, "/brands");
    }

    private <T> String getReportFile(T result, String path){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String apiUrl = "http://localhost:9500/pdf/api/reports" + path;
        ResponseEntity<String> apiResponse = restTemplate.postForEntity(apiUrl, result, String.class);
        String responseBody = apiResponse.getBody();


        return responseBody;
    }

}
