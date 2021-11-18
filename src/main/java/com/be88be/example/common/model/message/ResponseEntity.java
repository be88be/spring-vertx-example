package com.be88be.example.common.model.message;


import com.be88be.example.common.model.enums.ResponseStatus;

import lombok.Data;

@Data
public class ResponseEntity<T> {
	
	private int code = ResponseStatus.HTTP_OK.getCode();

    private T result;

    private String message = ResponseStatus.HTTP_OK.getMessage();

    private ResponseEntity() {

    }

    private ResponseEntity(T result) {
        this();
        this.result = result;
    }

    private ResponseEntity(ResponseStatus code, T result) {
        this(result);
        this.code = code.getCode();
    }

    private ResponseEntity(ResponseStatus code, T result, String message) {
        this(result);
        this.code = code.getCode();
        this.message = message;
    }
    
    private ResponseEntity(int code, T result,  String message) {
        this(result);
        this.code = code;
        this.message = message;
    }

    public static <T> ResponseEntity<T> of() {
        return new ResponseEntity<>();
    }

    public static <T> ResponseEntity<T> of(T result) {
        return new ResponseEntity<>(result);
    }
    
    public static <T> ResponseEntity<T> of(ResponseStatus code, T result) {
        return new ResponseEntity<>(code, result);
    }

    public static <T> ResponseEntity<T> of(ResponseStatus code, T result, String details) {
        return new ResponseEntity<>(code, result, details);
    }

    public static <T> ResponseEntity<T> error(int code, String message) {
        return new ResponseEntity<>(code, null, message);
    }
}
