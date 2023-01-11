package com.increff.pos.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory")
@Getter
@Setter
public class InventoryPojo extends AbstractPojo implements Serializable{
    
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
