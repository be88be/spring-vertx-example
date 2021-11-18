package com.be88be.example.common.model.message;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.be88be.example.common.model.enums.Api;

import lombok.Data;

@Data
public class RequestBody {
	private Api api;
	private String sender;
}
