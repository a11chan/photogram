package com.cos.photogramstart.config.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service // 현재 클래스를 IoC에 등록
public class OAuth2DetailsService extends DefaultOAuth2UserService {
  
  private final UserRepository userRepository;
  
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oauth2User = super.loadUser(userRequest);
    
    Map<String, Object> userinfo = oauth2User.getAttributes();
    String username = "facebook_"+(String) userinfo.get("id");
    String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString()); // 페이스북으로 로그인하지만, DB테이블 필수값이므로 일단 저장
    String name = (String) userinfo.get("name");
    String email = (String) userinfo.get("email");
    
    User userEntity = userRepository.findByUsername(username);
    
    if(userEntity == null) {
      User user = User.builder()
          .username(username)
          .password(password)
          .email(email)
          .name(name)
          .role("ROLE_USER")
          .build();
          
      return new PrincipalDetails(userRepository.save(user), oauth2User.getAttributes());
    } else {
      return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
  }
}
