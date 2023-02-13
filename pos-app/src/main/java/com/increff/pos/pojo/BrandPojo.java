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
@Table( name = "brands",
        uniqueConstraints = {@UniqueConstraint(name="unique_brand_category", columnNames = {"brand", "category"})})
@Getter
@Setter
public class BrandPojo extends AbstractPojo {
    
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
    
    @Column(nullable=false)
    private String brand;
    
    @Column(nullable=false)
    private String category;

    
     
}
