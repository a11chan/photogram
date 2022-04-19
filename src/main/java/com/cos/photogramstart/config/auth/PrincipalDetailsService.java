package com.cos.photogramstart.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service // IoC 컨테이너에 등록, POST 로그인 시 로그인 정보 낚아채서 처리
public class PrincipalDetailsService implements UserDetailsService {

  private final UserRepository userRepository;
  
  // 패스워드는 자동으로 확인하니 신경X
  // 리턴이 잘 되면 자동으로 UserDetails 타입을 세션으로 생성
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User userEntity = userRepository.findByUsername(username);
    
    if(userEntity == null) {
      return null;
    } else {
      return new PrincipalDetails(userEntity);
    }
    
  }

}
