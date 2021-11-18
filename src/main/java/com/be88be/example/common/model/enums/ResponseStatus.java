package com.be88be.example.common.model.enums;



import org.apache.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public enum ResponseStatus {
	HTTP_OK(HttpStatus.SC_OK, "ok"),
	HTTP_NOT_FOUND(HttpStatus.SC_NOT_FOUND, "resource not found"),
	HTTP_SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "internal server error"),
	
	METHOD_TYPE_NULL(600, "method type is null"),
	METHOD_NOT_MATCH(601, "cannot find matched method type"),
	
	CLIENT_SEND_OK(700, "web client send success"),
	CLIENT_SEND_ERROR(701, "web client send error");
	
	@Getter
	private Integer code = null;
	@Getter
	private String message = null;
	
	public static ResponseStatus getEnumByValue(String code){
		if (code == null) {
			return null;
		}
        for (ResponseStatus enumObj : ResponseStatus.values()){
            if (code.equals(enumObj.code)) return enumObj;
        }
        return null;
	}
}
