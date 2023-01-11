package com.increff.pos.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "brands",
        indexes ={@Index(name = "i_brand_category", columnList = "brand,category", unique = true)})
@Getter
@Setter
public class BrandPojo extends AbstractPojo implements Serializable{
    
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
	private Integer id;
    @Column(name = "brand", nullable = false)
    private String brand;
    @Column(name = "category", nullable = false)
    private String category;

    
     
}
