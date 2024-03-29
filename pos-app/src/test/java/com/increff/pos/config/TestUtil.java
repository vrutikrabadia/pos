package com.increff.pos.config;

import java.time.ZonedDateTime;
import java.util.Optional;

import com.increff.pos.model.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.DaySalesDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemsDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.DaySalesPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.DateTimeProvider;


@Component
public class TestUtil {
    


    @Autowired
    private BrandDao bDao;

    @Autowired
    private ProductDao pDao;

    @Autowired 
    private InventoryDao iDao;

    @Autowired
    private DaySalesDao dDao;

    @Autowired
    private OrderDao oDao;

    @Autowired
    private OrderItemsDao oiDao;

    @Autowired
    private PropertiesTest propertiesTest;

    public Optional<String> empty = Optional.empty();

    public PropertiesTest getProperties(){
        return propertiesTest;
    }
    public  BrandForm getBrandForm(String brand, String category){
        BrandForm f = new BrandForm();
        f.setBrand(brand);
        f.setCategory(category);

        return f;
    }

    public  ProductForm getProductForm(String barcode, String brand, String category, String name, Double mrp){
        ProductForm f = new ProductForm();
        f.setBarcode(barcode);
        f.setBrand(brand);
        f.setCategory(category);
        f.setMrp(mrp);
        f.setName(name);

        return f;
    }

    public  InventoryForm getInventoryForm(String barcode, Integer quantity){
        InventoryForm f = new InventoryForm();
        f.setBarcode(barcode);
        f.setQuantity(quantity);
        
        return f;
    }

    public  InventoryPojo getInventoryPojo(Integer prodId, Integer quatity){
        InventoryPojo pojo = new InventoryPojo();
        pojo.setProductId(prodId);
        pojo.setQuantity(quatity);

        return pojo;
    }

    public  OrderForm getOrderForm(){
        OrderForm form = new OrderForm();
        return form;    
    }

    public LoginForm getLoginForm(String username, String password){
        LoginForm form = new LoginForm();
        form.setEmail(username);
        form.setPassword(password);
        return form;
    }

    public  OrderItemsForm getItemsForm(String barCode, Integer quantity, Double sellingPrice){
        OrderItemsForm form = new OrderItemsForm();
        form.setBarcode(barCode);
        form.setQuantity(quantity);
        form.setSellingPrice(sellingPrice);

        return form;
    }

    public OrderItemPojo getItemsPojo(Integer orderId, Integer prodId, Integer quantity, Double sellingPrice){
        OrderItemPojo pojo = new OrderItemPojo();

        pojo.setOrderId(orderId);
        pojo.setProductId(prodId);
        pojo.setQuantity(quantity);
        pojo.setSellingPrice(sellingPrice);

        return pojo;
    }

    public  SalesReportForm getSalesReportForm(ZonedDateTime start, ZonedDateTime end, String brand, String category){
        SalesReportForm form = new SalesReportForm();
        form.setStartDate(start);
        form.setEndDate(end);
        form.setBrand(brand);
        form.setCategory(category);
        return form;
    }

    public  Integer addBrand(String brand, String category) {
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand(brand);
        pojo.setCategory(category);
        bDao.insert(pojo);
        return pojo.getId();
    }

    public  Integer addProduct(String barcode, Integer brandId , String name, Double mrp)  {
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode(barcode);
        pojo.setBrandCat(brandId);
        pojo.setName(name);
        pojo.setMrp(mrp);

        pDao.insert(pojo);

        return pojo.getId();
    }

    public  void addInventory(Integer prodId, Integer quantity){
        InventoryPojo inv = new InventoryPojo();
        inv.setProductId(prodId);
        inv.setQuantity(quantity);

        iDao.insert(inv);
    }

    public  Integer addBrandAndProduct(String brand, String category,String barcode, String name, Double mrp){
        Integer brandId = addBrand(brand, category);
        return addProduct(barcode, brandId, name, mrp);
    }

    public  void addBrandProductAndInventory(String brand, String category,String barcode, String name, Double mrp, Integer quatity){
        Integer prodId = addBrandAndProduct(brand, category, barcode, name, mrp);
        addInventory(prodId, quatity);
    }

    public  Integer addOrder(){
        OrderPojo order = new OrderPojo();
        oDao.insert(order);

        return order.getId();
    }


    public  void addOrderItem(Integer orderId, Integer prodId, Integer quantity, Double sellingPrice){
        OrderItemPojo item = new OrderItemPojo();
        item.setOrderId(orderId);
        item.setProductId(prodId);
        item.setQuantity(quantity);
        item.setSellingPrice(sellingPrice);

        oiDao.insert(item);
    }

    public  void createOrder(String brand, String category,String barcode, String name, Double mrp, Integer quatity, Integer orderQuantity, Double sellingPrice){
        Integer prodId = addBrandAndProduct(brand, category, barcode, name, mrp);
        addInventory(prodId, quatity);
        Integer orderId = addOrder();
        addOrderItem(orderId, prodId, orderQuantity, sellingPrice);
    }

    public  void addDaySales(ZonedDateTime date, Integer itemsCount, Integer orderCount, Double sales){
        DaySalesPojo dPojo = new DaySalesPojo();
        dPojo.setDate(date);
        dPojo.setInvoicedItemCount(itemsCount);
        dPojo.setInvoicedOrderCount(orderCount);
        dPojo.setTotalRevenue(sales);

        dDao.insert(dPojo);
    }

    public DaySalesPojo getDaySalesPojo(Integer itemsCount, Integer orderCount, Double totalRevenue){
        DaySalesPojo pojo = new DaySalesPojo();

        pojo.setDate(DateTimeProvider.getInstance().timeNow().withHour(12).withMinute(0).withSecond(0));
        pojo.setInvoicedItemCount(itemsCount);
        pojo.setInvoicedOrderCount(orderCount);
        pojo.setTotalRevenue(totalRevenue);


        return pojo;
    }

}
