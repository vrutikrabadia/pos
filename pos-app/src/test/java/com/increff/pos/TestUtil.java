package com.increff.pos;

import java.time.ZonedDateTime;

import javax.annotation.PostConstruct;

import com.increff.pos.util.DateTimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.DaySalesDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemsDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.OrderItemsForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.DaySalesPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;

@Component
public class TestUtil {
    
    private static ProductDao pDao;
    private static BrandDao bDao;
    private static InventoryDao iDao;
    private static DaySalesDao dDao;
    private static OrderDao oDao;
    private static OrderItemsDao oiDao;


    @Autowired
    private BrandDao brandDao;

    @Autowired
    private ProductDao productDao;

    @Autowired 
    private InventoryDao inventoryDao;

    @Autowired
    private DaySalesDao daySalesDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemsDao orderItemsDao;

    @PostConstruct
    private void init(){
        bDao = brandDao;
        pDao = productDao;
        iDao = inventoryDao;
        dDao = daySalesDao;
        oDao = orderDao;
        oiDao = orderItemsDao;
    }

    public static BrandForm getBrandForm(String brand, String category){
        BrandForm f = new BrandForm();
        f.setBrand(brand);
        f.setCategory(category);

        return f;
    }

    public static ProductForm getProductForm(String barcode, String brand, String category, String name, Double mrp){
        ProductForm f = new ProductForm();
        f.setBarcode(barcode);
        f.setBrand(brand);
        f.setCategory(category);
        f.setMrp(mrp);
        f.setName(name);

        return f;
    }

    public static InventoryForm getInventoryForm(String barcode, Integer quantity){
        InventoryForm f = new InventoryForm();
        f.setBarcode(barcode);
        f.setQuantity(quantity);
        
        return f;
    }

    public static InventoryPojo getInventoryPojo(Integer prodId, Integer quatity){
        InventoryPojo pojo = new InventoryPojo();
        pojo.setId(prodId);
        pojo.setQuantity(quatity);

        return pojo;
    }

    public static OrderForm getOrderForm(){
        OrderForm form = new OrderForm();
        return form;    
    }

    public static OrderItemsForm getItemsForm(String barCode, Integer quantity, Double sellingPrice){
        OrderItemsForm form = new OrderItemsForm();
        form.setBarcode(barCode);
        form.setQuantity(quantity);
        form.setSellingPrice(sellingPrice);

        return form;
    }

    public static OrderItemsPojo getItemsPojo(Integer orderId, Integer prodId, Integer quantity, Double sellingPrice){
        OrderItemsPojo pojo = new OrderItemsPojo();

        pojo.setOrderId(orderId);
        pojo.setProductId(prodId);
        pojo.setQuantity(quantity);
        pojo.setSellingPrice(sellingPrice);

        return pojo;
    }

    public static SalesReportForm getSalesReportForm(ZonedDateTime start, ZonedDateTime end, String brand, String category){
        SalesReportForm form = new SalesReportForm();
        form.setStartDate(start);
        form.setEndDate(end);
        form.setBrand(brand);
        form.setCategory(category);
        return form;
    }

    public static Integer addBrand(String brand, String category) {
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand(brand);
        pojo.setCategory(category);
        bDao.insert(pojo);
        return pojo.getId();
    }

    public static Integer addProduct(String barcode, Integer brandId , String name, Double mrp)  {
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode(barcode);
        pojo.setBrandCat(brandId);
        pojo.setName(name);
        pojo.setMrp(mrp);

        pDao.insert(pojo);

        return pojo.getId();
    }

    public static void addInventory(Integer prodId, Integer quantity){
        InventoryPojo inv = new InventoryPojo();
        inv.setId(prodId);
        inv.setQuantity(quantity);

        iDao.insert(inv);
    }

    public static Integer addBrandAndProduct(String brand, String category,String barcode, String name, Double mrp){
        Integer brandId = addBrand(brand, category);
        return addProduct(barcode, brandId, name, mrp);
    }

    public static void addBrandProductAndInventory(String brand, String category,String barcode, String name, Double mrp, Integer quatity){
        Integer prodId = addBrandAndProduct(brand, category, barcode, name, mrp);
        addInventory(prodId, quatity);
    }

    public static Integer addOrder(){
        OrderPojo order = new OrderPojo();
        oDao.insert(order);

        return order.getId();
    }


    public static void addOrderItem(Integer orderId, Integer prodId, Integer quantity, Double sellingPrice){
        OrderItemsPojo item = new OrderItemsPojo();
        item.setOrderId(orderId);
        item.setProductId(prodId);
        item.setQuantity(quantity);
        item.setSellingPrice(sellingPrice);

        oiDao.insert(item);
    }

    public static void createOrder(String brand, String category,String barcode, String name, Double mrp, Integer quatity, Integer orderQuantity, Double sellingPrice){
        Integer prodId = addBrandAndProduct(brand, category, barcode, name, mrp);
        addInventory(prodId, quatity);
        Integer orderId = addOrder();
        addOrderItem(orderId, prodId, orderQuantity, sellingPrice);
    }

    public static void addDaySales(ZonedDateTime date, Integer itemsCount, Integer orderCount, Double sales){
        DaySalesPojo dPojo = new DaySalesPojo();
        dPojo.setDate(date);
        dPojo.setItemsCount(itemsCount);
        dPojo.setOrderCount(orderCount);
        dPojo.setTotalRevenue(sales);

        dDao.insert(dPojo);
    }

    public static DaySalesPojo getDaySalesPojo(Integer itemsCount, Integer orderCount, Double totalRevenue){
        DaySalesPojo pojo = new DaySalesPojo();

        pojo.setDate(DateTimeProvider.getInstance().timeNow().withHour(12).withMinute(0).withSecond(0));
        pojo.setItemsCount(itemsCount);
        pojo.setOrderCount(orderCount);
        pojo.setTotalRevenue(totalRevenue);


        return pojo;
    }

}
