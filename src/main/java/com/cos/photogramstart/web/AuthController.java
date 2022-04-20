package com.cos.photogramstart.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.internal.build.AllowSysOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // DI할 때 사용, final이 있는 변수 모두
@Controller // IoC에 등록, 파일을 리턴하는 역할
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	private final AuthService authService;

//	public AuthController(AuthService authService) {
//		this.authService = authService;
//	}
	
	
	@GetMapping("/auth/signin")
	public String signinForm() {
		return "auth/signin";
	}

	@GetMapping("/auth/signup")
	public String signupForm() {
		return "auth/signup";
	}
	
	// 회원가입 처리 메서드 -- signup.jsp에서 온 요청 처리
	// key-value 형태(x-www-form-urlencoded)
	@PostMapping("/auth/signup")
	public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) {
		
		User user = signupDto.toEntity();
		authService.회원가입(user);
		
		// 회원가입 성공시 signin 페이지로 이동
		return "auth/signin";
		
	}
}