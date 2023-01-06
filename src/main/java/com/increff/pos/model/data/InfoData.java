package com.increff.pos.model.data;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class InfoData implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter @Setter private String message;
	@Getter @Setter private String email;

	public InfoData() {
		message = "No message";
		email = "No email";
	}
	
}
