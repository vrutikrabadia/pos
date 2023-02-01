package com.increff.pos.model.form;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

	@NotBlank(message = "email cannot be blank")
	private String email;
	
	@NotBlank(message = "password cannot be blank")
	private String password;
	
	@NotBlank(message = "role cannot be blank select on from [supervisor, operator]")
	private String role;

}
