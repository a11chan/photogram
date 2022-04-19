package com.cos.photogramstart.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final 변수 초기화 역할
@Service // 1. IoC  2. 트랜잭션 관리 
public class AuthService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional // 해당 메서드가 종료될 때까지 트랜잭션 관리 해줌
	// 이 프로젝트에선 write(insert, update, delete)일 경우에 붙여주기로 함
	public User 회원가입(User user) {
		// 회원가입 진행, 오류 발생 시 ddl-auto:create로 했다가 저장 후 원복
		// auto_increment 적용 안 되었을 때 참고
		
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRole("ROLE_USER"); // 관리자 ROLE_ADMIN
		User userEntity = userRepository.save(user);
		System.out.println(userEntity);
		return userEntity;
	}
}
