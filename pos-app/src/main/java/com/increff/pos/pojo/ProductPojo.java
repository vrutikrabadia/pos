package com.increff.pos.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products",
        indexes ={@Index(name = "i_product_barcode", columnList = "barcode", unique = true)},
        uniqueConstraints = {@UniqueConstraint(name="unique_barcode", columnNames = {"barcode"})})
@Getter
@Setter
public class ProductPojo extends AbstractPojo implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @Column(name = "barcode", nullable = false)
    @Expose
    private String barcode;
    
    @Column(name = "brand_cat", nullable = false)
    @Expose
    private Integer brandCat;
    
    @Column(name = "name", nullable = false)
    @Expose
    private String name;
    
    @Column(name = "mrp", nullable = false)
    @Expose
    private Double mrp;
}
