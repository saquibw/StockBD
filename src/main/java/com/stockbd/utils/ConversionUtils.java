package com.stockbd.utils;

import java.math.BigDecimal;

import org.springframework.util.StringUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConversionUtils {
	public Double parseDouble(String price) {
		if (StringUtils.hasText(price)) {
			BigDecimal value = new BigDecimal(sanitize(price));
			return value.doubleValue();
		}
		return 0.0;
	}
	
	public BigDecimal parseBigDecimal(String price) {
	    if (StringUtils.hasText(price)) {
	        return new BigDecimal(sanitize(price));
	    }
	    return BigDecimal.ZERO;
	}
	
	public Long parseLong(String price) {
		if (StringUtils.hasText(price)) {
			return Long.parseLong(sanitize(price));
		}
		return 0l;
	}
	
	public String sanitize(String input) {
        if (input == null) {
            return null;
        }
        return input.replace(",", "");
    }
}
