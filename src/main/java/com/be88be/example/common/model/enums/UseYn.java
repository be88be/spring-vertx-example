package com.be88be.example.common.model.enums;



import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public enum UseYn {
	YES("Y"), NO("N");							
	
	@Getter private String value;
	
	public static UseYn getEnumByValue(String value){
        for(UseYn enumObj : UseYn.values()){
            if(enumObj.value.equals(value)) return enumObj;
        }
        return null;
    }
	
	public static UseYn getEnumByString(String value){
        for(UseYn enumObj : UseYn.values()){
            if(value.equals(enumObj.value)) return enumObj;
        }
        return null;
    }	
    
}
