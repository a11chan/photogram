package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity // 해당 파일로 시큐리티 활성화
@Configuration // 이 클래스를 IoC에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
	public BCryptPasswordEncoder encoded() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// super.configure(http); -> 삭제 시 기존 시큐리티 비활성화
		http.csrf().disable();
		http.authorizeRequests()
			// 지정된 주소로 요청 시 인증요구, 나머지 페이지는 허용
			.antMatchers("/","/user/**","/image/**","/subscribe/**","/comment/**").authenticated()
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/auth/signin") // GET-인증이 필요한 페이지
			.loginProcessingUrl("/auth/signin") // POST-로그인 요청시 스프링 시큐리티가 로그인 절차 진행
			.defaultSuccessUrl("/");
			// 인증이 없는 페이지로 요청 시 signin 페이지로 redirecting
			// 로그인 성공시 "/" 페이지로 이동 -- story.jsp
	}
}
