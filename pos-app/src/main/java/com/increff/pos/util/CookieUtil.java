package com.increff.pos.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import lombok.Generated;


/**
 * Consists of methods that handles cookies and authenticate users.
 */
@Generated
public class CookieUtil {

	
	/**
	 * This method is used to check the cookie recieved in the HttpServletRequest and verifies the user. 
	 * @param HttpServletRequest req
	 * @param String name
	 * @return String
	 */
	public static String getCookie(HttpServletRequest req, String name) {
		Cookie[] cookies = req.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie c : cookies) {
			if (name.equals(c.getName())) {
				return c.getValue();
			}
		}

		return null;
	}
}
