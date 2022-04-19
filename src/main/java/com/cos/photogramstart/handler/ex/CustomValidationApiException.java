package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomValidationApiException extends RuntimeException {

	// 시리얼은 객체를 구분할 때 사용
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> errorMap;
	
	public CustomValidationApiException(String message) {
    super(message);   
  }
	
	public CustomValidationApiException(String message, Map<String, String> errorMap) {
		super(message);		
		this.errorMap = errorMap;
	}
	
	// message는 부모 클래스에 getter가 있으므로 super()로 대체
	public Map<String, String> getErrorMap() {
		return errorMap;
	}

}
