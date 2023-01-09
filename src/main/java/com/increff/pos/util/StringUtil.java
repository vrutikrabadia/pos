package com.increff.pos.util;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.UserPojo;

public class StringUtil {

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static String toLowerCase(String s) {
		return s == null ? null : s.trim().toLowerCase();
	}

	public static void normaliseBrand(BrandPojo p){
		p.setBrand(StringUtil.toLowerCase(p.getBrand()));
	}


	public static void normaliseProduct(ProductPojo p){
		p.setName(StringUtil.toLowerCase(p.getName()));
        p.setBarCode(StringUtil.toLowerCase(p.getBarCode()));
	}

	public static void normalizeUser(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
		p.setRole(p.getRole().toLowerCase().trim());
	}


}
