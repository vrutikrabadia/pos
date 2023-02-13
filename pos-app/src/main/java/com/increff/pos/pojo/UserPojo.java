package com.increff.pos.pojo;

import javax.persistence.*;

import com.increff.pos.model.Roles;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "users",
		uniqueConstraints = {@UniqueConstraint(name="unique_email", columnNames = {"email"})})
@Setter

public class UserPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable=false)
	private String email;
	
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Roles role;

}
