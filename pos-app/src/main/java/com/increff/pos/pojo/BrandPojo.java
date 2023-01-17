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
@Table(name = "brands",
        indexes ={@Index(name = "i_brand_category", columnList = "brand,category")},
        uniqueConstraints = {@UniqueConstraint(name="unique_brand_category", columnNames = {"brand", "category"})})
@Getter
@Setter
public class BrandPojo extends AbstractPojo implements Serializable{
    
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
	private Integer id;
    
    @Column(name = "brand", nullable = false)
    @Expose
    private String brand;
    
    @Column(name = "category", nullable = false)
    @Expose
    private String category;

    
     
}
