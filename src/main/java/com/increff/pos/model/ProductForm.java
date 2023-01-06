package com.increff.pos.model;

public class ProductForm {
    private String brand;
    private String category;
    private String name;
    private double mrp;

    public String getBrand() {
        return brand;
    }
    public String getCategory() {
        return category;
    }
    public String getName() {
        return name;
    }
    public double getMrp() {
        return mrp;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMrp(double mrp) {
        this.mrp = mrp;
    }
    
}
