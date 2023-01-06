package com.increff.pos.model;

public class ProductData extends ProductForm{
    private int id;
    private String barCode;
    private int brandCat;
    
    public int getId() {
        return id;
    }
    public String getBarCode() {
        return barCode;
    }
    public int getBrandCat() {
        return brandCat;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    public void setBrandCat(int brandCat) {
        this.brandCat = brandCat;
    }

}
