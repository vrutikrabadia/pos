package com.increff.pos.dto;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.ClientWrapper;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

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
    private ClientWrapper clientWrapper;



    //functions for inventory report

    public String getInventoryReport() throws ApiException {

        List<InventoryPojo> inventoryPojoList = iService.getAll();
        HashMap<Integer, Integer> productIdToBrandCatMap = getProductIdToBrandCatMap(inventoryPojoList);
        List<Integer> brandCatIds = productIdToBrandCatMap.values().stream().distinct().collect(Collectors.toList());
        HashMap<Integer, Integer> resultMap = getBrandCatIdToQuantityMap(productIdToBrandCatMap, inventoryPojoList, brandCatIds);

        return invReportFromMap(resultMap, brandCatIds);
    }

    private String invReportFromMap(HashMap<Integer, Integer> resultMap, List<Integer> brandCatIds) throws ApiException {
        List<InventoryReportData> result = new ArrayList<InventoryReportData>();

        List<BrandPojo> brandList = bService.getInColumns(singletonList("id"), singletonList(brandCatIds));

        HashMap<Integer, BrandPojo> idtoBrandCat = (HashMap<Integer, BrandPojo>) brandList.stream()
                .collect(Collectors.toMap(BrandPojo::getId, e -> e));

        for (Integer key : resultMap.keySet()) {
            InventoryReportData res = new InventoryReportData();
            res.setQuantity(resultMap.get(key));

            res.setBrand(idtoBrandCat.get(key).getBrand());
            res.setCategory(idtoBrandCat.get(key).getCategory());

            result.add(res);
        }

        if(result.isEmpty()) {
            throw new ApiException("No inventory data found");
        }
        
        return clientWrapper.getPdfClient().getPdfBase64(result, "inventoryReport");
    }

    protected HashMap<Integer, Integer> getProductIdToBrandCatMap(List<InventoryPojo> iList) throws ApiException {
        List<Integer> productIdList = iList.stream().map(InventoryPojo::getProductId).collect(Collectors.toList());
        List<ProductPojo> productList;
        try {
            productList = pService.getInColumns(singletonList("id"), singletonList(productIdList));
        } catch (ApiException e) {
            throw new ApiException("Runtime Exception");
        }

        return (HashMap<Integer, Integer>) productList.stream()
                .collect(Collectors.toMap(ProductPojo::getId, ProductPojo::getBrandCat));
    }

    protected HashMap<Integer, Integer> getBrandCatIdToQuantityMap(HashMap<Integer, Integer> productIdToBrandCat,
                                                                   List<InventoryPojo> iList, List<Integer> brandCatIds) {

        HashMap<Integer, Integer> resultMap = (HashMap<Integer, Integer>) brandCatIds.stream()
                .collect(Collectors.toMap(Function.identity(), i -> 0));

        for (InventoryPojo inv : iList) {

            Integer newQuantity = resultMap.get(productIdToBrandCat.get(inv.getProductId())) + inv.getQuantity();
            resultMap.put(productIdToBrandCat.get(inv.getProductId()), newQuantity);
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
            throw new ApiException("Start date should be less than End date");
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
            BrandPojo bPojo = bService.getCheckByBrandCategory(brand, category);
            brandList.add(bPojo);
        } else if ((Objects.nonNull(brand) && !brand.isEmpty())
                || (Objects.nonNull(category) && !category.isEmpty())) {

            if ((Objects.nonNull(brand) && !brand.isEmpty())) {
                brandList = bService.getInColumns(singletonList("brand"), singletonList(singletonList(brand)));
            } else {
                brandList = bService.getInColumns(singletonList("category"), singletonList(singletonList(category)));
            }
        }

        List<Integer> brandCatIds = brandList.stream().map(BrandPojo::getId).collect(Collectors.toList());

        List<ProductPojo> prodList = pService.getInColumns(singletonList("brandCat"), singletonList(brandCatIds));

        List<Integer> prodIds = prodList.stream().map(ProductPojo::getId).collect(Collectors.toList());

        List<OrderItemPojo> itemsList = getOrderItems(sDate, eDate, prodIds);

        return generateSalesReport(itemsList, brandList, prodList);

    }

    private String generateSalesReportOnlyDates(ZonedDateTime sDate, ZonedDateTime eDate) throws ApiException {

        List<OrderItemPojo> itemsList = getOrderItems(sDate, eDate, null);

        List<Integer> prodIds = itemsList.stream().map(OrderItemPojo::getProductId).distinct().collect(Collectors.toList());
        
        List<ProductPojo> prodList;
        try {
            prodList = pService.getInColumns(singletonList("id"), singletonList(prodIds));
        } catch (ApiException e) {
            throw new ApiException("Runtime exception");
        }

        List<Integer> brandCatIds = prodList.stream().map(ProductPojo::getBrandCat).distinct().collect(Collectors.toList());
        
        List<BrandPojo> brandList = bService.getInColumns(singletonList("id"), singletonList(brandCatIds));

        return generateSalesReport(itemsList, brandList, prodList);
    }

    protected List<OrderItemPojo> getOrderItems(ZonedDateTime sDate, ZonedDateTime eDate, List<Integer> prodIds) {
        List<OrderPojo> orderList = oService.getInDateRange(sDate, eDate);

        List<Integer> orderIds = orderList.stream().map(OrderPojo::getId).collect(Collectors.toList());
     
        if (Objects.nonNull(prodIds) && !prodIds.isEmpty()) {
            return oItemsService.getInColumn(Arrays.asList("productId", "orderId"), Arrays.asList(prodIds, orderIds));
        }

        else {
            return oItemsService.getInColumn(singletonList("orderId"), singletonList(orderIds));
        }

    }

    private String generateSalesReport(List<OrderItemPojo> itemlsList, List<BrandPojo> brandList,
                                       List<ProductPojo> prodList) throws ApiException {

        HashMap<Integer, Integer> prodIdToBrandCatId = (HashMap<Integer, Integer>) prodList.stream()
                .collect(Collectors.toMap(ProductPojo::getId, ProductPojo::getBrandCat));
      
        HashMap<Integer, BrandPojo> idToBrandCat = (HashMap<Integer, BrandPojo>) brandList.stream()
                .collect(Collectors.toMap(BrandPojo::getId, e -> e));

        HashMap<Integer, Double> brandCatToRevenue = (HashMap<Integer, Double>) brandList.stream()
                .collect(Collectors.toMap(BrandPojo::getId, e -> 0.0));

        HashMap<Integer, Integer> brandCatToQuantity = (HashMap<Integer, Integer>) brandList.stream()
                .collect(Collectors.toMap(BrandPojo::getId, e -> 0));

        for (OrderItemPojo item : itemlsList) {
            Integer brandCatId = prodIdToBrandCatId.get(item.getProductId());
            Integer newQuantity = brandCatToQuantity.get(brandCatId) + item.getQuantity();
            Double newRevenue = brandCatToRevenue.get(brandCatId) + (item.getQuantity()*item.getSellingPrice());
            
            brandCatToQuantity.put(brandCatId, newQuantity);
            brandCatToRevenue.put(brandCatId, newRevenue);
        }

        List<SalesReportData> result = new ArrayList<SalesReportData>();

        for(Integer key: brandCatToRevenue.keySet()){
            SalesReportData data = new SalesReportData();
            data.setBrand(idToBrandCat.get(key).getBrand());
            data.setCategory(idToBrandCat.get(key).getCategory());
            data.setQuantity(brandCatToQuantity.get(key));
            data.setRevenue(brandCatToRevenue.get(key));

            result.add(data);
        }

        if (result.isEmpty()) {
            throw new ApiException("No sales found");
        }

        return clientWrapper.getPdfClient().getPdfBase64(result, "salesReport");
    }

    //functions for brand report

    public String getBrandReport() throws ApiException {

        List<BrandData> brandList = bDto.getAll();
        if(brandList.isEmpty()){
            throw new ApiException("No brands found");
        }

        return clientWrapper.getPdfClient().getPdfBase64(brandList, "brandReport");
    }
}
