package com.increff.pos.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BrandPojo {
    
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
    private String brand;
    private String category;

    
    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCategory(String category) {
        this.category = category;
    }    
}