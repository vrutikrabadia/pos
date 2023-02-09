package com.increff.pos.util;

import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.increff.pos.model.data.UserPrincipal;

import lombok.Generated;

/*
https://stackoverflow.com/questions/4664893/how-to-manually-set-an-authenticated-user-in-spring-security-springmvc
*/

@Generated
public class SecurityUtil {

	public static void createContext(HttpSession session) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	}

	public static void setAuthentication(Authentication token) {
		SecurityContextHolder.getContext().setAuthentication(token);
	}

	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static UserPrincipal getPrincipal() {
		Authentication token = getAuthentication();
		return Objects.isNull(token) ? null : (UserPrincipal) getAuthentication().getPrincipal();
	}

}
