package com.increff.pos.model.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

	@NotBlank(message = "Email cannot be blank")
	@Size(max = 255, message = "Email length must be less than 255 characters")
	private String email;
	
	@NotBlank(message = "Password cannot be blank")
	@Size(min = 8 ,max = 255, message = "Password length must be between 8 and 255 characters")
	private String password;
	
	@NotBlank(message = "Role cannot be blank, select one from [SUPERVISOR, OPERATOR]")
	private String role;

}
