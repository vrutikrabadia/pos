package com.increff.pos.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ProductPojo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String barCode;
    private int brandCat;
    private String name;
    private double mrp;
    
    public int getId() {
        return id;
    }
    public String getBarCode() {
        return barCode;
    }
    public int getBrandCat() {
        return brandCat;
    }
    public String getName() {
        return name;
    }
    public double getMrp() {
        return mrp;
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
    public void setName(String name) {
        this.name = name;
    }
    public void setMrp(double mrp) {
        this.mrp = mrp;
    }
}
