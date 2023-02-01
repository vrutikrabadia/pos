package com.increff.pos.model.form;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {

	@NotBlank(message = "email cannot be blank")
	private String email;

	@NotBlank(message = "password cannot be blank")
	private String password;
}
