package com.increff.pos.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products",
        uniqueConstraints = {@UniqueConstraint(name="unique_barcode", columnNames = {"barcode"})})
@Getter
@Setter
public class ProductPojo extends AbstractPojo implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable=false)
    private String barcode;
    
    @Column(nullable=false)
    private Integer brandCat;
    
    @Column(nullable=false)
    private String name;
    
    @Column(nullable=false)
    private Double mrp;
}
