package com.cos.photogramstart.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController // 응답을 위해
@ControllerAdvice // Exception을 모두 가로챔
public class ControllerExceptionHandler {

  // RuntimeException이 발생하는 모든 예외를 아래 메서드가 가로챔
  @ExceptionHandler(CustomValidationException.class)
  public String validationException(CustomValidationException e) {
    // 1.클라이언트에 응답할 때는 script가 좋음
    // 2.Ajax 통신에서는 Dto 방식이 좋음
    // 3.android 통신에서는 Dto 방식이 좋음
    // 2~3번은 에러 메시지를 개발자가 받음, 1번은 사용자가 받음

    if (e.getErrorMap() == null) {
      return Script.back(e.getMessage());
    } else {
      return Script.back(e.getErrorMap().toString()); // JS로 응답
    }

  }

  @ExceptionHandler(CustomException.class)
  public String exception(CustomException e) {
    return Script.back(e.getMessage());

  }

  // 리텬형<?> 으로 하면 리턴할 때 타입이 자동으로 결정됨
  @ExceptionHandler(CustomValidationApiException.class)
  public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
    return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CustomApiException.class)
  public ResponseEntity<?> apiException(CustomApiException e) {
    return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);

    // public CMRespDto<?> validationException(CustomValidationException e) {
    // return new CMRespDto<Map<String,String>>(-1,e.getMessage(),e.getErrorMap());
  }

}
