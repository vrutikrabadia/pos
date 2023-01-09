package com.increff.pos.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
public class InventoryPojo {
    
    @Id
    @Getter @Setter private int id;
    @Getter @Setter private int quantity;
}
