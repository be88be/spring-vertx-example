package com.be88be.example.common.model.enums;


import java.util.ArrayList;
import java.util.List;

import io.vertx.core.http.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public enum Api {
	GET_TASK("/task", HttpMethod.GET,"작업"),
	GET_TEST("/test", HttpMethod.GET,"테스트");

	@Getter
	private String value = null;
	
	@Getter
	private HttpMethod httpMethod = null;
	
	@Getter
	private String text = null;
	
	public static Api getEnumByValue(String value){
		if (value == null) {
			return null;
		}
        for (Api enumObj : Api.values()){
            if (value.equals(enumObj.value)) return enumObj;
        }
        return null;
	}
	
	public static List<Api> getApiValues() {
		List<Api> values = new ArrayList<Api>();
        for (Api enumObj : Api.values()) {
        	values.add(enumObj);
        }
        return values;
	}
}
