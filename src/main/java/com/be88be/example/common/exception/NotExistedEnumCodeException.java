package com.be88be.example.common.exception;

@SuppressWarnings("serial")
public class NotExistedEnumCodeException extends Exception {

	public NotExistedEnumCodeException() {}	
	public NotExistedEnumCodeException(String message) { super(message); }	
	public NotExistedEnumCodeException(String message, Throwable cause) { super(message, cause); }	
	public NotExistedEnumCodeException(Throwable cause) { super(cause); }	
}
