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

import lombok.Getter;
import lombok.Setter;

@Entity
@Table( name = "brand",
    indexes ={@Index(name = "i_brand_category", columnList = "brand,category")},
        uniqueConstraints = {@UniqueConstraint(name="unique_brand_category", columnNames = {"brand", "category"})})
@Getter
@Setter
public class BrandPojo extends AbstractPojo implements Serializable{
    
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(nullable=false)
	private Integer id;
    
    @Column(nullable=false)
    private String brand;
    
    @Column(nullable=false)
    private String category;

    
     
}
