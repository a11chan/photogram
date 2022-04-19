package com.cos.photogramstart.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;


@Data
public class PrincipalDetails implements UserDetails {

  private static final long serialVersionUID = 1L;

  private User user;
  
  // 생성자 정의
  public PrincipalDetails(User user) {
    this.user = user;
  }
  
  @Override // 권한은 여러 개일 수 있어서 컬렉션 사용
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collector = new ArrayList<>();
    collector.add(() -> { return user.getRole(); }); // 람다식 사용
    return collector;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() { // 계정의 유효기간이 남았음?
    return true; // 네
  }

  @Override
  public boolean isAccountNonLocked() { // 계정이 열려있습니까?
    return true; /// 네
  }

  @Override
  public boolean isCredentialsNonExpired() { // 자격증명(비밀번호)이 유효하니? (비번 바꾼 지 1년 내임?)
    return true; // 네
  }

  @Override
  public boolean isEnabled() { // 이 계정이 활성화되었습니까?
    return true; // 네
  }

}
