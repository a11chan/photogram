package com.cos.photogramstart.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;

// 클래스를 메모리에 띄울 때 어떤 어노테이션을 써야 할지 모르겠으면 Component로 지정
//RestController든 Service든 모든 것들이 Component를 상속해서 구현한 것
@Component 
@Aspect
public class ValidationAdvice {
  // Controller의 파라미터가 무엇이든 상관없이 작동
  @Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))")
  public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    // proceedingJoinPoint -> @Around의 범위에 있는 모든 변수에 접근 가능한 변수
    // 여기에 적힌 내용이 @Around에 지정된 메서드보다 먼저 실행됨
    System.out.println("web api 컨트롤러===================");
    Object[] args = proceedingJoinPoint.getArgs();
    for (Object arg : args) {
      if (arg instanceof BindingResult) {
        System.out.println("유효성 검사를 하는 함수입니다.");
        BindingResult bindingResult = (BindingResult) arg;
        
        if (bindingResult.hasErrors()) {
          Map<String, String> errorMap = new HashMap<>();

          for (FieldError error : bindingResult.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
          }
          throw new CustomValidationApiException("유효성 검사 실패", errorMap);
        }
      }
    }
    
    return proceedingJoinPoint.proceed(); // 이전까지의 코드 실행 후 컨트롤러에서 호출된 함수 실행
  }
  @Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
  public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    System.out.println("web 컨트롤러===================");
    Object[] args = proceedingJoinPoint.getArgs();
    for (Object arg : args) {
      if (arg instanceof BindingResult) {
        System.out.println("유효성 검사를 하는 함수입니다.");
        
        BindingResult bindingResult = (BindingResult) arg;
        if(bindingResult.hasErrors()) {
          Map<String, String> errorMap = new HashMap<>();
          
          for(FieldError error : bindingResult.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
            System.out.println("=========================");
            System.out.println(error.getDefaultMessage());
            System.out.println("=========================");
          }
          throw new CustomValidationException("유효성 검사 실패",errorMap);
        }
      }
    }
    
    return proceedingJoinPoint.proceed();
  }
}
