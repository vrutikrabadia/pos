package com.increff.pos.dto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.SalesReportData;
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

    public List<InventoryReportData> getInventoryReport() throws ApiException {

        Integer invetorySize = iService.getTotalEntries();
        List<InventoryPojo> iList = iService.getAll(0, invetorySize);

        HashMap<Integer, Integer> prodIdtoBrandCat = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> resultMap = new HashMap<Integer, Integer>();

        for (InventoryPojo inv : iList) {
            if (!prodIdtoBrandCat.containsKey(inv.getId())) {
                ProductPojo prod = pService.get(inv.getId());
                prodIdtoBrandCat.put(prod.getId(), prod.getBrandCat());
            }
            if (!resultMap.containsKey(prodIdtoBrandCat.get(inv.getId()))) {
                resultMap.put(prodIdtoBrandCat.get(inv.getId()), 0);
            }

            Integer newQuantity = resultMap.get(prodIdtoBrandCat.get(inv.getId())) + inv.getQuantity();
            resultMap.put(prodIdtoBrandCat.get(inv.getId()), newQuantity);

        }

        List<InventoryReportData> result = new ArrayList<InventoryReportData>();

        for (Integer key : resultMap.keySet()) {
            InventoryReportData res = new InventoryReportData();
            res.setQuantity(resultMap.get(key));

            BrandPojo brand = bService.get(key);

            res.setBrand(brand.getBrand());
            res.setCategory(brand.getCategory());

            result.add(res);
        }

        return result;

    }

    public List<SalesReportData> getSalesReport(String startDate, String endDate, Optional<String> brand,
            Optional<String> category) throws ApiException {
        Date sDate;
        Date eDate;

        try {
            sDate = Date.from(LocalDate.parse(startDate).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            eDate = Date.from(LocalDate.parse(endDate).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            throw new ApiException("Error converting date");
        }

        if (sDate.compareTo(eDate) > 0) {
            throw new ApiException("start date should be less than end date");
        }


        List<OrderPojo> orderList = oService.getInDateRange(sDate, eDate);

        HashMap<Integer, Integer> prodToBrandCatId = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> resultMapQuantity = new HashMap<Integer, Integer>();
        HashMap<Integer, Double> resultMapRevenue = new HashMap<Integer, Double>();

        for (OrderPojo order : orderList) {
            List<OrderItemsPojo> itemsList = oItemsService.selectByOrderId(order.getId());

            for (OrderItemsPojo item : itemsList) {

                if (!prodToBrandCatId.containsKey(item.getProductId())) {
                    ProductPojo prod = pService.get(item.getProductId());
                    prodToBrandCatId.put(prod.getId(), prod.getBrandCat());
                }

                Integer itemBrandCat = prodToBrandCatId.get(item.getProductId());
                if (!resultMapQuantity.containsKey(itemBrandCat)) {
                    resultMapQuantity.put(itemBrandCat, 0);
                    resultMapRevenue.put(itemBrandCat, 0.0);
                }

                Integer newQuantity = resultMapQuantity.get(itemBrandCat) + item.getQuantity();
                Double newRevenue = resultMapRevenue.get(itemBrandCat) + (item.getQuantity() * item.getSellingPrice());

                resultMapQuantity.put(itemBrandCat, newQuantity);
                resultMapRevenue.put(itemBrandCat, newRevenue);
            }
        }

        List<SalesReportData> result = new ArrayList<SalesReportData>();

        if ((brand.isPresent() && !brand.get().isBlank()) && (category.isPresent() && !category.get().isBlank())) {
            BrandPojo bPojo = bService.get(brand.get(), category.get());
            SalesReportData res = new SalesReportData();
            res.setQuantity(resultMapQuantity.get(bPojo.getId()));
            res.setRevenue(resultMapRevenue.get(bPojo.getId()));

            res.setBrand(bPojo.getBrand());
            res.setCategory(bPojo.getCategory());

            result.add(res);

        } else if ((brand.isPresent() && !brand.get().isBlank())
                || (category.isPresent() && !category.get().isBlank())) {

            List<BrandPojo> bList;
            Integer totalBrands = bService.getTotalEntries();
            if ((brand.isPresent() && !brand.get().isBlank())) {
                bList = bService.searchQueryStringBrand(0, totalBrands, brand.get());
            } else {
                bList = bService.searchQueryStringCategory(0, totalBrands, category.get());
            }

            for (BrandPojo bPojo : bList) {
                SalesReportData res = new SalesReportData();
                res.setQuantity(resultMapQuantity.get(bPojo.getId()));
                res.setRevenue(resultMapRevenue.get(bPojo.getId()));

                res.setBrand(bPojo.getBrand());
                res.setCategory(bPojo.getCategory());

                result.add(res);
            }

        } else {
            for (Integer key : resultMapQuantity.keySet()) {
                SalesReportData res = new SalesReportData();
                res.setQuantity(resultMapQuantity.get(key));
                res.setRevenue(resultMapRevenue.get(key));

                BrandPojo brandPojo = bService.get(key);

                res.setBrand(brandPojo.getBrand());
                res.setCategory(brandPojo.getCategory());

                result.add(res);
            }
        }

        return result;

    }

    public List<BrandData> getBrandReport(){
        
        Integer totalBrands = bService.getTotalEntries();
        return bDto.getAll(0, totalBrands, 1, Optional.empty()).getData();
        
    }

}
