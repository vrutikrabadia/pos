package com.increff.pos.model.form;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

	@NotBlank(message = "Email cannot be blank")
	private String email;
	
	@NotBlank(message = "Password cannot be blank")
	private String password;
	
	@NotBlank(message = "Role cannot be blank, select one from [supervisor, operator]")
	private String role;

}
